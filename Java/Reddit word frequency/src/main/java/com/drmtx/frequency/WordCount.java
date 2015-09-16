package com.drmtx.frequency;

/**
 * Created by antivo on 8/27/15.
 */

/**
 * Represents one word count in frequency analysis
 */
public class WordCount {
    private String word;
    private Long count;

    public WordCount(String word, Long count) {
        this.count = count;
        this.word = word;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "Point{" +
                "count=" + count +
                ", word='" + word + '\'' +
                '}';
    }
}
