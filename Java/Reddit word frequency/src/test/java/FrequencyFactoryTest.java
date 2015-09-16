import com.drmtx.app.Application;
import com.drmtx.frequency.FrequencyFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antivo on 8/27/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class FrequencyFactoryTest {
    @Test
    public void commentsToFrequency() {

        List<String> l = new ArrayList<>();
        l.add("I n3ver say n3ver");
        l.add("i no no no was never here, !!");
        l.add("i was never here");

        Assert.assertEquals((long) FrequencyFactory.commentsToFrequency(l).get("i"), 3);

    }
}
