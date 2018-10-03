package com.app.restfulws.resource;

import com.app.core.User;

import java.util.List;

/**
 *
 * @author Shekhar.Agrawal
 */
public class MyJaxBeanUserList {

  private String statusCode;
  private String statusMsg;
  private List<User> result;

  public MyJaxBeanUserList(){}

  public MyJaxBeanUserList(String statusCode, String statusMsg, List<User> result){
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
  
  public List<User> getResult() {
    return result;
  }

  public void setModels(List<User> result) {
    this.result = result;
  }
  
}
