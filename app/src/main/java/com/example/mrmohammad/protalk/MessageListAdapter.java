package com.example.mrmohammad.protalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messagesItems;

    public MessageListAdapter(Context context, List<Message> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Message getItem(int position) {
        return messagesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message m = messagesItems.get(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (messagesItems.get(position).getMsgMe()) {
            convertView = mInflater.inflate(R.layout.right_chat_layout,
                    null);
        } else {
            convertView = mInflater.inflate(R.layout.left_chat_layout,
                    null);
        }

        TextView lblFrom =     convertView.findViewById(R.id.lblMsgFrom);
        TextView txtMsg =  convertView.findViewById(R.id.txtMsg);
        txtMsg.setText(m.getMessage());
        lblFrom.setText("Time: " + m.getDate()+" - "+m.getHour());

        return convertView;
    }
}