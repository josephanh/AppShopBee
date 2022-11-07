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

}
