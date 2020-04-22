package me.cunzai.uphelper.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class JsonUtil {
    private static JsonParser parser;

    static {
        parser = new JsonParser();
    }

    public static JsonObject getJsonFromString(String str){
        return parser.parse(str).getAsJsonObject();
    }

    @SneakyThrows
    public static JsonObject getJsonFromResponse(HttpResponse response){
        return parser.parse(EntityUtils.toString(response.getEntity())).getAsJsonObject();
    }

    @SneakyThrows
    public static JsonArray getJsonArrayFromResponse(HttpResponse response){
        return parser.parse(EntityUtils.toString(response.getEntity())).getAsJsonArray();
    }
}
