package com.app.core;

import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import java.util.*;

public class ProductController {

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
                System.err.println("Waiting for redis retail controller");
            }
        }
        System.err.println("Connected to redis");
        return redis;
    }

    public ProductController() {}

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        redis.select(dbProduct);

        Set<String> keys = redis.keys("*");
        for (String key : keys) {
            Product product = getProduct(key);
            products.add(product);
        }
        return products;
    }
    public static Product getProduct(String productid) {
        Product product = new Product();
        redis.select(dbProduct);

        try {
            JSONObject json = new JSONObject(redis.get(productid));
            product.productId = json.getString("product_id");
            product.productName = json.getString("product_name");
            product.productPrice = json.getDouble("product_price");
        } catch (Exception ex) {
            System.err.println("Error in getting product: " + productid);
        }
        return product;
    }
    public static String setProduct(String product_id, String product_name, double product_price) {
        String res = null;

        JSONObject json = new JSONObject();
        json.put("product_id", product_id );
        json.put("product_name", product_name);
        json.put("product_price", product_price );

        redis.select(dbProduct);
        try {
            res = redis.set(product_id, json.toString());
        } catch (Exception ex) {
            System.err.println("Error in setting product: " + product_id);
        }
        return res;
    }

    public static void main(String[] args) {
        ProductController rc = new ProductController();
        String productid = "203";

        System.out.println("\n\t Get One Product");
        Product q2 = rc.getProduct(productid);
        System.out.println(q2);

    }

}
