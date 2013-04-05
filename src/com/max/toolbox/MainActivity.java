
package com.max.toolbox;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.android_utils.R;
import com.max.toolbox.utils.MediaUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String path = Environment.getExternalStorageDirectory().toString()+"/ttt/xxx/ddd/rrr/4.aac";
        final MediaUtil mediaUtil = new MediaUtil();
        mediaUtil.recVoice(path);
        
        Button start,stop;
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                mediaUtil.startRec();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}