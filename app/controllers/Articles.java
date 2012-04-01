package controllers;

import play.*;

import java.util.*;
import models.*;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.templates.JavaExtensions;

public class Articles extends PageController {

    public static void index() {
        flash.keep();
        list(10);
    }

    public static void list(int size) {
        List<Article> articles = Article.recents(1, size);

        List<Article> nonPublishedArticles = null;
        if (Security.check(Role.ADMIN_SESSION)) {
            nonPublishedArticles = Article.findAllInvalid();
        }

        render(articles, size, nonPublishedArticles);
    }

    public static void create() {
        Member author = Member.findByLogin(Security.connected());
        Article article = new Article();
        render("Articles/edit.html", article);
    }

    public static void edit(final Long articleId) {
        Article article = Article.findById(articleId);
        notFoundIfNull(article);

        render(article);
    }

    private static void checkAccess(Article article) throws Throwable {
        if (!article.valid) {
            Member user = Member.findByLogin(Security.connected());
            if (user == null || !(user instanceof Staff)) {
                flash.error("Vous n'avez pas accès à cette fonctionnalité");
                index();
            }
        }
    }

    public static void show(final Long articleId, String slugify, boolean noCount) throws Throwable {
        Article article = Article.findById(articleId);
        notFoundIfNull(article);
        checkAccess(article);

        Article previous = article.findPrevious();
        Article following = article.findFollowing();
        // Don't count look when coming from internal redirect
        if (!noCount) {
            article.lookedBy(Member.findByLogin(Security.connected()));
        }
        // List<Activity> activities = Activity.recentsByArticle(article, 1, 10);
        render(article, previous, following /*, activities */);
    }
    
    public static void postComment(
            Long articleId,
            @Required String content) throws Throwable {
        Article article = Article.findById(articleId);
        notFoundIfNull(article);
        
        if (Validation.hasErrors()) {
            render("Articles/show.html", article);
        }

        Member author = Member.findByLogin(Security.connected());
        article.addComment(new ArticleComment(author, article, content));
        article.save();
        flash.success("Merci pour votre commentaire %s", author);
        show(articleId, JavaExtensions.slugify(article.title), true);
    }

    public static void save(Article article) throws Throwable {
        Logger.info("Tentative d'enregistrement d'article " + article);

        // Don't change author if already set
        if (article.author == null) {
            article.author = Member.findByLogin(Security.connected());
        }
        article.postedAt = new Date();

        validation.valid(article);
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            render("Articles/edit.html", article);
        }

        article.save();
        flash.success("L'article \"%s\" a été enregistré", article);
        Logger.info("L'article \"%s\" a été publié", article);
        show(article.id, JavaExtensions.slugify(article.title), true);
    }
    
    public static void validate(long articleId) throws Throwable {
        Article article = Article.findById(articleId);
        notFoundIfNull(article);

        article.validate();
        flash.success("L'article \"%s\" a été publié", article);
        show(article.id, JavaExtensions.slugify(article.title), true);
    }
    
    public static void unvalidate(long articleId) throws Throwable {
        Article article = Article.findById(articleId);
        notFoundIfNull(article);

        article.unvalidate();
        flash.success("L'article \"%s\" a été invalidé", article);
        show(article.id, JavaExtensions.slugify(article.title), true);
    }
    
    public static void delete(long articleId) {
        Article article = Article.findById(articleId);
        notFoundIfNull(article);

        article.delete();
        flash.success("L'article \"%s\" a été supprimé", article);
        index();
    }
}