package com.com.Bll;

import android.util.Log;

import com.com.entity.Fileorder;
import com.com.entity.SetFileorder;
import com.com.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class CBLL {

    private static CBLL cbll = new CBLL(); //在每次调用是自动创建实例

    private CBLL() {
    }

    public static CBLL getInstance() {
        return cbll;
    }


    /*
     *-----------------------------------用户登录-----------------------------
     */
    public JSONEntity jsonlogin(JSONObject json) {

        JSONEntity entity = new JSONEntity();
        try {
            boolean flag = json.getBoolean("flag");
            if (flag) {
                entity.setFlag(flag);
                JSONObject mUserJS = json.getJSONObject("data");
                User userentity = new User(mUserJS);
                entity.setData(userentity);

            } else {
                entity.setFlag(flag);
                entity.setMessage(json.getInt("message"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return entity;
    }

    /*
     *-----------------------------------获取用户待处理订单--------------------------
     */
    public JSONEntity jsoneditor(JSONObject json) {

        JSONEntity entity = new JSONEntity();
        try {
            boolean flag = json.getBoolean("flag");
            if (flag) {
                entity.setFlag(flag);
                JSONArray array = json.getJSONArray("result");
                List<Fileorder> orderlist = new ArrayList<Fileorder>();
                for (int i = 0; i < array.length(); i++) {
                    Fileorder order = new Fileorder((JSONObject) array.get(i));
                    orderlist.add(order);
                }
                SetFileorder carset = new SetFileorder(orderlist);
                entity.setData(carset);

            } else {
                entity.setFlag(flag);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return entity;
    }

    /*
     *-----------------------------------搜索订单--------------------------
     */
    public JSONEntity jsonsearch(JSONObject json) {

        JSONEntity entity = new JSONEntity();
        try {
            boolean flag = json.getBoolean("flag");
            if (flag) {
                entity.setFlag(flag);
                JSONObject mUserJS = json.getJSONObject("data");
                Fileorder userentity = new Fileorder(mUserJS);
                entity.setData(userentity);

            } else {
                entity.setFlag(flag);


            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return entity;
    }


    /*
     *-----------------------------------获取所有历史订单--------------------------
     */
    public JSONEntity jsonsearchhis(JSONObject json) {


        JSONEntity entity = new JSONEntity();
        JSONArray array = null;
        List<Fileorder> orderlist = new ArrayList<Fileorder>();
        try {
            array = json.getJSONArray("result");

            for (int i = 0; i < array.length(); i++) {
                Fileorder order = new Fileorder((JSONObject) array.get(i));
                orderlist.add(order);
            }

            SetFileorder carset = new SetFileorder(orderlist);
            entity.setData(carset);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return entity;
    }


    /*
     *-----------------------------------获取所有的编辑人员--------------------------
     */
    public JSONEntity geteditorjson(JSONObject json) {

        JSONEntity entity = new JSONEntity();
        try {
            boolean flag = json.getBoolean("flag");
            if (flag) {
                entity.setFlag(flag);
                JSONArray array = json.getJSONArray("result");
                List<User> orderlist = new ArrayList<User>();
                for (int i = 0; i < array.length(); i++) {
                    User order = new User((JSONObject) array.get(i), 2);
                    orderlist.add(order);
                }
                entity.setData(orderlist);

            } else {
                entity.setFlag(flag);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return entity;
    }
}

