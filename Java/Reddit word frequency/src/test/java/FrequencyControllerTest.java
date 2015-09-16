import com.drmtx.app.Application;
import com.drmtx.controller.FrequencyController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by antivo on 8/27/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class FrequencyControllerTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void commentsToFrequency() {
        String url = "https://www.reddit.com/r/java/comments/32pj67/java_reference_in_gta_v_beautiful/.json";

        ResponseEntity<Long> re = applicationContext.getBean(FrequencyController.class).newFrequency(url);
    }
}
