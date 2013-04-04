package controllers.api;

import play.mvc.Router;
import play.vfs.VirtualFile;

import java.lang.Object;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

public class ApiUrl {

    public static String getInterestUrl(long id) {
        return Router.getFullUrl("api.ApiInterests.interest", map(id));
    }

    public static String getMemberUrl(long id) {
        return Router.getFullUrl("api.ApiMembers.member", map(id));
    }

    private static Map<String, Object> map(long id) {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("id", id);
        return params;
    }

    public static String getFullUrl(final String path) {
        if (path == null) return null;
        if (path.startsWith("http")) return path;
        return Router.reverse(VirtualFile.fromRelativePath(path), true);
    }
}
