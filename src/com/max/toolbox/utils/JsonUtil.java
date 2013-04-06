
package com.max.toolbox.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @类名 JsonUtil
 * @作者 YULIANGMAX
 * @日期 2013-4-5
 * @版本 1.0
 */
public class JsonUtil {

    // 对象式
    String objJson = "{\"userbean\":{\"Uid\":\"100196\",\"Showname\":\"\u75af\u72c2\u7684\u7334\u5b50\",\"Avtar\":null,\"State\":1}}";
    
    class UserBean {
        String uid;
        String showname;
        String avtar;
        String state;
    }
    
    public void getJsonObj() throws JSONException {
        UserBean userBean = new UserBean();
        JSONObject jsonObject = new JSONObject(objJson).getJSONObject("userbean");
        userBean.uid = jsonObject.getString("Uid");
        userBean.showname = jsonObject.getString("Showname");
        userBean.avtar = jsonObject.getString("Avtar");
        userBean.state = jsonObject.getString("State");
    }

    // 数组式
    String arrayJson =
    "{\"calendar\":" +
            "{\"calendarlist\":"+
                "[" +
                "{\"calendar_id\":\"1705\",\"title\":\"(\u4eb2\u5b50)ddssd\",\"category_name\":\"\u9ed8\u8ba4\u5206\u7c7b\",\"showtime\":\"1288927800\",\"endshowtime\":\"1288931400\",\"allDay\":false}," +
                "{\"calendar_id\":\"1706\",\"title\":\"(\u65c5\u884c)\",\"category_name\":\"\u9ed8\u8ba4\u5206\u7c7b\",\"showtime\":\"1288933200\",\"endshowtime\":\"1288936800\",\"allDay\":false}" +
                "]" + 
            "}" +
    "}";
    
    class MyCalendar {
        String calendar_id;
        String title;
        String category_name;
        String showtime;
        String endshowtime;
        boolean allDay;
    }

    public void getJsonArray() throws JSONException {
        JSONObject jsonObject = new JSONObject(arrayJson).getJSONObject("calendar");
        JSONArray jsonArray = jsonObject.getJSONArray("calendarlist");
        ArrayList<MyCalendar> myCalendars = new ArrayList<JsonUtil.MyCalendar>();
        for (int i = 0; i < jsonArray.length(); i++) {
            MyCalendar myCalendar = new MyCalendar();
            JSONObject obj = (JSONObject) jsonArray.opt(i);
            myCalendar.calendar_id = obj.getString("calendar_id");
            myCalendar.title = obj.getString("title");
            myCalendar.category_name = obj.getString("category_name");
            myCalendar.showtime = obj.getString("showtime");
            myCalendar.endshowtime = obj.getString("endshowtime");
            myCalendar.allDay = obj.getBoolean("allDay");
            myCalendars.add(myCalendar);
        }
    }
    


}
