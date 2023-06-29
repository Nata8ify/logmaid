package xyz.n8ify.logmaid.utils;

import xyz.n8ify.logmaid.enums.LogLevel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogContentUtil {

    private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    public static String generate(LogLevel logLevel, String message) {
        return String.format("[%s] - [%s] : %s", format.format(new Date()), logLevel, message);
    }

}
