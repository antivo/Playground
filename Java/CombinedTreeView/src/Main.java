import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<StackTrace> stackTraces = new ArrayList<StackTrace>();
        List<StackTraceElement> stackTraceElements = new ArrayList<StackTraceElement>();
        stackTraceElements.add(new StackTraceElement("Thread", "run", 662));
        stackTraceElements.add(new StackTraceElement("SampleWriter", "process", 189));
        stackTraceElements.add(new StackTraceElement("SampleWriter", "write", 175));
        stackTraceElements.add(new StackTraceElement("SampleWriter", "validate", 145));
        stackTraces.add(new StackTrace("thread1", stackTraceElements));

        stackTraceElements = new ArrayList<StackTraceElement>();
        stackTraceElements.add(new StackTraceElement("Thread", "run", 662));
        stackTraceElements.add(new StackTraceElement("SampleWriter", "process", 189));
        stackTraceElements.add(new StackTraceElement("SampleWriter", "write", 175));
        stackTraceElements.add(new StackTraceElement("SampleWriter", "validateSingle", 153));
        stackTraces.add(new StackTrace("thread2", stackTraceElements));

        stackTraceElements = new ArrayList<StackTraceElement>();
        stackTraceElements.add(new StackTraceElement("Thread", "run", 662));
        stackTraceElements.add(new StackTraceElement("SampleWriter", "process", 189));
        stackTraceElements.add(new StackTraceElement("SampleWriter", "write", 175));
        stackTraceElements.add(new StackTraceElement("SampleWriter", "validateSingle", 153));
        stackTraces.add(new StackTrace("thread3", stackTraceElements));

        stackTraceElements = new ArrayList<StackTraceElement>();
        stackTraceElements.add(new StackTraceElement("Thread", "run", 662));
        stackTraceElements.add(new StackTraceElement("SampleWriter", "process", 195));
        stackTraceElements.add(new StackTraceElement("SampleWriter", "validate", 145));
        stackTraces.add(new StackTrace("thread4", stackTraceElements));

        stackTraceElements = new ArrayList<StackTraceElement>();
        stackTraceElements.add(new StackTraceElement("Servlet", "main", 334));
        stackTraceElements.add(new StackTraceElement("Logger", "log", 783));
        stackTraces.add(new StackTrace("thread5", stackTraceElements));

        Node node = new Node(stackTraces);

        node.print();


    }
}
