package push.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.util.Log;

public class PushSearvice extends Service {

    static final String TAG="LocalService";
    private Intent notificationIntent;
    private Context context;
    private final int icon = R.drawable.ic_launcher;
    private final CharSequence tickerText = "Hello";
    private NotificationManager mNotificationManager;
    private boolean run_flg = true;
    private ConditionVariable mCondition;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        context = getApplicationContext();
        notificationIntent = new Intent(this, TestPushActivity.class);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand Received start id " + startId + ": " + intent);
       //サービスはメインスレッドで実行されるため、処理を止めないように更新処理を別スレッドに。
        Thread thread = new Thread(null, mTask, "NotifyingService");
        mCondition = new ConditionVariable(false);
        thread.start();
        //明示的にサービスの起動、停止が決められる場合の返り値
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        run_flg = false;
        this.stopSelf();
        mCondition.open();
    }

    private int WAIT_TIME = 5000;//5秒単位
    private int i = 0;
    private Runnable mTask = new Runnable() {
        public void run() {
            while (run_flg) {
                long when = System.currentTimeMillis();
                Notification notification = new Notification(icon, tickerText, when);
                CharSequence contentTitle = "My notification";
                CharSequence contentText = "Hello World! "+i;
                PendingIntent contentIntent = PendingIntent.getActivity(PushSearvice.this, 0, notificationIntent, 0);
                notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
                mNotificationManager.notify(1, notification);
                mCondition.block(WAIT_TIME);
                i++;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
