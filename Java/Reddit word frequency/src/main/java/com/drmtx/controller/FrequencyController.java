package com.drmtx.controller;

import com.drmtx.frequency.Frequency;
import com.drmtx.frequency.FrequencyFactory;
import com.drmtx.frequency.WordCount;
import com.drmtx.service.FrequencyService;
import com.drmtx.util.SelectUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

/**
 * Created by antivo on 8/27/15.
 */
@RestController
@RequestMapping("/frequency")
public class FrequencyController {
    private static final Logger logger = Logger.getLogger(FrequencyController.class);

    @Autowired
    private FrequencyFactory frequencyFactory;

    @Autowired
    private FrequencyService frequencyService;

    /**
     * /frequency/new
     * API Input:
     * This API endpoint should take a Reddit comment URL as a parameter e.g.:
     * https://www.reddit.com/r/java/comments/32pj67/java_reference_in_gta_v_beautiful/.json
     * API Response:
     * For each request executed against the API endpoint returns an unique identifier,
     * which is the input for the second API endpoint.
     * @param urlCoded url on the Reddit comment api to be analysed
     * @return id of the stored result
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Long> newFrequency(@RequestBody String urlCoded) {
        logger.info("received codec url: " + urlCoded);
        try {
            String url = java.net.URLDecoder.decode(urlCoded, "UTF-8");
            logger.info("url decoded : " + url);
            Optional<Frequency> frequency = frequencyFactory.makeFrequency(url);
            if (frequency.isPresent()) {
                logger.info("created frequency : " + frequency.get());
                Frequency f = frequencyService.save(frequency.get());
                logger.info("frequency id to return: " + f.getId());
                return ResponseEntity.ok(f.getId());
            }
        } catch(UnsupportedEncodingException e) {
            logger.error("Received url can not be decoded");
        }
        logger.warn("Calculation of frequency was not successful. WIll return INTERNAL_SERVER_ERROR.");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * /frequency/{id}?count=number_of_results
     * API Input: takes an id as input as well as a parameter count
     * API Output:Returns the the top n elements (bounded by the count parameter) from the word frequency analysis
     * @param id id of stored result
     * @param number_of_results paramar count used as limit of retrieved results
     * @return
     */
    @RequestMapping(value = "/{id}/count", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<WordCount>> count(@PathVariable Long id, @RequestParam Long number_of_results) {
        logger.info("Count is requested for id: " + id + ". With number_of_results = " + number_of_results);
        Frequency frequency = frequencyService.findOne(id);
        logger.info("Frequency under the id found: " + frequency);
        if(null != id) {
            List<WordCount> list = SelectUtil.selectFirst(number_of_results, frequency.getFrequency());
            logger.info("List of points to return: " + list);
            return ResponseEntity.ok(list);
        } else {
            return new ResponseEntity<List<WordCount>>(HttpStatus.NOT_FOUND);
        }
    }
}
