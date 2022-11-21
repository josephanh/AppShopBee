package nta.nguyenanh.code_application.model;

public class CartModel {
    private String idProduct, nameProduct, color;
    private Float price;
    private Integer total;

    public CartModel(String idProduct, String nameProduct, String color, Float price, Integer total) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.color = color;
        this.price = price;
        this.total = total;
    }

    public CartModel(String nameProduct, String color, Float price, Integer total) {
        this.nameProduct = nameProduct;
        this.color = color;
        this.price = price;
        this.total = total;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
