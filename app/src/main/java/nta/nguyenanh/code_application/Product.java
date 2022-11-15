package nta.nguyenanh.code_application;

public class Product {
    String id;
    String nameproduct;

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

    public Product() {
    }

    public Product(String id, String nameproduct) {
        this.id = id;
        this.nameproduct = nameproduct;
    }
}
