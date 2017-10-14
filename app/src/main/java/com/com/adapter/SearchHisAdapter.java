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
import com.example.jammy.pdf_demo.R;


import static com.example.jammy.pdf_demo.R.id.btn_look;

/**
 * Created by Administrator on 2017/9/24.
 */

public class SearchHisAdapter extends BaseAdapter {

    private Context context;
    private SetFileorder list;

    public SearchHisAdapter(Context c, SetFileorder l) {
        super();
        this.context = c;
        this.list = l;
    }

    class ViewHolder {

        private TextView tv_orderNum;//订单号
        private TextView tv_orderSpeed; //订单进度
        private TextView tv_ReciveorderTime;//接单时间
        private TextView tv_orderTime;//截止时间
        private TextView tv_orderExplain;//订单说明
        private TextView tv_ordermark; //订单备注
        private Button btn_look;
        private Button btn_final;
        private TextView tv_pulltime;
        private TextView tv_waityou;  //订单标题
        private TextView tv_client_name;//客户名称
        private TextView locked;
        private TextView tv_ordertime;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewholder = null;

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_searchhis, null);
            viewholder = new ViewHolder();
            viewholder.tv_pulltime = (TextView) convertView.findViewById(R.id.tv_pulltime);
            viewholder.tv_orderNum = (TextView) convertView.findViewById(R.id.tv_orderNum);
            viewholder.tv_orderSpeed = (TextView) convertView.findViewById(R.id.tv_orderSpeed);
            viewholder.tv_ReciveorderTime = (TextView) convertView.findViewById(R.id.tv_ReciveorderTime);
            viewholder.tv_orderTime = (TextView) convertView.findViewById(R.id.tv_orderTime);
            viewholder.tv_orderExplain = (TextView) convertView.findViewById(R.id.tv_orderExplain);
            viewholder.tv_ordermark = (TextView) convertView.findViewById(R.id.tv_ordermark);
            viewholder.btn_look = (Button) convertView.findViewById(btn_look);
            viewholder.tv_waityou = (TextView) convertView.findViewById(R.id.tv_waityou);
            viewholder.tv_client_name = (TextView) convertView.findViewById(R.id.tv_client_name);
            viewholder.locked = (TextView) convertView.findViewById(R.id.locked);
            viewholder.btn_final = (Button) convertView.findViewById(R.id.btn_final);
            viewholder.tv_ordertime= (TextView) convertView.findViewById(R.id.tv_ordertime);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }


            if(list.getItem(0).getSchedule().equals("2")){
                viewholder.btn_look.setText("浏览");
                viewholder.btn_final.setVisibility(View.GONE);
                Log.i("tag","aaa"+list.getItem(0).getSchedule());
            }else {
                if (list.getItem(position).getEdition() == 0) {
                    viewholder.btn_look.setText("浏览");
                } else {

                    if (list.getItem(position).getLocked() == 0) {
                        viewholder.btn_look.setText("编辑");
                    } else {
                        viewholder.btn_look.setText("浏览");
                    }
                }
            }


        if(list.getItem(position).getSchedule()=="0"){
            viewholder.tv_orderSpeed.setText("已录入待编辑");
        }else if(list.getItem(position).getSchedule()=="1"){
            viewholder.tv_orderSpeed.setText("可编辑");
        }else if(list.getItem(position).getSchedule()=="2"){
            viewholder.tv_orderSpeed.setText("已终审完成");
        }


        Log.i("TAG", "kkkk" + list.getItem(position).getOrder_id());


            if (list.getItem(0).getSchedule().equals("2")){
                viewholder.locked.setText("终审完成");
                viewholder.btn_final.setVisibility(View.INVISIBLE);
            }else {
                if (list.getItem(position).getEdition() == 1) {
                    if (list.getItem(position).getLocked() == 1) {
                        viewholder.locked.setText("已锁定");
                    } else {
                        viewholder.locked.setText("未被锁定");
                    }
                    viewholder.btn_final.setText("终审订单");
                } else {
                    viewholder.btn_final.setText("退至订单");
                    viewholder.locked.setText("历史订单");
                }
            }



        viewholder.tv_orderNum.setText(list.getItem(position).getOrder_name() + "");
        viewholder.tv_client_name.setText(list.getItem(position).getClient_name());
        viewholder.tv_ReciveorderTime.setText(list.getItem(position).getOrder_time() + "");
        viewholder.tv_orderTime.setText(list.getItem(position).getEnd_time() + "");
        viewholder.tv_orderExplain.setText(list.getItem(position).getExplain() + "");
        viewholder.tv_ordertime.setText(list.getItem(position).getModify_time());
        viewholder.tv_ordermark.setText(list.getItem(position).getRemark());
        viewholder.tv_pulltime.setText(list.getItem(position).getDelivery_time());
        viewholder.btn_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDeleteListener.onDeleteClick(position);
            }
        });

        viewholder.btn_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monItemfinalListener.onfinal(position);

            }
        });


        return convertView;

    }

    /**
     * 终审存档按钮
     */
    public interface onItemfinalListener {
        void onfinal(int i);
    }

    private onItemfinalListener monItemfinalListener;


    public void setOnItemfinalClickListener(onItemfinalListener mOnItemfinalListener) {
        this.monItemfinalListener = mOnItemfinalListener;
    }

    /**
     * 编辑按钮的监听接口
     */
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }
}


