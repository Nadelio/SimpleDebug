import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleDebug {

    private int DEBUG;

    /**
     * Create a debugger instance with a specified debug level.
     * @param debugLevel
     */
    public SimpleDebug(int debugLevel) { this.DEBUG = debugLevel; }
    public void setDebugLevel(int debugLevel) { this.DEBUG = debugLevel; }
    public void disable() { this.DEBUG = 0; }

    @Deprecated
    private static void test() throws Exception {
        SimpleDebug logger = new SimpleDebug(7);
        logger.debug("This is a debug message.", 1);
        logger.debug("This is a debug message with information: ", Integer.valueOf(42), "and some text.", 2);
        logger.warn("This is a warning message.", 3);
        logger.notice("This is a notice message.", 4);
        logger.error("This is an error message.", 5);
        try {
            logger.fatal("This is a fatal error message.", 6);
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
        File logFile = new File("test.log");
        logger.log("This is a log message.", logFile, 2);
        logger.log("This is another log message.", logFile, 3);
        logger.debug("This is a debug message with file logging.", logFile, 2);
        logger.warn(logger.info(2, "This is a warning message with info", 42), 2);
    }

    /**
     * Send a debug message to the console.
     * @param message
     * @param level
     */
    public void debug(String message, int level) {
        if(this.DEBUG < level) return;
        System.out.println(SimpleColor.DEBUG("[ DEBUG: " + message) + SimpleColor.DEBUG(" ]"));
    }

    /**
     * Send a debug message to the console and log it to a file.
     * @param message
     * @param file
     * @param level
     * @throws Exception
     */
    public void debug(String message, File file, int level) throws Exception {
        if(this.DEBUG < level) return;
        debug(message, level);
        log(message, file, level);
    }

    /**
     * Send a debug message to the console with additional information.
     * @param message
     * @param level
     * @param args
     */
    public void debug(String message, int level, Object... args) {
        if(this.DEBUG < level) return;
        if (args.length == 0) {
            System.out.println(SimpleColor.DEBUG("[ DEBUG: " + message) + SimpleColor.DEBUG(" ]"));
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < args.length; i++) {
            sb.append(SimpleColor.INFO(args[i].toString()));
            if (i < args.length - 1) {
                sb.append(SimpleColor.DEBUG(", "));
            }
        }
        System.out.println(SimpleColor.DEBUG("[ DEBUG: " + message + "|") + sb.toString() + SimpleColor.DEBUG(" ]"));
    }

    /**
     * Format a set of arguments into a string with info color.
     * @param level
     * @param args
     * @return
     */
    public String info(int level, Object... args) {
        if(this.DEBUG < level || args.length == 0) return "";

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < args.length; i++) {
            sb.append(SimpleColor.INFO(args[i].toString()));
            if (i < args.length - 1) {
                sb.append(SimpleColor.DEBUG(", "));
            }
        }
        return sb.toString();
    }

    /**
     * Send a warning message to the console.
     * @param level
     * @param args
     * @return
     */
    public void warn(String message, int level) {
        if(this.DEBUG < level) return;
        System.out.println(SimpleColor.WARNING("[ WARN: " + message) + SimpleColor.WARNING(" ]"));
    }

    /**
     * Send a notice message to the console.
     * @param level
     * @param args
     * @return
     */
    public void notice(String message, int level) {
        if(this.DEBUG < level) return;
        System.out.println(SimpleColor.NOTICE("[ NOTICE: " + message) + SimpleColor.NOTICE(" ]"));
    }

    /**
     * Send an error message to the console.
     * @param level
     * @param args
     * @return
     */
    public void error(String message, int level) {
        if(this.DEBUG < level) return;
        System.out.println(SimpleColor.ERROR("[ ERROR: ") + SimpleColor.INFO(message) + SimpleColor.ERROR(" ]"));
    }

    /**
     * Send a fatal error message to the console and throw an exception.
     * @param level
     * @param args
     * @throws Exception
     */
    public void fatal(String message, int level) throws Exception {
        if(this.DEBUG < level) return;
        throw new Exception(SimpleColor.ERROR("[ FATAL: ") + SimpleColor.INFO(message) + SimpleColor.ERROR(" ]"));
    }

    /**
     * Log a message to a file with a timestamp.
     * @param message
     * @param file
     * @param level
     * @throws Exception
     */
    public void log(String message, File file, int level) throws Exception {
        if(this.DEBUG < level) return;

        LocalDateTime now = LocalDateTime.now(); // Get the current timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Format the timestamp
        String formattedTimestamp = now.format(formatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write("[Log: " + formattedTimestamp + " | " + message + "]");
            writer.newLine();
        } catch (Exception e) {
            error("Failed to write to log file: " + e.getMessage(), level);
        }
    }
}