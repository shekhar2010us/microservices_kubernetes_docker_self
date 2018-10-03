package com.app.restfulws.resource;

import org.json.JSONObject;

/**
 *
 * @author Shekhar.Agrawal
 */
public class MyJaxBeanJson {

    private String statusCode;
    private String statusMsg;
    private JSONObject result;

    public MyJaxBeanJson(){}

    public MyJaxBeanJson(String statusCode, String statusMsg, JSONObject result){
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.result = result;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setModels(JSONObject result) {
        this.result = result;
    }

}
