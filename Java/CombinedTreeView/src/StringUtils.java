/**
 * Created by antivo on 6/14/15.
 */
public class StringUtils {
    public static String createIntendation(int n) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; ++i) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
