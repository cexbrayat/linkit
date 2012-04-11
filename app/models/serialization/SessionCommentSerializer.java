package models.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.SessionComment;

import java.lang.reflect.Type;

/**
 * @author: Samil
 */
public class SessionCommentSerializer implements JsonSerializer<SessionComment> {
    @Override
    public JsonElement serialize(SessionComment comment, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", comment.id);
        obj.addProperty("sessionId", comment.session.id);
        JsonObject author = new JsonObject();
        author.addProperty("id", comment.author.id);
        author.addProperty("firstname", comment.author.firstname);
        author.addProperty("lastname", comment.author.lastname);
        obj.add("author", author);
        obj.addProperty("date", comment.postedAt.toString());
        obj.addProperty("content", comment.content);
        return obj;
    }
}
