package com.app.restfulws.resource;

import com.app.core.UserBuyProduct;

/**
 *
 * @author Shekhar.Agrawal
 */
public class MyJaxBeanUserBuyProduct {

  private String statusCode;
  private String statusMsg;
  private UserBuyProduct result;

  public MyJaxBeanUserBuyProduct(){}

  public MyJaxBeanUserBuyProduct(String statusCode, String statusMsg, UserBuyProduct result){
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
  
  public UserBuyProduct getResult() {
    return result;
  }

  public void setModels(UserBuyProduct result) {
    this.result = result;
  }
  
}
