
package com.max.toolbox;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.max.toolbox.view.DragListView;

import java.util.ArrayList;

public class DragListActivity extends Activity {

    private DragListView dragListView;
    private ArrayList<String> arrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draglistactivity);

        arrayList.add("临啊的送风随碟附送的飞");
        arrayList.add("兵去委屈委屈委屈委屈委屈");
        arrayList.add("斗巨亏与口语与i抑郁");
        arrayList.add("者进口红酒客户进口红酒客户尽快i偶i偶偶");
        arrayList.add("皆v不错v不错v不错v标点符号反光镜皇军服");
        arrayList.add("阵接口了接口了接口了接口邻居i偶i欧尼");
        arrayList.add("列在现场咨询case亲我的爱我认为通过的风格");
        arrayList.add("在风获得返回购房合同与同一u哦帮vnv");
        arrayList.add("前asfsdgdfhgjhljk;易图通以偶ifrtutipio[");

        dragListView = (DragListView) findViewById(R.id.dragListView);
        dragListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList));

    }

}
