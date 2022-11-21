package nta.nguyenanh.code_application.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    private String id, nameproduct, describe;
    private Float price;
    private Integer available;
    private ArrayList<String> color, image;
    private Integer sale, sold, total;
    private String id_category;
    private Long datestart,dateend;

    public Product(String id, String nameproduct, String describe, Float price, Integer available, ArrayList<String> color, ArrayList<String> image, Integer sale, Integer sold, Integer total, String id_category, Long datestart, Long dateend) {
        this.id = id;
        this.nameproduct = nameproduct;
        this.describe = describe;
        this.price = price;
        this.available = available;
        this.color = color;
        this.image = image;
        this.sale = sale;
        this.sold = sold;
        this.total = total;
        this.id_category = id_category;
        this.datestart = datestart;
        this.dateend = dateend;
    }

    public Product(String id, String nameproduct, String describe, Float price, Integer available, ArrayList<String> color, ArrayList<String> image, Integer sale, Integer sold, Integer total, String id_category) {
        this.id = id;
        this.nameproduct = nameproduct;
        this.describe = describe;
        this.price = price;
        this.available = available;
        this.color = color;
        this.image = image;
        this.sale = sale;
        this.sold = sold;
        this.total = total;
        this.id_category = id_category;
    }

    public Product(String id, Float price, ArrayList<String> color, Integer total) {
        this.id = id;
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public ArrayList<String> getColor() {
        return color;
    }

    public void setColor(ArrayList<String> color) {
        this.color = color;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public Long getDatestart() {
        return datestart;
    }

    public void setDatestart(Long datestart) {
        this.datestart = datestart;
    }

    public Long getDateend() {
        return dateend;
    }

    public void setDateend(Long dateend) {
        this.dateend = dateend;
    }
}
