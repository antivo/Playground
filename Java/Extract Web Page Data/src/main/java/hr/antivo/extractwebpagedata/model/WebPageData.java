package hr.antivo.extractwebpagedata.model;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by antivo on 3/12/15.
 */
public class WebPageData {
    private String mainArticleTitle;
    private String mainArticleContent;
    private String author;
    private String publishingDateOfTheArticle;

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

    public static class WebPageDataBuilder {
        private WebPageData webPageData;

        public WebPageDataBuilder() {
            webPageData = new WebPageData();
        }

        public WebPageDataBuilder withAuthor(String author) {
            this.webPageData.setAuthor(author);
            return this;
        }

        public WebPageDataBuilder withMainArticleContent(String mainArticleContent) {
            this.webPageData.setMainArticleContent(mainArticleContent);
            return this;
        }

        public WebPageDataBuilder withMainArticleTitle(String mainArticleTitle) {
            this.webPageData.setMainArticleTitle(mainArticleTitle);
            return this;
        }

        public WebPageDataBuilder withPublishingDateOfTheArticle(String publishingDateOfTheArticle) {
            this.webPageData.setPublishingDateOfTheArticle(publishingDateOfTheArticle);
            return this;
        }

        public WebPageData build() {
            return webPageData;
        }
    }
}