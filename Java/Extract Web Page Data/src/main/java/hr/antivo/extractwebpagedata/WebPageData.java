package hr.antivo.extractwebpagedata;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by antivo on 3/12/15.
 */
public class WebPageData {
    String mainArticleTitle;
    String mainArticleContent;
    String author;
    String publishingDateOfTheArticle;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMainArticleContent() {
        return mainArticleContent;
    }

    public void setMainArticleContent(String mainArticleContent) {
        this.mainArticleContent = mainArticleContent;
    }

    public String getMainArticleTitle() {
        return mainArticleTitle;
    }

    public void setMainArticleTitle(String mainArticleTitle) {
        this.mainArticleTitle = mainArticleTitle;
    }

    public String getPublishingDateOfTheArticle() {
        return publishingDateOfTheArticle;
    }

    public void setPublishingDateOfTheArticle(String publishingDateOfTheArticle) {
        this.publishingDateOfTheArticle = publishingDateOfTheArticle;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return "WebPageData{" +
                "author='" + author + '\'' +
                ", mainArticleTitle='" + mainArticleTitle + '\'' +
                ", mainArticleContent='" + mainArticleContent + '\'' +
                ", publishingDateOfTheArticle=" + publishingDateOfTheArticle +
                '}';
    }
}
