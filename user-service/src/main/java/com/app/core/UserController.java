package com.app.core;

import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import java.util.*;

public class UserController {

    public static int dbUser = 1;
    public static String redis_host = "redis";
    public static int redis_port = 6379;
    public static Jedis redis = null;

    static {
        connectToRedis(redis_host, redis_port);
    }

    public static Jedis connectToRedis(String host, int port) {
        redis = new Jedis(host, port);
        while (true) {
            try {
                redis.select(dbUser);
                Set<String> keys = redis.keys("*");
                System.out.println("user after keys");
                System.out.println(keys);
                break;
            } catch (JedisConnectionException e) {
                System.err.println("Waiting for redis user controller");
            }
        }
        System.err.println("Connected to redis");
        return redis;
    }

    public UserController() {}

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        redis.select(dbUser);

        Set<String> keys = redis.keys("*");
        for (String key : keys) {
            User user = getUser(key);
            users.add(user);
        }
        return users;
    }
    public static User getUser(String userid) {
        User user = new User();
        redis.select(dbUser);
        try {
            JSONObject json = new JSONObject(redis.get(userid));
            user.userId = json.getString("user_id");
            user.userName = json.getString("user_name");
            user.userCredit = json.getDouble("user_credit");
        } catch (Exception ex) {
            System.err.println("Error in getting user: " + userid);
        }
        return user;
    }
    public static String setUser(String user_id, String user_name, double user_credit) {
        String res = null;

        JSONObject json = new JSONObject();
        json.put("user_id", user_id );
        json.put("user_name", user_name);
        json.put("user_credit", user_credit );

        redis.select(dbUser);
        try {
            res = redis.set(user_id, json.toString());
        } catch (Exception ex) {
            System.err.println("Error in setting user: " + user_id);
        }
        return res;
    }

}
