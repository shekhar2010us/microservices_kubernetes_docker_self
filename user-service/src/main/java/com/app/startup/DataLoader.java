package com.app.startup;

import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataLoader {

    public static int dbUser = 1;
    public static String redis_host = "redis";
    public static int redis_port = 6379;
    public static Jedis redis = null;

    static {
        connectToRedis(redis_host, redis_port);
    }

    public static Jedis connectToRedis(String host, int port) {
        try {
            redis = new Jedis(host, port);
            redis.select(dbUser);
            Set<String> keys = redis.keys("*");
            System.out.println("user all keys at startup");
            System.out.println(keys);
        } catch (Exception e) {
            System.err.println("Error in Redis connection: " + e);
        }
        System.out.println("Connected to redis");
        return redis;
    }

    public static Jedis connectToRedisA(String host, int port) {
        redis = new Jedis(host, port);
        while (true) {
            try {
                redis.select(dbUser);
                Set<String> keys = redis.keys("*");
                System.out.println("user after keys");
                System.out.println(keys);
                break;
            } catch (JedisConnectionException e) {
                System.err.println("Waiting for redis dataloader");
            }
        }
        System.err.println("Connected to redis");
        return redis;
    }

    public static Map<String, JSONObject> getUserData() {
        System.out.println("Getting User Data...");
        Map<String, JSONObject> list = new HashMap<>();

        String text = "101,'\"john\", 500.0\n" +
                "102,'\"mike\", 1000.0\n" +
                "103,'\"sam\", 800.0\n" +
                "104,'\"maria\", 750.0\n" +
                "105,'\"mary\", 1200.0\n" +
                "106,'\"lynda\", 1500.0\n";

        String[] records = text.split("\n");
        for (String record : records) {
            JSONObject json = new JSONObject();
            String[] parts = record.split(",");

            String user_id = parts[0].trim();
            String user_name = parts[1].trim();
            double user_credit = Double.parseDouble(parts[2].trim());
            json.put("user_id", user_id );
            json.put("user_name", user_name);
            json.put("user_credit", user_credit );
            list.put(user_id, json);
        }
        System.out.println("getUserData {}");
        System.out.println(list);
        return list;
    }
    public static void uploadData(Map<String, JSONObject> data, int dbNum) {
        System.out.println("Uploading User and Product Data...");
        redis.select(dbNum);
        for (String key : data.keySet()) {
            String val = data.get(key).toString();
            redis.set(key, val);
        }
    }

    public static void run() {
        System.out.println("Static Data Loader to Redis....");

        Map<String, JSONObject> userData = getUserData();
        uploadData(userData, dbUser);

    }
    public static void main(String[] args) {
        run();

        redis.select(dbUser);
        Set<String> keys = redis.keys("*");
        System.out.println("user all keys at startup");
        System.out.println(keys);

    }

}
