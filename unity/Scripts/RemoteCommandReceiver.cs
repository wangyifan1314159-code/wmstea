using System;
using System.Text;
using UnityEngine;
using UnityEngine.Events;

/// <summary>
/// 远程控制指令接收器
/// 模拟通过 MQTT / UDP / WebSocket 接收地面集控中心下发的 JSON 指令，
/// 解析后转发给 DiggerController 执行。
///
/// 真实项目中将此脚本中的 SimulateLocalInput() 替换为 MQTT 订阅回调即可。
/// 推荐 MQTT 插件：M2MqttUnity (https://github.com/gpvigano/M2MqttUnity)
/// </summary>
public class RemoteCommandReceiver : MonoBehaviour
{
    // ─── Inspector 配置 ────────────────────────────────────────────────
    [Header("MQTT 连接配置")]
    [Tooltip("MQTT Broker 地址（5G边缘网关 IP）")]
    public string brokerAddress = "192.168.1.100";

    [Tooltip("MQTT 端口")]
    public int brokerPort = 1883;

    [Tooltip("订阅的控制指令 Topic")]
    public string commandTopic = "mine/digger/command";

    [Header("引用")]
    [Tooltip("掘进机控制器")]
    public DiggerController diggerController;

    [Header("调试（仅编辑器内使用）")]
    [Tooltip("启用键盘模拟输入（替代真实 MQTT）")]
    public bool enableSimulatedInput = true;

    // ─── 事件（可供 UI 模块订阅）──────────────────────────────────────
    [Header("事件")]
    public UnityEvent<string> onCommandReceived;   // 收到指令时触发，参数为原始 JSON
    public UnityEvent onEmergencyStop;             // 紧急停止事件

    // ─── 私有字段 ──────────────────────────────────────────────────────
    private DiggerCommand _lastCommand;

    // ─── Unity 生命周期 ────────────────────────────────────────────────
    private void Start()
    {
        if (diggerController == null)
        {
            diggerController = FindObjectOfType<DiggerController>();
            if (diggerController == null)
                Debug.LogError("[RemoteCommandReceiver] 未找到 DiggerController！");
        }

        // 真实项目：在此处连接 MQTT Broker 并订阅 commandTopic
        // ConnectMqtt();
    }

    private void Update()
    {
        if (enableSimulatedInput)
            SimulateLocalInput();
    }

    // ─── 公开接口 ──────────────────────────────────────────────────────

    /// <summary>
    /// 接收来自 MQTT 消息回调的原始 JSON 字符串
    /// 在 MQTT 插件的 OnMessageArrived 回调中调用此方法
    /// </summary>
    public void OnMqttMessageReceived(string topic, string payload)
    {
        if (topic != commandTopic) return;

        Debug.Log($"[RemoteCommandReceiver] 收到指令: {payload}");
        onCommandReceived?.Invoke(payload);

        try
        {
            RemoteCommandJson json = JsonUtility.FromJson<RemoteCommandJson>(payload);
            DiggerCommand cmd = ParseCommand(json);
            DispatchCommand(cmd);
        }
        catch (Exception ex)
        {
            Debug.LogError($"[RemoteCommandReceiver] 指令解析失败: {ex.Message}");
        }
    }

    // ─── 私有方法 ──────────────────────────────────────────────────────

    /// <summary>
    /// 将 JSON 数据结构转换为 DiggerCommand
    /// </summary>
    private DiggerCommand ParseCommand(RemoteCommandJson json)
    {
        DiggerCommand cmd = new DiggerCommand();
        cmd.value = json.value;

        switch (json.cmd.ToLower())
        {
            case "forward":       cmd.type = CommandType.MoveForward;   break;
            case "backward":      cmd.type = CommandType.MoveBackward;  break;
            case "turn_left":     cmd.type = CommandType.TurnLeft;      break;
            case "turn_right":    cmd.type = CommandType.TurnRight;     break;
            case "start_cutting": cmd.type = CommandType.StartCutting;  break;
            case "stop_cutting":  cmd.type = CommandType.StopCutting;   break;
            case "e_stop":        cmd.type = CommandType.EmergencyStop; break;
            default:              cmd.type = CommandType.None;          break;
        }
        return cmd;
    }

    /// <summary>
    /// 将解析后的指令派发到 DiggerController
    /// </summary>
    private void DispatchCommand(DiggerCommand cmd)
    {
        if (cmd.type == CommandType.EmergencyStop)
        {
            diggerController?.EmergencyStop();
            onEmergencyStop?.Invoke();
            return;
        }

        diggerController?.ReceiveCommand(cmd);
    }

    /// <summary>
    /// 键盘模拟输入（开发调试用，上线时关闭 enableSimulatedInput）
    ///
    /// 按键映射：
    ///   W / ↑   → 前进
    ///   S / ↓   → 后退
    ///   A / ←   → 左转
    ///   D / →   → 右转
    ///   Space   → 开始截割
    ///   C       → 停止截割
    ///   Escape  → 紧急停止
    /// </summary>
    private void SimulateLocalInput()
    {
        DiggerCommand cmd = new DiggerCommand { type = CommandType.None };

        if (Input.GetKey(KeyCode.W) || Input.GetKey(KeyCode.UpArrow))
            cmd.type = CommandType.MoveForward;
        else if (Input.GetKey(KeyCode.S) || Input.GetKey(KeyCode.DownArrow))
            cmd.type = CommandType.MoveBackward;
        else if (Input.GetKey(KeyCode.A) || Input.GetKey(KeyCode.LeftArrow))
            cmd.type = CommandType.TurnLeft;
        else if (Input.GetKey(KeyCode.D) || Input.GetKey(KeyCode.RightArrow))
            cmd.type = CommandType.TurnRight;
        else if (Input.GetKey(KeyCode.Space))
            cmd.type = CommandType.StartCutting;
        else if (Input.GetKeyDown(KeyCode.C))
            cmd.type = CommandType.StopCutting;

        if (Input.GetKeyDown(KeyCode.Escape))
        {
            diggerController?.EmergencyStop();
            onEmergencyStop?.Invoke();
            return;
        }

        // 避免每帧重复派发相同指令
        if (cmd.type != _lastCommand.type)
        {
            _lastCommand = cmd;
            DispatchCommand(cmd);
        }
    }
}

// ─── JSON 数据结构（与地面集控中心协议对齐）────────────────────────────────

/// <summary>
/// 远程控制指令 JSON 格式示例：
/// {
///   "cmd": "forward",
///   "value": 1.0,
///   "timestamp": 1716000000,
///   "operator_id": "OP_001"
/// }
/// </summary>
[System.Serializable]
public class RemoteCommandJson
{
    public string cmd;
    public float value;
    public long timestamp;
    public string operator_id;
}
