package com.example.mrmohammad.protalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity{

    ListView listMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        listMsg = findViewById(R.id.myList);

        ArrayList<Message> listMessages;
        MessageListAdapter adapter;
        listMessages = new ArrayList<>();

        listMessages.add(new Message("Hello How R U??", true,  14, 17));
        listMessages.add(new Message("I'm fine", false, 54, 11));
        adapter = new MessageListAdapter(this, listMessages);
        listMsg.setAdapter(adapter);
    }
}
