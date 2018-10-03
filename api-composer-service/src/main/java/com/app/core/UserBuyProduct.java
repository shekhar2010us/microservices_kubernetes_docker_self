package com.app.core;

public class UserBuyProduct {

    public String userId;
    public String userName;
    public Double userCredit;
    public Double productPrice;
    public boolean canBuy;
    public double newUserCredit;

    public UserBuyProduct() {}

    public UserBuyProduct(String _userId, String _userName, double _userCredit, double _productPrice
    , boolean _canBuy, double _newUserCredit) {
        super();
        this.userId = _userId;
        this.userName = _userName;
        this.userCredit = _userCredit;
        this.productPrice = _productPrice;
        this.canBuy = _canBuy;
        this.newUserCredit = _newUserCredit;
    }

    @Override
    public String toString() {
        return "userId[" + userId + "] " +
                "userName[" + userName + "] " +
                "with credit[" + userCredit + "] " +
                "can buy [" + canBuy + "] " +
                "product [" + productPrice + "] " +
                "new credit will be [" + newUserCredit + "] "
                ;
    }

}
