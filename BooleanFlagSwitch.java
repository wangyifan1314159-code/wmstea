import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Java boolean function flag switch examples.
 *
 * Demonstrates three common boolean flag/switch patterns:
 * 1. Simple boolean field flag
 * 2. Thread-safe AtomicBoolean switch
 * 3. Boolean method-parameter feature switch
 */
public class BooleanFlagSwitch {

    // -----------------------------------------------------------------------
    // 1. Simple boolean field flag (single-threaded environment)
    // -----------------------------------------------------------------------
    private boolean enabled = false;

    /** Turns the switch on. */
    public void enable() {
        this.enabled = true;
    }

    /** Turns the switch off. */
    public void disable() {
        this.enabled = false;
    }

    /** Toggles the switch state. */
    public void toggle() {
        this.enabled = !this.enabled;
    }

    /** Returns the current switch state. */
    public boolean isEnabled() {
        return this.enabled;
    }

    /** Executes different logic based on the switch state. */
    public void doWork() {
        if (enabled) {
            System.out.println("[Simple flag] Switch is ON - executing business logic");
        } else {
            System.out.println("[Simple flag] Switch is OFF - skipping business logic");
        }
    }

    // -----------------------------------------------------------------------
    // 2. Thread-safe AtomicBoolean switch (multi-threaded environment)
    // -----------------------------------------------------------------------
    private final AtomicBoolean atomicFlag = new AtomicBoolean(false);

    /** Atomically sets the switch to true; returns the previous value. */
    public boolean atomicEnable() {
        return atomicFlag.getAndSet(true);
    }

    /** Atomically sets the switch to false; returns the previous value. */
    public boolean atomicDisable() {
        return atomicFlag.getAndSet(false);
    }

    /** Atomically sets the switch to {@code update} only if the current value equals {@code expect}. */
    public boolean atomicCompareAndSet(boolean expect, boolean update) {
        return atomicFlag.compareAndSet(expect, update);
    }

    /** Returns the current atomic switch state. */
    public boolean isAtomicEnabled() {
        return atomicFlag.get();
    }

    // -----------------------------------------------------------------------
    // 3. Boolean method-parameter feature switch (method-level flag)
    // -----------------------------------------------------------------------

    /**
     * Sends a message, with optional debug logging controlled by a boolean parameter.
     *
     * @param message  the message to send
     * @param debugLog whether to print debug log output
     */
    public void sendMessage(String message, boolean debugLog) {
        if (debugLog) {
            System.out.println("[DEBUG] Preparing to send message: " + message);
        }
        // simulate sending the message
        System.out.println("[SEND] " + message);
        if (debugLog) {
            System.out.println("[DEBUG] Message sent successfully");
        }
    }

    /**
     * Queries data, with cache usage controlled by a boolean parameter.
     *
     * @param key      the lookup key
     * @param useCache whether to use the cache
     * @return         the query result
     */
    public String query(String key, boolean useCache) {
        if (useCache) {
            System.out.println("[Cache] Fetching from cache: " + key);
            return "cached_" + key;
        }
        System.out.println("[Database] Querying from database: " + key);
        return "db_" + key;
    }

    // -----------------------------------------------------------------------
    // main: demonstrates all patterns
    // -----------------------------------------------------------------------
    public static void main(String[] args) {
        BooleanFlagSwitch demo = new BooleanFlagSwitch();

        System.out.println("=== 1. Simple boolean flag ===");
        demo.doWork();           // switch is off
        demo.enable();
        demo.doWork();           // switch is on
        demo.toggle();
        demo.doWork();           // toggled (now off)

        System.out.println("\n=== 2. AtomicBoolean thread-safe switch ===");
        System.out.println("Initial state: " + demo.isAtomicEnabled());
        boolean old = demo.atomicEnable();
        System.out.println("Previous value before atomicEnable(): " + old + ", current value: " + demo.isAtomicEnabled());
        boolean swapped = demo.atomicCompareAndSet(true, false);
        System.out.println("CAS(true->false) succeeded: " + swapped + ", current value: " + demo.isAtomicEnabled());

        System.out.println("\n=== 3. Boolean method-parameter switch ===");
        demo.sendMessage("Hello, World!", true);   // debug logging on
        System.out.println();
        demo.sendMessage("Hello, World!", false);  // debug logging off
        System.out.println();
        System.out.println("useCache=true  result: " + demo.query("user:1", true));
        System.out.println("useCache=false result: " + demo.query("user:1", false));
    }
}
