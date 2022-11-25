package nta.nguyenanh.code_application.listener;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import nta.nguyenanh.code_application.PayActivity;
import nta.nguyenanh.code_application.model.ProductCart;

public class Listener {

    Context context;

    public Listener(Context context) {
        this.context = context;
    }

    public void gotoPay(ArrayList<ProductCart> list) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("listPay", list);
        context.startActivity(intent);
    }

}
