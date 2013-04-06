
package com.max.toolbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.max.toolbox.utils.MediaUtil;

import java.io.IOException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startRecVoice();
//        startHeartbeatService();
        
    }
    
    private void startRecVoice() {
        setContentView(R.layout.activity_recvoice);
        String path = Environment.getExternalStorageDirectory().toString()+"/ttt/xxx/ddd/rrr/4.aac";
        final MediaUtil mediaUtil = new MediaUtil();
        mediaUtil.recVoice(path);
        
        Button start,stop;
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    mediaUtil.startRec();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                mediaUtil.stopRec();
            }
        });
    }
    
    private void startHeartbeatService() {
        Intent serviceIntent = new Intent("HeartbeatService");  
        serviceIntent.putExtra("url","http://www.xxx.com");  
        startService(serviceIntent);  
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
