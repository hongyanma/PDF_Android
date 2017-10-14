package com.com.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/12.
 */

public class Fileorder {

    private int file_id;
    private String order_id;
    private String client_name;
    private String order_time;
    private String delivery_time;
    private String end_time;
    private int order_state;
    private String file_path;
    private int edition;
    private String schedule;
    private String explain;
    private String remark;
    private int user_id;
    private int locked;
    private String edit_time;

    private String modify_time;
    private String order_name;



    public  Fileorder(){}




    public  Fileorder(JSONObject json){
        try {
            this.file_id=json.getInt("file_id");
            this.order_id=json.getString("order_id");
            this.client_name=json.getString("client_name");
            this.order_time=json.getString("order_time");
           this.delivery_time=json.getString("delivery_time"); //交货时间
            this.end_time=json.getString("end_time");
            this.modify_time=json.getString("modify_time");
           this.file_path=json.getString("file_path"); //文件路径
            this.schedule=json.getString("schedule");
            this.explain=json.getString("explain");
            this.remark=json.getString("remark");
           this.order_name=json.getString("order_name");
            this.locked=json.getInt("locked");
            this.edition=json.getInt("edition");
            this.edit_time=json.getString("edit_time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public HashMap<String, String> towait(int role_id,int user_id ) {
        // TODO Auto-generated method stub
        HashMap<String, String> map=new HashMap<String, String>();
        map.put("method","getOrderListById");
        map.put("role_id",role_id+"");
        map.put("user_id",user_id+"");
        return map;
    }


    public HashMap<String, String> toEditorJson() {
        // TODO Auto-generated method stub
        HashMap<String, String> map=new HashMap<String, String>();
        map.put("method","GetEditorList");
        return map;
    }


    public HashMap<String, String> lockfile(String orderid) {
        // TODO Auto-generated method stub
        HashMap<String, String> map=new HashMap<String, String>();
        map.put("method","download");
        map.put("order_id",orderid);
        return map;
    }

    public HashMap<String, String> searchJson(String ordernum) {
        // TODO Auto-generated method stub
        HashMap<String, String> map=new HashMap<String, String>();
        map.put("method","search");
        map.put("order_name",ordernum);
        return map;
    }



    public HashMap<String,String> toCompeile(String orderid){
        // TODO Auto-generated method stub
        HashMap<String, String> map=new HashMap<String, String>();
        map.put("method","getOrderHis");
        map.put("order_id",orderid);
        return map;
    }

    public HashMap<String ,String> FileLocked(String orderid){
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("method","UnLockOrder");
        map.put("order_id",orderid);
        return map;
    }
    public HashMap<String ,String> FileLocked(String orderid,String userid){
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("method","ReviewOrder");
        map.put("order_id",orderid);
        map.put("user_id",userid);
        return map;
    }
    public HashMap<String,String> FileLocked(String order_id, int userId, String edittime) {
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("method","withdraw");
        map.put("order_id",order_id);
        map.put("user_id",userId+"");
        map.put("edit_time",edittime);
        return map;

    }

    public String getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(String edit_time) {
        this.edit_time = edit_time;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getFile_path() {
        return file_path;
    }
    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getClient_name() {
        return client_name;
    }
    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }
    public String getOrder_time() {
        return order_time;
    }
    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }
    public String getDelivery_time() {
        return delivery_time;
    }
    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public int getFile_id() {
        return file_id;
    }
    public void setFile_id(int file_id) {
        this.file_id = file_id;
    }
    public int getOrder_state() {
        return order_state;
    }
    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }
    public int getLocked(){return locked;}
    public int getEdition() {
        return edition;
    }
    public void setEdition(int edition) {
        this.edition = edition;
    }
    public String getSchedule() {
        return schedule;
    }
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    public String getExplain() {
        return explain;
    }
    public void setExplain(String explain) {
        this.explain = explain;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEnd_time() {
        return end_time;
    }
    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public void totalSet(String name, String value) {
        switch(name){
            case "client_name":
                this.client_name = (String) value;
                break;
            case "order_time":
                this.order_time = (String) value;
                break;
            case "end_time":
                this.end_time = (String) value;
                break;
            case "delivery_time":
                this.delivery_time = (String) value;
                break;
            case "explain" :
                this.explain = (String) value;
                break;
            case "remark" :
                this.remark = (String) value;
                break;
            default :
                break;
        }
    }



}
