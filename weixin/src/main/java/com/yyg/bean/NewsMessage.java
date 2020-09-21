package com.yyg.bean;

import java.util.List;

public class NewsMessage extends BaseResponseMessage {

    private int ArticleCount;

    private List<Article> Articles;

    public List<Article> getArticles() {
        return Articles;
    }

    public void setArticles(List<Article> articles) {
        Articles = articles;
    }

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }
}
