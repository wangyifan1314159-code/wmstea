# Unity 矿井数字孪生 — 项目目录结构说明

本目录为矿井掘进智能化安全管控系统的 **Unity 数字孪生可视化模块**，使用 **C# 脚本** 驱动所有三维模型。

---

## 目录结构

```
unity/
├── README_Unity目录结构.md          ← 本文件
│
├── Scenes/                          ← 场景文件
│   ├── MainScene.unity              ← 主场景（数字孪生总览）
│   ├── TunnelScene.unity            ← 掘进巷道场景
│   └── ControlCenterScene.unity     ← 地面集控中心场景
│
├── Models/                          ← 三维模型资源（FBX/OBJ）
│   ├── DiggerMachine.fbx            ← 掘进机模型
│   ├── Tunnel.fbx                   ← 巷道模型
│   ├── Worker.fbx                   ← 人员模型
│   └── SupportRig.fbx               ← 液压支架模型
│
├── Animations/                      ← 动画控制器与动画片段
│   ├── DiggerAnimator.controller    ← 掘进机动画状态机
│   ├── WorkerAnimator.controller    ← 人员动画状态机
│   ├── Digging.anim                 ← 截割动画
│   ├── Moving.anim                  ← 行走动画
│   ├── Idle.anim                    ← 待机动画
│   └── Emergency.anim               ← 紧急停止动画
│
├── Scripts/                         ← C# 驱动脚本（核心）
│   ├── DiggerController.cs          ← 掘进机主控制器（模型运动+状态）
│   ├── DiggerAnimator.cs            ← 动画状态机驱动
│   ├── RemoteCommandReceiver.cs     ← 远程指令接收与解析（5G/MQTT）
│   ├── WorkerTracker.cs             ← 人员模型实时定位驱动（UWB数据）
│   ├── SensorDataVisualizer.cs      ← 传感器数据可视化（瓦斯/温度等）
│   └── CameraController.cs          ← 多视角摄像机控制
│
├── Prefabs/                         ← 预制体
│   ├── DiggerMachine.prefab         ← 掘进机预制体
│   ├── WorkerPrefab.prefab          ← 人员预制体
│   └── WarningUI.prefab             ← 报警UI预制体
│
├── Materials/                       ← 材质与Shader
│   ├── DiggerMetal.mat              ← 掘进机金属材质
│   ├── TunnelRock.mat               ← 岩石材质
│   └── WarningHighlight.mat         ← 报警高亮材质（红色闪烁）
│
└── Editor/                          ← Unity 编辑器扩展脚本
    └── DiggerInspector.cs           ← 自定义Inspector面板
```

---

## 驱动流程说明

```
远程控制台 / MQTT消息
        │
        ▼
RemoteCommandReceiver.cs   ← 接收5G/MQTT指令，解析为指令结构体
        │
        ▼
DiggerController.cs        ← 解析指令→驱动Rigidbody位移/旋转/停止
        │
        ├──▶ DiggerAnimator.cs     ← 同步更新Animator参数（截割/行走/待机）
        │
        ├──▶ SensorDataVisualizer  ← 更新传感器数值显示（瓦斯浓度/顶板压力）
        │
        └──▶ WorkerTracker.cs      ← UWB坐标→人员模型实时位置更新
```

---

## 开发环境

| 项目 | 版本 |
|------|------|
| Unity | 2022.3 LTS |
| 脚本语言 | **C#** |
| 渲染管线 | URP (Universal Render Pipeline) |
| 通信协议 | MQTT (M2MqttUnity 插件) |
| 物理引擎 | PhysX (Unity 内置) |

---

## 快速上手

1. 用 Unity Hub 打开本项目（Unity 2022.3+）
2. 打开 `Scenes/MainScene.unity`
3. 在 `Hierarchy` 找到 `DiggerMachine` 对象，查看挂载的 `DiggerController` 脚本
4. 在 Inspector 中配置 MQTT 服务器 IP 与 Topic
5. 点击 Play，即可看到模型响应远程指令
