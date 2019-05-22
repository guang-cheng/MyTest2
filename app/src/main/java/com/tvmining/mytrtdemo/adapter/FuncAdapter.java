package com.tvmining.mytrtdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tvmining.mytrtdemo.R;
import com.tvmining.mytrtdemo.UserInfo;

import java.util.List;

/**
 * Created by xkazerzhang on 2017/6/22.
 */
public class FuncAdapter extends BaseAdapter {
    private Context context;
    private List<UserInfo> listFuncs;

    private class ViewHolder{
        TextView tvRoomInfo;
    }


    public FuncAdapter(Context ctx, List<UserInfo> list){
        context = ctx;
        listFuncs = list;
    }

    public void updateList(List<UserInfo> rooms){
        listFuncs = rooms;
    }

    @Override
    public int getCount() {
        return listFuncs.size();
    }

    @Override
    public Object getItem(int i) {
        return listFuncs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder)convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_menulist, null);

            holder = new ViewHolder();
            holder.tvRoomInfo = (TextView) convertView.findViewById(R.id.tv_msg);

            convertView.setTag(holder);
        }

        UserInfo info = listFuncs.get(position);
        if (null != info){
            holder.tvRoomInfo.setText("用户"+(position+1)+":"+info.getUserId());
        }

        return convertView;
    }
}
