using UnityEngine;

/// <summary>
/// 掘进机动画状态机驱动
/// 负责将运行状态同步到 Animator 参数，驱动动画切换
/// 与 DiggerController 配合挂载在同一 GameObject 上
/// </summary>
[RequireComponent(typeof(Animator))]
public class DiggerAnimator : MonoBehaviour
{
    // Animator 参数名称常量（需与 DiggerAnimator.controller 中一致）
    private static readonly int ParamIsMoving   = Animator.StringToHash("IsMoving");
    private static readonly int ParamIsCutting  = Animator.StringToHash("IsCutting");
    private static readonly int ParamEmergency  = Animator.StringToHash("Emergency");
    private static readonly int ParamMoveSpeed  = Animator.StringToHash("MoveSpeed");

    private Animator _animator;
    private Rigidbody _rb;

    private void Awake()
    {
        _animator = GetComponent<Animator>();
        _rb = GetComponent<Rigidbody>();
    }

    private void Update()
    {
        // 实时更新移动速度参数，用于混合树平滑过渡
        if (_rb != null)
        {
            float speed = new Vector3(_rb.velocity.x, 0, _rb.velocity.z).magnitude;
            _animator.SetFloat(ParamMoveSpeed, speed, 0.1f, Time.deltaTime);
        }
    }

    /// <summary>
    /// 根据掘进机状态更新动画参数
    /// 由 DiggerController 在状态变化时调用
    /// </summary>
    public void SetState(DiggerState state)
    {
        // 重置所有 bool 参数
        _animator.SetBool(ParamIsMoving,  false);
        _animator.SetBool(ParamIsCutting, false);
        _animator.SetBool(ParamEmergency, false);

        switch (state)
        {
            case DiggerState.Moving:
                _animator.SetBool(ParamIsMoving, true);
                break;

            case DiggerState.Cutting:
                _animator.SetBool(ParamIsCutting, true);
                break;

            case DiggerState.Emergency:
                _animator.SetBool(ParamEmergency, true);
                _animator.SetTrigger("EmergencyTrigger"); // 触发一次性紧急动画
                break;

            case DiggerState.Idle:
            default:
                // 全部 false → 自动播放 Idle 动画
                break;
        }
    }
}
