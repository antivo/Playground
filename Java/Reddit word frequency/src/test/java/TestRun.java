import com.drmtx.app.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Created by antivo on 8/27/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class TestRun {
    private static final String API1 = "http://localhost:8080/frequency/new";
    private static final String POST_ARGUMENT = "https://www.reddit.com/r/java/comments/32pj67/java_reference_in_gta_v_beautiful/.json";

    @Test
    public void commentsToFrequency() {
        RestTemplate restTemplate = new RestTemplate();
        Long id = restTemplate.postForEntity(API1, POST_ARGUMENT, Long.class).getBody();

        String API2 = "http://localhost:8080/frequency/" + id +"/count?number_of_results=5";
        String ss = restTemplate.getForEntity(API2, String.class).getBody();


    }
}
