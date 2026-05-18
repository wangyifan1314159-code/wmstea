using UnityEngine;

/// <summary>
/// 掘进机主控制器
/// 负责接收指令并驱动模型进行位移、旋转、截割等操作
/// 挂载到场景中的 DiggerMachine GameObject 上
/// </summary>
[RequireComponent(typeof(Rigidbody))]
[RequireComponent(typeof(DiggerAnimator))]
public class DiggerController : MonoBehaviour
{
    // ─── Inspector 可配置参数 ──────────────────────────────────────────
    [Header("移动参数")]
    [Tooltip("前进/后退速度 (m/s)")]
    public float moveSpeed = 2.0f;

    [Tooltip("左右转向速度 (deg/s)")]
    public float turnSpeed = 30.0f;

    [Header("截割参数")]
    [Tooltip("截割头旋转速度 (deg/s)")]
    public float cutterSpeed = 360.0f;

    [Tooltip("截割头上下摆动速度 (deg/s)")]
    public float cutterSwingSpeed = 15.0f;

    [Header("引用")]
    [Tooltip("截割头 Transform（子物体）")]
    public Transform cutterHead;

    // ─── 私有字段 ──────────────────────────────────────────────────────
    private Rigidbody _rb;
    private DiggerAnimator _animator;

    // 当前运行状态
    private DiggerState _state = DiggerState.Idle;

    // 由 RemoteCommandReceiver 写入的目标指令
    private DiggerCommand _currentCommand;

    // 累计截割头摆动角度（上下 ±30°）
    private float _cutterSwingAngle = 0f;
    private int _cutterSwingDirection = 1;

    // ─── Unity 生命周期 ────────────────────────────────────────────────
    private void Awake()
    {
        _rb = GetComponent<Rigidbody>();
        _animator = GetComponent<DiggerAnimator>();

        // 掘进机不受重力翻倒影响
        _rb.constraints = RigidbodyConstraints.FreezeRotationX
                        | RigidbodyConstraints.FreezeRotationZ;
    }

    private void FixedUpdate()
    {
        ExecuteCommand(_currentCommand);
    }

    // ─── 公开接口（供 RemoteCommandReceiver 调用）─────────────────────

    /// <summary>
    /// 接收来自远程控制台的指令
    /// </summary>
    public void ReceiveCommand(DiggerCommand cmd)
    {
        _currentCommand = cmd;
    }

    /// <summary>
    /// 紧急停止（硬件联锁触发时直接调用）
    /// </summary>
    public void EmergencyStop()
    {
        _currentCommand = new DiggerCommand { type = CommandType.EmergencyStop };
        _rb.velocity = Vector3.zero;
        _rb.angularVelocity = Vector3.zero;
        _animator.SetState(DiggerState.Emergency);
        _state = DiggerState.Emergency;
        Debug.LogWarning("[DiggerController] 紧急停止触发！");
    }

    // ─── 私有方法 ──────────────────────────────────────────────────────

    private void ExecuteCommand(DiggerCommand cmd)
    {
        switch (cmd.type)
        {
            case CommandType.MoveForward:
                Move(1);
                SetState(DiggerState.Moving);
                break;

            case CommandType.MoveBackward:
                Move(-1);
                SetState(DiggerState.Moving);
                break;

            case CommandType.TurnLeft:
                Turn(-1);
                SetState(DiggerState.Moving);
                break;

            case CommandType.TurnRight:
                Turn(1);
                SetState(DiggerState.Moving);
                break;

            case CommandType.StartCutting:
                StartCutting();
                SetState(DiggerState.Cutting);
                break;

            case CommandType.StopCutting:
                StopCutting();
                SetState(DiggerState.Idle);
                break;

            case CommandType.EmergencyStop:
                // 已在 EmergencyStop() 中处理
                break;

            default:
                // 无指令时减速停止
                Decelerate();
                if (_state != DiggerState.Emergency)
                    SetState(DiggerState.Idle);
                break;
        }
    }

    private void Move(float direction)
    {
        Vector3 velocity = transform.forward * direction * moveSpeed;
        velocity.y = _rb.velocity.y; // 保留竖直速度（重力）
        _rb.velocity = velocity;
    }

    private void Turn(float direction)
    {
        float angle = direction * turnSpeed * Time.fixedDeltaTime;
        _rb.MoveRotation(_rb.rotation * Quaternion.Euler(0, angle, 0));
    }

    private void StartCutting()
    {
        if (cutterHead == null) return;

        // 截割头自转
        cutterHead.Rotate(Vector3.forward, cutterSpeed * Time.fixedDeltaTime, Space.Self);

        // 截割头上下摆动 ±30°
        float swing = _cutterSwingDirection * cutterSwingSpeed * Time.fixedDeltaTime;
        _cutterSwingAngle += swing;
        cutterHead.Rotate(Vector3.right, swing, Space.World);

        if (Mathf.Abs(_cutterSwingAngle) >= 30f)
        {
            _cutterSwingDirection *= -1;
        }
    }

    private void StopCutting()
    {
        // 截割头回中（平滑归零）
        if (cutterHead == null) return;
        cutterHead.localRotation = Quaternion.Slerp(
            cutterHead.localRotation,
            Quaternion.identity,
            Time.fixedDeltaTime * 2f
        );
        _cutterSwingAngle = 0f;
    }

    private void Decelerate()
    {
        _rb.velocity = Vector3.Lerp(_rb.velocity, Vector3.zero, Time.fixedDeltaTime * 5f);
    }

    private void SetState(DiggerState newState)
    {
        if (_state == newState) return;
        _state = newState;
        _animator.SetState(newState);
    }
}

// ─── 数据结构定义 ──────────────────────────────────────────────────────────

/// <summary>掘进机运行状态枚举</summary>
public enum DiggerState
{
    Idle,       // 待机
    Moving,     // 行走
    Cutting,    // 截割
    Emergency   // 紧急停止
}

/// <summary>远程控制指令类型</summary>
public enum CommandType
{
    None,
    MoveForward,
    MoveBackward,
    TurnLeft,
    TurnRight,
    StartCutting,
    StopCutting,
    EmergencyStop
}

/// <summary>远程控制指令结构体</summary>
[System.Serializable]
public struct DiggerCommand
{
    public CommandType type;
    public float value; // 可选：速度倍率、角度等附加参数
}
