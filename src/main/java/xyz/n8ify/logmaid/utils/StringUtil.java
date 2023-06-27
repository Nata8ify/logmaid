package xyz.n8ify.logmaid.utils;

public class StringUtil {

    public static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    public static String escape(String value) {
        final char escapeChar = '_';
        return value
                .replace('/', escapeChar).replace('\\', escapeChar)
                .replace('\n', escapeChar).replace('+', escapeChar)
                .replace('(', escapeChar).replace(')', escapeChar)
                .replace('*', escapeChar).replace('%', escapeChar)
                .replace('#', escapeChar).replace('&', escapeChar)
                .replace('=', escapeChar).replace('^', escapeChar)
                .replace('@', escapeChar).replace('{', escapeChar)
                .replace('}', escapeChar).replace('[', escapeChar)
                .replace(']', escapeChar).replace('?', escapeChar)
                .replace(';', escapeChar).replace(':', escapeChar)
                .replace(',', escapeChar).replace('.', escapeChar)
                .replace('-', escapeChar);
    }

    public static String generateHeaderString(String header) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n------------------------------------------------------------------------------------------");
        builder.append(String.format("\n---- %s \n", header));
        return builder.toString();
    }

}
