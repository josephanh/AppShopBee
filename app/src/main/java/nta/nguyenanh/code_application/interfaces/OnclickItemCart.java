package nta.nguyenanh.code_application.interfaces;

public interface OnclickItemCart {
    void onClickMinus(int totalNew, int totalOld, float price);
    void onClickPlus(int totalNew, int totalOld, float price);

    void onClickCheck(boolean isCheck, int total, float price);
}
