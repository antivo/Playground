/**
 * Created by antivo on 6/14/15.
 */
public final class StackTraceElement {
    public final String className;
    public final String method;
    public final int lineNumber;

    public StackTraceElement(String className, String method, int lineNumber) {
        this.className = className;
        this.method = method;
        this.lineNumber = lineNumber;
    }

}