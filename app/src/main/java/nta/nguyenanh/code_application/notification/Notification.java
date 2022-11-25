package nta.nguyenanh.code_application.notification;

import static android.content.Context.NOTIFICATION_SERVICE;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.RemoteViews;

import nta.nguyenanh.code_application.R;

public class Notification {

    Context context;

    public Notification(Context context) {
        this.context = context;
    }



    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void customNotification(String subText, String contentText){

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        android.app.Notification notification;

        Intent iNotify = new Intent(context, Notification.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, Utils.REQ_CODE, iNotify, PendingIntent.FLAG_IMMUTABLE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    notification = new android.app.Notification.Builder(context)
                            .setSmallIcon(R.drawable.logo_1)
                            .setContentText(contentText)
//                            .setLargeIcon(getBitmapFromVectorDrawable(this, R.drawable.ic_logo_main))
                            .setSubText(subText)
                            .setSettingsText(subText)
                            .setChannelId(Utils.CHANNEL_ID)
                            .build();
                    manager.createNotificationChannel(new NotificationChannel(Utils.CHANNEL_ID, "Shopbee", NotificationManager.IMPORTANCE_HIGH));
        }
        else {
                notification = new android.app.Notification.Builder(context)
                        .setSmallIcon(R.drawable.logo_1)
                        .setContentText(contentText)
                        .setSubText(subText)
//                        .setLargeIcon(getBitmapFromVectorDrawable(this, R.drawable.ic_logo_main))
                        .setContentIntent(pendingIntent)
                        .build();
        }
        manager.notify(Utils.NOTIFY_ID, notification);
    }
}