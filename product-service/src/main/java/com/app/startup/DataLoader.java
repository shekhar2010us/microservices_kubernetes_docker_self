package com.app.startup;

import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataLoader {

    public static int dbProduct = 2;
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
                redis.select(dbProduct);
                Set<String> keys = redis.keys("*");
                System.out.println("product after keys");
                System.out.println(keys);
                break;
            } catch (JedisConnectionException e) {
                System.err.println("Waiting for redis dataloader");
            }
        }
        System.err.println("Connected to redis");
        return redis;
    }
    public static Map<String, JSONObject> getProductData() {
        System.out.println("Getting Product Data...");
        Map<String, JSONObject> list = new HashMap<>();

        String text = "201,'\"watch\", 850.0\n" +
                "202,'\"phone\", 1050.0\n" +
                "203,'\"bag\", 250.0\n" +
                "204,'\"laptop\", 1300.0\n" +
                "205,'\"headphone\", 300.0\n" +
                "206,'\"camera\", 550.0\n";

        String[] records = text.split("\n");
        for (String record : records) {
            JSONObject json = new JSONObject();
            String[] parts = record.split(",");

            String product_id = parts[0].trim();
            String product_name = parts[1].trim();
            double product_price = Double.parseDouble(parts[2].trim());

            json.put("product_id", product_id );
            json.put("product_name", product_name);
            json.put("product_price", product_price );
            list.put(product_id, json);
        }
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

        Map<String, JSONObject> productData = getProductData();
        uploadData(productData, dbProduct);
    }
    public static void main(String[] args) {
        run();

        redis.select(dbProduct);
        Set<String> keys = redis.keys("*");
        System.out.println("product after keys");
        System.out.println(keys);

    }

}
