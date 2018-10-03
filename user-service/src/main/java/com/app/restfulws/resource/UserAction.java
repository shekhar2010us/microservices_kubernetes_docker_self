package com.app.restfulws.resource;

import com.app.core.UserController;
import com.app.core.User;
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

@Path("userDesign")
public class UserAction {
    
    public UserAction() {}

    // http://localhost:8080/user-service-1.0/rest/userDesign/getallusers
    @GET
    @Path("/getallusers")
    @Produces("application/json")
    public MyJaxBeanUserList getAllUsers(@Context UriInfo ui) {
        MyJaxBeanUserList mb = null;
        List<User> users = UserController.getAllUsers();
        mb = new MyJaxBeanUserList("200", "OK", users);
        return mb;
    }

    // http://localhost:8080/user-service-1.0/rest/userDesign/getuser?userid=101
    @GET
    @Path("/getuser")
    @Produces("application/json")
    public MyJaxBeanUserList getUser(@Context UriInfo ui) {
        MyJaxBeanUserList mb = null;
        List<User> users = new ArrayList<>();

        MultivaluedMap<String,String> queryParams = ui.getQueryParameters();

        //Check for the Mandatory Parameters else return HTTP 412
        if( queryParams.containsKey("userid") ) {
            String userid = queryParams.getFirst("userid");
            User user = UserController.getUser(userid);
            users.add(user);
            mb = new MyJaxBeanUserList("200", "OK", users);
        } else {
            // pre condition failed
            mb = new MyJaxBeanUserList("412","Mandatory Parameter 'userid' Missing", users);
        }
        return mb;
    }

    // http://localhost:8080/user-service-1.0/rest/userDesign/getuserjson?userid=101
    @GET
    @Path("/getuserjson")
    @Produces("application/json")
    public MyJaxBeanJson getUserJson(@Context UriInfo ui) {
        MyJaxBeanJson mb = null;

        MultivaluedMap<String,String> queryParams = ui.getQueryParameters();

        //Check for the Mandatory Parameters else return HTTP 412
        if( queryParams.containsKey("userid") ) {
            String userid = queryParams.getFirst("userid");
            User user = UserController.getUser(userid);
            JSONObject json = new JSONObject();
            json.put("user_id", user.userId);
            json.put("user_name", user.userName);
            json.put("user_credit", user.userCredit);
            mb = new MyJaxBeanJson("200", "OK", json);
        } else {
            // pre condition failed
            mb = new MyJaxBeanJson("412","Mandatory Parameter 'userid' Missing", null);
        }
        return mb;
    }

    // http://localhost:8080/user-service-1.0/rest/userDesign/setuser?userid=101&username=john&usercredit=200
    @GET
    @Path("/setuser")
    @Produces("application/json")
    public MyJaxBeanJson setUser(@Context UriInfo ui) {
        MyJaxBeanJson mb = null;
        JSONObject jsonResp = new JSONObject();

        MultivaluedMap<String,String> queryParams = ui.getQueryParameters();

        //Check for the Mandatory Parameters else return HTTP 412
        if( queryParams.containsKey("userid") && queryParams.containsKey("username") && queryParams.containsKey("usercredit") ) {
            String userid = queryParams.getFirst("userid");
            String username = queryParams.getFirst("username");
            double usercredit = Double.parseDouble(queryParams.getFirst("usercredit"));
            UserController.setUser(userid, username, usercredit);
            jsonResp.put("inserted_new_user", true);
            mb = new MyJaxBeanJson("200", "OK", jsonResp);
        } else {
            // pre condition failed
            mb = new MyJaxBeanJson("412","Mandatory Parameter 'userid' Missing", null);
        }
        return mb;
    }

}
