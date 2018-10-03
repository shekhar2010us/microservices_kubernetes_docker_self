package com.app.restfulws.resource;

import com.app.core.Product;
import com.app.core.ProductController;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shekhar.Agrawal
 */

@Path("productDesign")
public class ProductAction {
    
    public ProductAction() {}

    // http://localhost:8080/product-service-1.0/rest/productDesign/getallproducts
    @GET
    @Path("/getallproducts")
    @Produces("application/json")
    public MyJaxBeanProductList getAllProducts(@Context UriInfo ui) {
        MyJaxBeanProductList mb = null;
        List<Product> products = ProductController.getAllProducts();
        mb = new MyJaxBeanProductList("200", "OK", products);
        return mb;
    }

    // http://localhost:8080/product-service-1.0/rest/productDesign/getproduct?productid=201
    @GET
    @Path("/getproduct")
    @Produces("application/json")
    public MyJaxBeanProductList getProduct(@Context UriInfo ui) {
        MyJaxBeanProductList mb = null;
        List<Product> products = new ArrayList<>();

        MultivaluedMap<String,String> queryParams = ui.getQueryParameters();

        //Check for the Mandatory Parameters else return HTTP 412
        if( queryParams.containsKey("productid") ) {
            String productid = queryParams.getFirst("productid");
            Product product = ProductController.getProduct(productid);
            products.add(product);
            mb = new MyJaxBeanProductList("200", "OK", products);
        } else {
            // pre condition failed
            mb = new MyJaxBeanProductList("412","Mandatory Parameter 'productid' Missing", products);
        }
        return mb;
    }

    // http://localhost:8080/product-service-1.0/rest/productDesign/getproductjson?productid=201
    @GET
    @Path("/getproductjson")
    @Produces("application/json")
    public MyJaxBeanJson getProductJson(@Context UriInfo ui) {
        MyJaxBeanJson mb = null;

        MultivaluedMap<String,String> queryParams = ui.getQueryParameters();

        //Check for the Mandatory Parameters else return HTTP 412
        if( queryParams.containsKey("productid") ) {
            String productid = queryParams.getFirst("productid");
            Product product = ProductController.getProduct(productid);
            JSONObject json = new JSONObject();
            json.put("product_id", product.productId);
            json.put("product_name", product.productName);
            json.put("product_price", product.productPrice);
            mb = new MyJaxBeanJson("200", "OK", json);
        } else {
            // pre condition failed
            mb = new MyJaxBeanJson("412","Mandatory Parameter 'productid' Missing", null);
        }
        return mb;
    }

}
