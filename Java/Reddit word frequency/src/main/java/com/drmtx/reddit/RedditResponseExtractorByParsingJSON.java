package com.drmtx.reddit;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by antivo on 8/27/15.
 */
@Component
public class RedditResponseExtractorByParsingJSON implements RedditResponseExtractor {
    private static final Logger logger = Logger.getLogger(RedditResponseExtractorByParsingJSON.class);

    public static final JSONParser parser = new JSONParser();

    @Override
    public List<String> extractCommentsFromResponse(String response) throws ParseException {
        JSONArray array = (JSONArray) parser.parse(response);
        logger.debug("JSONArray from response: " + array);
        JSONObject json = (JSONObject) (array.get(1));
        logger.debug("JSONObject where comments are stored: " + json);
        List<String> comments = new ArrayList<>();
        parseRecursive(comments, json);
        return comments;
    }

    static private void parseChildren(List<String> comments, JSONArray children) {
        Iterator i$ = children.iterator();
        while(i$.hasNext()) {
            Object anArrayEntry = i$.next();
            JSONObject entry = (JSONObject)anArrayEntry;
            JSONObject data = (JSONObject) entry.get("data");
            if(null != data) {
                String body = (String) data.get("body");
                logger.debug("body found: " + body);
                if(body != null) {
                    comments.add(body.toString());
                }
                Object replies = data.get("replies");
                if(replies != null && !replies.equals("")) {
                    parseRecursive(comments, (JSONObject) replies);
                }
            }
        }
    }

    static private void parseRecursive(List<String> comments, JSONObject object) {
        assert comments != null : "List of comments must be instantiated.";
        assert object != null : "JSON Object must be instantiated.";
        JSONArray array = (JSONArray) ((JSONObject) object.get("data")).get("children");
        parseChildren(comments, array);
    }
}
