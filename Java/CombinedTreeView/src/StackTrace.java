import java.util.List;

/**
 * Created by antivo on 6/14/15.
 */
public final class StackTrace {
    public final String threadName;
    public final List<StackTraceElement> stackTraceElements;

    public StackTrace(String threadName, List<StackTraceElement> stackTraceElements) {
        this.threadName = threadName;
        this.stackTraceElements = stackTraceElements;
    }
}