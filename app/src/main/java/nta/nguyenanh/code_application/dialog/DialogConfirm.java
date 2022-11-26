package nta.nguyenanh.code_application.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nta.nguyenanh.code_application.DetailProductActivity;
import nta.nguyenanh.code_application.LoginActivity;
import nta.nguyenanh.code_application.MainActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.interfaces.OnClickDiaLogConfirm;
import nta.nguyenanh.code_application.interfaces.OnClickItemProduct;
import nta.nguyenanh.code_application.model.Product;

public class DialogConfirm {
    Context context;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    OnClickDiaLogConfirm clickEvent;

    public DialogConfirm(Context context, OnClickDiaLogConfirm clickEvent) {
        this.context = context;
        this.clickEvent = clickEvent;
    }

    public void showDialog() {
        View view = View.inflate(context, R.layout.custom_dialog_confirm, null);
        builder = new AlertDialog.Builder(context);
        builder.setView(view);
        dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        TextView titleprogess = view.findViewById(R.id.title_progess);
        Button btndongy = view.findViewById(R.id.btn_dongy);
        Button btnhuy = view.findViewById(R.id.btn_huy);
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });

        btndongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent.ClickButtonAgree();
                hideDialog();
            }
        });
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}
