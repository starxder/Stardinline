package com.example.starxder.stardinline.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.starxder.stardinline.Beans.Order;
import com.example.starxder.stardinline.R;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class OrderAdapter extends ArrayAdapter<Order> {

    private int resourId;

    public OrderAdapter(Context context, int textViewResourceId, List<Order> objects) {
        super(context, textViewResourceId, objects);
        resourId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourId,null);
        TextView order_num = (TextView)view.findViewById(R.id.tv_order_num);
        TextView time = (TextView)view.findViewById(R.id.tv_time);
        order_num.setText(order.getOrdertype()+order.getOrdername());
        time.setText(order.getOrdertime());

        return view;
    }
}
