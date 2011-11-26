package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import models.Article;
import models.ArticleComment;
import models.Member;
import models.activity.Activity;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;

public class Articles extends Controller {

    public static void index() {
        list(1,10);
    }

    public static void list(int page, int lenght) {
        List<Article> articles = Article.recents(page, 10);
        render(articles);
    }

    public static void create() {
        Member author = Member.findByLogin(Security.connected());
        Article article = new Article(author);
        render("Article/edit.html", article);
    }

    public static void edit(final Long articleId) {
        Article article = Article.findById(articleId);
        render(article);
    }

    public static void show(final Long articleId, boolean noCount) {
        Article article = Article.findById(articleId);
        // Don't count look when coming from internal redirect
        if (!noCount) {
            article.lookedBy(Member.findByLogin(Security.connected()));
        }
        // List<Activity> activities = Activity.recentsByArticle(article, 1, 10);
        render(article /*, activities */);
    }
    
    public static void postComment(
            Long articleId,
            @Required String content) {
        Article article = Article.findById(articleId);
        if (Validation.hasErrors()) {
            render("Articles/show.html", article);
        }

        Member author = Member.findByLogin(Security.connected());
        article.addComment(new ArticleComment(author, article, content));
        article.save();
        flash.success("Merci pour votre commentaire %s", author);
        show(articleId, true);
    }

    public static void save(@Valid Article article) {
        Logger.info("Tentative d'enregistrement d'article " + article);

        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            render("Articles/edit.html", article);
        }

        article.save();
        flash.success("Article " + article + " enregistré");
        Logger.info("Article " + article + " enregistré");
        show(article.id, true);
    }
}