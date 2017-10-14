package com.com.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.com.entity.SetFileorder;
import com.com.entity.User;
import com.com.tool.SPUtils;
import com.example.jammy.pdf_demo.R;
import com.example.jammy.pdf_demo.SearchActivity;

import java.util.List;

import static com.example.jammy.pdf_demo.R.id.btn_look;

/**
 * Created by Administrator on 2017/7/13.
 */

public class SearchAdapter extends BaseAdapter {


    private Context context;
    private SetFileorder list;
    private String role_id;
    private List<User> editorlist;; //获得编辑人列表
    public SearchAdapter(Context c, SetFileorder l,String role_id){
        super();
        this.context=c;
        this.list =l;
        this.role_id=role_id;
    }

    class ViewHolder{

        private   TextView   tv_orderNum;//订单号
        private   TextView   tv_orderSpeed; //订单进度
        private   TextView   tv_ReciveorderTime;//接单时间
        private   TextView   tv_orderTime;//截止时间
        private   TextView   tv_orderExplain;//订单说明
        private   TextView   tv_ordermark; //订单备注
        private   Button     btn_look;
        private   TextView tv_pulltime;
        private   TextView tv_waityou;  //订单标题
        private  TextView tv_client_name;//客户名称
        private  TextView locked;
        private  TextView tv_ordertime;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.getSize();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }




    @Override
    public View getView(final int position, View convertView,final  ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewholder =null;

        if(convertView==null){

            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_search, null);
            viewholder = new ViewHolder();
            viewholder.tv_pulltime=(TextView)convertView.findViewById(R.id.tv_pulltime);
            viewholder.tv_orderNum=(TextView)convertView.findViewById(R.id.tv_orderNum);
            viewholder.tv_orderSpeed=(TextView)convertView.findViewById(R.id.tv_orderSpeed);
            viewholder.tv_ReciveorderTime=(TextView)convertView.findViewById(R.id.tv_ReciveorderTime);
            viewholder.tv_orderTime=(TextView)convertView.findViewById(R.id.tv_orderTime);
            viewholder.tv_orderExplain=(TextView)convertView.findViewById(R.id.tv_orderExplain);
            viewholder.tv_ordermark=(TextView)convertView.findViewById(R.id.tv_ordermark);
            viewholder.btn_look=(Button) convertView.findViewById(btn_look);
            viewholder.tv_waityou=(TextView)convertView.findViewById(R.id.tv_waityou);
            viewholder.tv_client_name=(TextView)convertView.findViewById(R.id.tv_client_name);
            viewholder.locked=(TextView)convertView.findViewById(R.id.locked);
            viewholder.tv_ordertime= (TextView) convertView.findViewById(R.id.tv_ordertime);
            convertView.setTag(viewholder);
        }

        else{
            viewholder = (ViewHolder) convertView.getTag();
        }
        if(role_id.equals("4")){
            viewholder.btn_look.setText("浏览");
        }else{



            if (SPUtils.get(context, "KEY", 0).equals(1) && position==0){
                viewholder.btn_look.setText("编辑");
            }




            if(list.getItem(0).getSchedule().equals("2")){
                viewholder.btn_look.setText("浏览");
            }else {
                if (list.getItem(position).getLocked() == 0) {
                    viewholder.btn_look.setText("编辑");
                } else {
                    viewholder.btn_look.setText("浏览");
                }
            }

        }


            if (list.getItem(0).getSchedule().equals("2")) {
                viewholder.locked.setText("终审完成");
            } else {
                if (list.getItem(position).getLocked() == 1) {
                    viewholder.locked.setText("已锁定");
                } else {
                    viewholder.locked.setText("未被锁定");
                }
            }

        if(list.getItem(position).getSchedule()=="0"){
            viewholder.tv_orderSpeed.setText("已录入待编辑");
        }else if(list.getItem(position).getSchedule()=="1"){
            viewholder.tv_orderSpeed.setText("可编辑");
        }else if(list.getItem(position).getSchedule()=="2"){
            viewholder.tv_orderSpeed.setText("已终审完成");
        }

        viewholder.tv_orderNum.setText(list.getItem(position).getOrder_name()+"");
        viewholder.tv_client_name.setText(list.getItem(position).getClient_name());
        viewholder.tv_ReciveorderTime.setText(list.getItem(position).getOrder_time()+"");
        viewholder.tv_orderTime.setText(list.getItem(position).getEnd_time()+"");
        viewholder.tv_orderExplain.setText(list.getItem(position).getExplain()+"");
        viewholder.tv_ordermark.setText(list.getItem(position).getRemark());
        viewholder.tv_pulltime.setText(list.getItem(position).getDelivery_time());
        viewholder.tv_ordertime.setText(list.getItem(position).getModify_time());
        viewholder.btn_look.setOnClickListener(new View.OnClickListener() {
                                                       @Override
            public void onClick(View v) {
                mOnItemDeleteListener.onDeleteClick(position);
            }
        });


        return convertView;

    }

    /**
     * 删除按钮的监听接口
     */
    public interface onItemDeleteListener {
        void onDeleteClick(int i);

    }
    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }
}
