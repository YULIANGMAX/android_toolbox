
package com.max.toolbox.demo;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DynamicView {

    public void dynamicLinearLayout(Activity activity) {
        // ��һ������������һ������(������LinearLayout����)��
        LinearLayout lLayout = new LinearLayout(activity);// (�����this��һ��Context����)��
        // �ڶ���������ϸ�ڣ�Ҳ�������Բ��֣����磬������Ŀؼ��������ŵģ�
        lLayout.setOrientation(LinearLayout.VERTICAL);
        
        // ������������Ŀؼ��������Լ���Ҫ�Ŀؼ���
        EditText editText = new EditText(activity);
        Button button = new Button(activity);// �����this����һ��Context������
        editText.setLines(3);
        editText.setGravity(Gravity.TOP | Gravity.LEFT);
        button.setText("�����������");
        
        // ������������ϸ�ڣ������������������ԣ�
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0, 0, 0, 0);// ����margin����

        // �����ǽ��ؼ����뵽��������ȥ��
        lLayout.addView(editText, lParams);
        lLayout.addView(button, lParams);
        
        // �����ǽ�����������ContentView�У�
        activity.setContentView(lLayout);
    }
    
    public void dynamicRelativeLayout(Activity activity) {
        // ��һ������������һ������(������LinearLayout����)��
        RelativeLayout rLayout = new RelativeLayout(activity);// (�����this��һ��Context����)��
        // �ڶ���������ϸ�ڣ�Ҳ�������Բ��֣����磬������Ŀؼ��������ŵģ�
        rLayout.setGravity(Gravity.CENTER);
        
        // ������������Ŀؼ��������Լ���Ҫ�Ŀؼ���
        EditText editText = new EditText(activity);
        Button button = new Button(activity);// �����this����һ��Context������
        editText.setLines(3);
        editText.setGravity(Gravity.TOP | Gravity.LEFT);
        button.setText("�����������");
        
        // ������������ϸ�ڣ������������������ԣ�
        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // ����RelativeLayout�ؼ���android:layout_centerHorizontal����
        rParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        // ����RelativeLayout�ؼ���android:layout_below����
        rParams.addRule(RelativeLayout.BELOW, editText.getId());
        // ����RelativeLayout�ؼ���android:layout_alignParentLeft����
        rParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        // ����RelativeLayout�ؼ���android:layout_toRightOf����
        rParams.addRule(RelativeLayout.LEFT_OF, button.getId());
        
        // �����ǽ��ؼ����뵽��������ȥ��
        rLayout.addView(editText, rParams);
        rLayout.addView(button, rParams);

        // �����ǽ�����������ContentView�У�
        activity.setContentView(rLayout);
    }

}
