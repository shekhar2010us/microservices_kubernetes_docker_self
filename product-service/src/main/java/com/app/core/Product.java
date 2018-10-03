package com.app.core;

public class Product {

    public String productId;
    public String productName;
    public Double productPrice;

    public Product() {}

    public Product(String _productId, String _productName, double _productPrice) {
        this.productId = _productId;
        this.productName = _productName;
        this.productPrice = _productPrice;
    }

    @Override
    public String toString() {
        return "productId[" + productId + "] productName[" + productName + "] with price[" + productPrice + "]";
    }


}
