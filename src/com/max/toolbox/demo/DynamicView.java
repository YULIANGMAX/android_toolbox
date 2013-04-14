
package com.max.toolbox.demo;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DynamicView {

    public void dynamicLinearLayout(Activity activity) {
        // 第一是容器，创建一个容器(这里用LinearLayout举例)；
        LinearLayout lLayout = new LinearLayout(activity);// (这里的this是一个Context变量)；
        // 第二是容器的细节，也就是属性部分；比如，容器里的控件是怎样排的；
        lLayout.setOrientation(LinearLayout.VERTICAL);
        
        // 第三是容器里的控件，创建自己想要的控件；
        EditText editText = new EditText(activity);
        Button button = new Button(activity);// 这里的this都是一个Context变量；
        editText.setLines(3);
        editText.setGravity(Gravity.TOP | Gravity.LEFT);
        button.setText("插入随机表情");
        
        // 第四是容器的细节，就是设置容器的属性；
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.setMargins(0, 0, 0, 0);// 设置margin属性

        // 第五是将控件加入到容器当中去；
        lLayout.addView(editText, lParams);
        lLayout.addView(button, lParams);
        
        // 第六是将容器将加入ContentView中；
        activity.setContentView(lLayout);
    }
    
    public void dynamicRelativeLayout(Activity activity) {
        // 第一是容器，创建一个容器(这里用LinearLayout举例)；
        RelativeLayout rLayout = new RelativeLayout(activity);// (这里的this是一个Context变量)；
        // 第二是容器的细节，也就是属性部分；比如，容器里的控件是怎样排的；
        rLayout.setGravity(Gravity.CENTER);
        
        // 第三是容器里的控件，创建自己想要的控件；
        EditText editText = new EditText(activity);
        Button button = new Button(activity);// 这里的this都是一个Context变量；
        editText.setLines(3);
        editText.setGravity(Gravity.TOP | Gravity.LEFT);
        button.setText("插入随机表情");
        
        // 第四是容器的细节，就是设置容器的属性；
        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置RelativeLayout控件下android:layout_centerHorizontal属性
        rParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        // 设置RelativeLayout控件下android:layout_below属性
        rParams.addRule(RelativeLayout.BELOW, editText.getId());
        // 设置RelativeLayout控件下android:layout_alignParentLeft属性
        rParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        // 设置RelativeLayout控件下android:layout_toRightOf属性
        rParams.addRule(RelativeLayout.LEFT_OF, button.getId());
        
        // 第五是将控件加入到容器当中去；
        rLayout.addView(editText, rParams);
        rLayout.addView(button, rParams);

        // 第六是将容器将加入ContentView中；
        activity.setContentView(rLayout);
    }

}
