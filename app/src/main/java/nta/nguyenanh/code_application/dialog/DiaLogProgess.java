package nta.nguyenanh.code_application.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import nta.nguyenanh.code_application.R;

public class DiaLogProgess {
    Context context;
    Dialog dialog;

    public DiaLogProgess(Context context) {
        this.context = context;
    }
    public void showDialog(String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_progess);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        TextView titleprogess = dialog.findViewById(R.id.title_progess);
        titleprogess.setText(title);

        dialog.create();
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}
