package tests;

import com.example.demo.entity.Post;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.geometry.Pos;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{
        /*
        String url = "http://129.204.153.225:8080/posts";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String responseData = response.body().string();
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(responseData);
        if (jsonElement.isJsonObject()){
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement jsonElement1 = jsonObject.get("_embedded").getAsJsonObject().get("postList");
            JsonArray jsonArray = jsonElement1.getAsJsonArray();
            System.out.println(jsonArray);
            List<Post> posts = new ArrayList<>();
            try {
                Gson gson = new Gson();
                for(JsonElement jsonElement2 : jsonArray){
                    posts.add(gson.fromJson(jsonElement2,Post.class));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(posts.get(0));
        }
        */
        System.out.println(new Timestamp(System.currentTimeMillis()));
//        Gson gson = new Gson();
//        User user = new User();
//        user.setId(1);
//        user.setPassword("1234566");
//        String str = gson.toJson(user);
//        System.out.println(str);
//        String str2 = "{\"id\":1,\"username\":\"weson\",\"password\":\"1234566\",\"book\":{\"name\":\"hello\",\"price\":15.3}}";
//        User user1 = gson.fromJson(str,User.class);
//        User user2 = gson.fromJson(str2,User.class);
//        System.out.println(gson.toJson(user2.getBook(),Book.class));
    }

}
