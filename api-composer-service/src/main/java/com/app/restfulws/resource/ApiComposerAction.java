package com.app.restfulws.resource;

import com.app.core.UserBuyProduct;
import org.json.JSONObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Shekhar.Agrawal
 */

@Path("apicomposerDesign")
public class ApiComposerAction {
    
    public ApiComposerAction() {}


    // http://localhost:8080/api-composer-service-1.0/rest/apicomposerDesign/userfromapic?userid=101
    @GET
    @Path("/userfromapic")
    @Produces("application/json")
    public MyJaxBeanJson userFromApiC(@Context UriInfo ui) {
        MyJaxBeanJson mb = null;
        MultivaluedMap<String,String> queryParams = ui.getQueryParameters();

        //Check for the Mandatory Parameters else return HTTP 412
        if( queryParams.containsKey("userid") ) {
            String userid = queryParams.getFirst("userid");
            // use user-service
            JSONObject userJson = getUserResponse(userid);

            mb = new MyJaxBeanJson("200" , "OK" , userJson);
        } else {
            // pre condition failed
            mb = new MyJaxBeanJson("412","Mandatory Parameters Missing", null);
        }
        return mb;
    }


    // http://localhost:8080/api-composer-service-1.0/rest/apicomposerDesign/userbuy?userid=101&productid=201
    @GET
    @Path("/userbuy")
    @Produces("application/json")
    public MyJaxBeanUserBuyProduct userBuy(@Context UriInfo ui) {
        MyJaxBeanUserBuyProduct mb = null;

        MultivaluedMap<String,String> queryParams = ui.getQueryParameters();

        //Check for the Mandatory Parameters else return HTTP 412
        if( queryParams.containsKey("userid") && queryParams.containsKey("productid") ) {
            String userid = queryParams.getFirst("userid");
            String productid = queryParams.getFirst("productid");

            // use user-service
            JSONObject userJson = getUserResponse(userid);
            // use product-service
            JSONObject productJson = getProductResponse(productid);

            double user_credit = userJson.getDouble("user_credit");
            String user_name = userJson.getString("user_name");
            double product_price = productJson.getDouble("product_price");

            boolean canBuy = false;
            if (user_credit >= product_price)
                canBuy = true;

            if (canBuy)
                setUser(userid, user_name, (user_credit - product_price));

            // actual response
            UserBuyProduct userBuyProduct = new UserBuyProduct();
            userBuyProduct.userId = userid;
            userBuyProduct.userName = user_name;
            userBuyProduct.userCredit = user_credit;
            userBuyProduct.productPrice = product_price;
            userBuyProduct.canBuy = canBuy;
            userBuyProduct.newUserCredit = user_credit;
            if (canBuy) {
                userBuyProduct.newUserCredit = (user_credit-product_price);
            }

            mb = new MyJaxBeanUserBuyProduct("200" , "OK" , userBuyProduct);
        } else {
            // pre condition failed
            mb = new MyJaxBeanUserBuyProduct("412","Mandatory Parameters Missing", null);
        }
        return mb;
    }

    private JSONObject getUserResponse(String userid) {
        // TODO
        JSONObject json = null;
        try {
            String url = null;
            if (userid.equalsIgnoreCase("101")) {
                url = "http://34.205.24.3:31003/user-service-1.0/rest/userDesign/getuser?userid=" + userid;
            } else if (userid.equalsIgnoreCase("102")) {
                url = "http://userservice:31003/user-service-1.0/rest/userDesign/getuser?userid=" + userid;
            } else if (userid.equalsIgnoreCase("103")) {
                url = "http://user-service:31003/user-service-1.0/rest/userDesign/getuser?userid=" + userid;
            }

            String resp = getUrlText(url);
            json = new JSONObject(resp);
        } catch (Exception ex) {
            System.err.println(ex);
        }
        MyJaxBeanJson mb = new MyJaxBeanJson("200", "OK", json);
        return mb.getResult();
    }

    private JSONObject getProductResponse(String productid) {
        // TODO -
        // use http://product-service:8080/product-service-1.0/rest/productDesign/getproductjson?productid=201 to get product
        MyJaxBeanJson mb = new MyJaxBeanJson();
        return mb.getResult();
    }
    private JSONObject setUser(String userid, String user_name, double user_credit) {
        // TODO -
        // use http://user-service:8080/user-service-1.0/rest/userDesign/setuser?userid=101&username=john&usercredit=200 to set user
        MyJaxBeanJson mb = new MyJaxBeanJson();
        return mb.getResult();
    }

    private String getUrlText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();
        return response.toString();
    }

}
