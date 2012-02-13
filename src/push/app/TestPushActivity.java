package push.app;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestPushActivity extends Activity {

    private static final int HELLO_ID = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btn = (Button) findViewById(R.id.StartButton);
        btn.setOnClickListener(btnListener);//リスナの登録

        btn  = (Button) findViewById(R.id.StopButton);
        btn.setOnClickListener(btnListener);//リスナの登録
    }

    private OnClickListener btnListener = new OnClickListener() {
        public void onClick(View v) {

            switch(v.getId()){

            case R.id.StartButton://startServiceでサービスを起動
                startService(new Intent(TestPushActivity.this, PushSearvice.class));
                break;
            case R.id.StopButton://stopServiceでサービスの終了
                stopService(new Intent(TestPushActivity.this, PushSearvice.class));
                break;
            }
        }
    };
}