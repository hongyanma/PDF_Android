package com.com.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class SetFileorder {

    private List<Fileorder> FileorderList = new ArrayList<Fileorder>();

    public SetFileorder(List<Fileorder> Fileorderlist){
        this.FileorderList = Fileorderlist;
    }


    public int getSize(){
        return FileorderList.size();
    }


    public List<Fileorder> getFileorderList() {
        return FileorderList;
    }


    public void setFileorderList(List<Fileorder> FileorderList) {
        FileorderList = FileorderList;
    }


    public Fileorder getItem(int arg0){
        return FileorderList.get(arg0);
    }

}
