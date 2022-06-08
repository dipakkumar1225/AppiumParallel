package utilities.miscellanious;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;

final public class Log {
    private static final Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private Log() {
//        System.setProperty("java.util.logging.config.file",ConfigReader.getInstance("config.properties").getProperties().get("LOG_FILEPATH"));
    };

    public static void logError(Class clazz, String msg) {
        log(Level.ERROR, clazz, msg, null);
    }

    public static void logWarn(Class clazz, String msg) {
        log(Level.WARN, clazz, msg, null);
    }

    public static void logFatal(Class clazz, String msg) {
        log(Level.FATAL, clazz, msg, null);
    }

    public static void logInfo(Class clazz, String msg) {
        log(Level.INFO, clazz, msg, null);
    }

    public static void logDebug(Class clazz, String msg) {
        log(Level.DEBUG, clazz, msg, null);
    }


    public static void logError(Class clazz, String msg, Throwable throwable) {
        log(Level.ERROR, clazz, msg, throwable);
    }


    public static void logWarn(Class clazz, String msg, Throwable throwable) {
        log(Level.WARN, clazz, msg, throwable);
    }

    public static void logFatal(Class clazz, String msg, Throwable throwable) {
        log(Level.FATAL, clazz, msg, throwable);
    }

    public static void logInfo(Class clazz, String msg, Throwable throwable) {
        log(Level.INFO, clazz, msg, throwable);
    }

    public static void logDebug(Class clazz, String msg, Throwable throwable) {
        log(Level.DEBUG, clazz, msg, throwable);
    }

    private static void log(Level level, Class clazz, String msg, Throwable throwable) {
        String message = String.format("[%s] : %s", clazz, msg);
        switch (level) {
            case INFO:
                LOGGER.info(message, throwable);
                break;
            case WARN:
                LOGGER.warn(message, throwable);
                break;
            case ERROR:
                LOGGER.error(message, throwable);
                break;
            case FATAL:
                LOGGER.fatal(message, throwable);
                break;
            default:
            case DEBUG:
                LOGGER.debug(message, throwable);
        }
    }

}