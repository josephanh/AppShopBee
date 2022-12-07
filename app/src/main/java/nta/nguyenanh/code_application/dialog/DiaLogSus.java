package nta.nguyenanh.code_application.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import nta.nguyenanh.code_application.MainActivity;
import nta.nguyenanh.code_application.R;

public class DiaLogSus {
    Context context;
    Dialog dialog;
    goToHome goToHome;

    public DiaLogSus(Context context, DiaLogSus.goToHome goToHome) {
        this.context = context;
        this.goToHome = goToHome;
    }

    public void showDiaLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sus, null);

        builder.setView(view);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button gotoHo = view.findViewById(R.id.gotoHome);
        gotoHo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome.gotoMain();
                hideDialog();
            }
        });

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
    public void hideDialog() {
        dialog.hide();
    }

    public interface goToHome {
        void gotoMain();
    }
}
