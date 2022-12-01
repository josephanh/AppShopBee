package nta.nguyenanh.code_application.interfaces;

import android.widget.CheckBox;
import android.widget.ImageView;

public interface OnclickItemCart {
    void onClickMinus(int totalNew, int totalOld, float price);
    void onClickPlus(int totalNew, int totalOld, float price);

    void onClickCheck(boolean isCheck, int total, float price);

    void hideCheck(CheckBox checkBox, ImageView imageView);

    void onClickDelete(String id, int position);
}
