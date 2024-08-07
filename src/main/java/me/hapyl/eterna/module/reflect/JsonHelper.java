package me.hapyl.eterna.module.reflect;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.annotation.Nullable;
import java.io.InputStreamReader;

public class JsonHelper {

    @Nullable
    public static JsonObject getJson(String url) {
        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final CloseableHttpResponse response = client.execute(new HttpGet(url));

            return new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), JsonObject.class);
        } catch (Exception e) {
            return null;
        }
    }

}
