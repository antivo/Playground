import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by antivo on 6/14/15.
 */
public final class Node {
    private static final int STANDARD_OFFSET = 4;

    private Node parent;
    private final Map<String, Node> elements = new HashMap<String, Node>();
    private int counter;

    private Node() {
        this.parent = null;
        this.counter = 0;
    }

    public Node(List<StackTrace> stackTraces) {
        for(StackTrace stackTrace : stackTraces) {
            Node lastVisited = this;
            for(StackTraceElement stackTraceElement : stackTrace.stackTraceElements) {
                String ss = StackTraceElementUtils.toString(stackTraceElement);
                lastVisited = lastVisited.addChild(ss);
            }
            lastVisited.incCounter();
        }
    }

    private void incCounter() {
        Node node = this;
        while(node != null) {
            ++node.counter;
            node = node.parent;
        }
    }

    private Node addChild(String line) {
        Node node = null;
        if(elements.keySet().contains(line)) {
            node = elements.get(line);
        } else {
            node = new Node();
            node.parent = this;
            elements.put(line, node);
        }
        return node;
    }

    private void prettyPrint(int offset) {
        for(String ss : elements.keySet()) {
            System.out.println(StringUtils.createIntendation(offset) + this.counter + " " + ss);
            elements.get(ss).prettyPrint(offset + STANDARD_OFFSET);
        }
    }

    public void print() {
        this.prettyPrint( 0 );
    }
}
