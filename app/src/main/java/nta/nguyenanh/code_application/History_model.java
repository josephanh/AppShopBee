package nta.nguyenanh.code_application;

public class History_model {

    private int id_history;
    private String name_history;

    public History_model(int id_history, String name_history) {
        this.id_history = id_history;
        this.name_history = name_history;
    }

    public History_model() {
    }

    public int getId_history() {
        return id_history;
    }

    public void setId_history(int id_history) {
        this.id_history = id_history;
    }

    public String getName_history() {
        return name_history;
    }

    public void setName_history(String name_history) {
        this.name_history = name_history;
    }
}
