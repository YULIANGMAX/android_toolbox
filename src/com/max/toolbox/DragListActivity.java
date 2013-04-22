
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

        arrayList.add("�ٰ����ͷ�������͵ķ�");
        arrayList.add("��ȥί��ί��ί��ί��ί��");
        arrayList.add("���޿��������i����");
        arrayList.add("�߽��ں�ƿͻ����ں�ƿͻ�����iżiżż");
        arrayList.add("��v����v����v����v�����ŷ��⾵�ʾ���");
        arrayList.add("��ӿ��˽ӿ��˽ӿ��˽ӿ��ھ�iżiŷ��");
        arrayList.add("�����ֳ���ѯcase���ҵİ�����Ϊͨ���ķ��");
        arrayList.add("�ڷ��÷��ع�����ͬ��ͬһuŶ��vnv");
        arrayList.add("ǰasfsdgdfhgjhljk;��ͼͨ��żifrtutipio[");

        dragListView = (DragListView) findViewById(R.id.dragListView);
        dragListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList));

    }

}
