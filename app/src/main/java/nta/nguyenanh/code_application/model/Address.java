package nta.nguyenanh.code_application.model;

public class Address {
    private String name;
    private Integer code, province_code;

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
