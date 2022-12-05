package nta.nguyenanh.code_application.model;

public class Address {
    private String idAddress;
    private String name;
    private Integer code, province_code;

    private String address, nameReceiver, phonenumber;
    private int available;


    public Address(String idAddress,String address, String nameReceiver, String phonenumber, int available) {
        this.idAddress = idAddress;
        this.address = address;
        this.nameReceiver = nameReceiver;
        this.phonenumber = phonenumber;
        this.available = available;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNameReceiver() {
        return nameReceiver;
    }

    public void setNameReceiver(String nameReceiver) {
        this.nameReceiver = nameReceiver;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public Address(String name, Integer code, Integer province_code) {
        this.name = name;
        this.code = code;
        this.province_code = province_code;
    }

    public Address(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getProvince_code() {
        return province_code;
    }

    public void setProvince_code(Integer province_code) {
        this.province_code = province_code;
    }
}
