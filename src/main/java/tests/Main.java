package tests;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args){
        Gson gson = new Gson();
        User user = new User();
        user.setId(1);
        user.setPassword("1234566");
        user.setUserName("weson");
        String str = gson.toJson(user);
        String str2 = "{\"id\":1,\"username\":\"weson\",\"password\":\"1234566\",\"book\":{\"name\":\"hello\",\"price\":15.3}}";
        User user1 = gson.fromJson(str,User.class);
        User user2 = gson.fromJson(str2,User.class);
        System.out.println(gson.toJson(user2.getBook(),Book.class));
    }
}
