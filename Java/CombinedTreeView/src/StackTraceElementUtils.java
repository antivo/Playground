/**
 * Created by antivo on 6/14/15.
 */
public class StackTraceElementUtils {
    public static String toString(StackTraceElement stackTraceElement) {
        StringBuilder sb = new StringBuilder();
        sb.append(stackTraceElement.className).append('.').append(stackTraceElement.method).append(':').append(stackTraceElement.lineNumber);
        return sb.toString();
    }
}
