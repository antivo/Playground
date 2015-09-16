package com.drmtx.reddit;

import org.json.simple.parser.ParseException;

import java.util.List;

/**
 * Created by antivo on 8/27/15.
 */
public interface RedditResponseExtractor {
    /**
     * Extract commpents from the response of the Reddit comment api json
     * @param response json that Reddit comment api returned
     * @return list of comments extracted from the json
     * @throws ParseException if there was a problem with the json format
     */
    List<String> extractCommentsFromResponse(String response) throws ParseException;
}
