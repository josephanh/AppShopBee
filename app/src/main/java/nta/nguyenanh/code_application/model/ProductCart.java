package nta.nguyenanh.code_application.model;

import java.util.ArrayList;

public class ProductCart {
    private String id, nameproduct;
    private Float price;
    private String color;
    private Integer total;

    public ProductCart() {
    }

    public ProductCart(String id, String nameproduct, Float price, String color, Integer total) {
        this.id = id;
        this.nameproduct = nameproduct;
        this.price = price;
        this.color = color;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
