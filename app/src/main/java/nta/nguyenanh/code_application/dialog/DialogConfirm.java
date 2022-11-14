package nta.nguyenanh.code_application.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nta.nguyenanh.code_application.R;

public class DialogConfirm {
    Context context;
    Dialog dialog;

    public DialogConfirm(Context context) {
        this.context = context;
    }

    public void showDialog(String dongy, String huy) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_progess);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        TextView titleprogess = dialog.findViewById(R.id.title_progess);
        Button btndongy = dialog.findViewById(R.id.btn_dongy);
        Button btnhuy = dialog.findViewById(R.id.btn_huy);
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });

        btndongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.create();
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}
