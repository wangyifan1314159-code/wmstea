import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Java boolean 函数标志开关示例
 *
 * 演示三种常见的布尔标志开关用法：
 * 1. 简单 boolean 标志变量
 * 2. 线程安全的 AtomicBoolean 开关
 * 3. 基于 boolean 函数参数的功能开关
 */
public class BooleanFlagSwitch {

    // -----------------------------------------------------------------------
    // 1. 简单 boolean 标志变量（单线程环境）
    // -----------------------------------------------------------------------
    private boolean enabled = false;

    /** 打开开关 */
    public void enable() {
        this.enabled = true;
    }

    /** 关闭开关 */
    public void disable() {
        this.enabled = false;
    }

    /** 切换开关状态 */
    public void toggle() {
        this.enabled = !this.enabled;
    }

    /** 查询开关状态 */
    public boolean isEnabled() {
        return this.enabled;
    }

    /** 根据开关状态执行不同逻辑 */
    public void doWork() {
        if (enabled) {
            System.out.println("[简单标志] 开关已开启，执行业务逻辑");
        } else {
            System.out.println("[简单标志] 开关已关闭，跳过业务逻辑");
        }
    }

    // -----------------------------------------------------------------------
    // 2. 线程安全的 AtomicBoolean 开关（多线程环境）
    // -----------------------------------------------------------------------
    private final AtomicBoolean atomicFlag = new AtomicBoolean(false);

    /** 原子性地打开开关，返回旧值 */
    public boolean atomicEnable() {
        return atomicFlag.getAndSet(true);
    }

    /** 原子性地关闭开关，返回旧值 */
    public boolean atomicDisable() {
        return atomicFlag.getAndSet(false);
    }

    /** 原子性地比较并设置开关：仅当当前值为 expect 时才设置为 update */
    public boolean atomicCompareAndSet(boolean expect, boolean update) {
        return atomicFlag.compareAndSet(expect, update);
    }

    /** 查询原子开关状态 */
    public boolean isAtomicEnabled() {
        return atomicFlag.get();
    }

    // -----------------------------------------------------------------------
    // 3. 函数参数作为功能开关（方法级标志）
    // -----------------------------------------------------------------------

    /**
     * 发送消息，通过 boolean 参数控制是否启用调试日志
     *
     * @param message  要发送的消息
     * @param debugLog 是否打印调试日志
     */
    public void sendMessage(String message, boolean debugLog) {
        if (debugLog) {
            System.out.println("[DEBUG] 准备发送消息: " + message);
        }
        // 模拟消息发送
        System.out.println("[发送] " + message);
        if (debugLog) {
            System.out.println("[DEBUG] 消息发送完成");
        }
    }

    /**
     * 查询数据，通过 boolean 参数控制是否启用缓存
     *
     * @param key       查询键
     * @param useCache  是否使用缓存
     * @return          查询结果
     */
    public String query(String key, boolean useCache) {
        if (useCache) {
            System.out.println("[缓存] 从缓存中获取: " + key);
            return "cached_" + key;
        }
        System.out.println("[数据库] 从数据库查询: " + key);
        return "db_" + key;
    }

    // -----------------------------------------------------------------------
    // main：演示所有用法
    // -----------------------------------------------------------------------
    public static void main(String[] args) {
        BooleanFlagSwitch demo = new BooleanFlagSwitch();

        System.out.println("=== 1. 简单 boolean 标志 ===");
        demo.doWork();           // 关闭状态
        demo.enable();
        demo.doWork();           // 开启状态
        demo.toggle();
        demo.doWork();           // 切换后（关闭）

        System.out.println("\n=== 2. AtomicBoolean 线程安全开关 ===");
        System.out.println("初始状态: " + demo.isAtomicEnabled());
        boolean old = demo.atomicEnable();
        System.out.println("atomicEnable() 前的旧值: " + old + "，当前值: " + demo.isAtomicEnabled());
        boolean swapped = demo.atomicCompareAndSet(true, false);
        System.out.println("CAS(true->false) 成功: " + swapped + "，当前值: " + demo.isAtomicEnabled());

        System.out.println("\n=== 3. 函数参数布尔开关 ===");
        demo.sendMessage("Hello, World!", true);   // 开启调试日志
        System.out.println();
        demo.sendMessage("Hello, World!", false);  // 关闭调试日志
        System.out.println();
        System.out.println("useCache=true  结果: " + demo.query("user:1", true));
        System.out.println("useCache=false 结果: " + demo.query("user:1", false));
    }
}
