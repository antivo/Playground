package com.drmtx.reddit;

import java.util.Optional;

/**
 * Created by antivo on 8/27/15.
 */
public interface RedditClient {
    /**
     * Return response from the Reddit comment api
     * @param redditCommentURL url using Reddit comment api pointing to topic
     * @return response from the Reddit comment api
     * @throws IllegalArgumentException if url can not be used
     */
    Optional<String> getCommentsResponseFor(String redditCommentURL) throws IllegalArgumentException;
}
