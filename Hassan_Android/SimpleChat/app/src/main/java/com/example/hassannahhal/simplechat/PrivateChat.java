package com.example.hassannahhal.simplechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hassannahhal on 2015-11-20.
 */
public class PrivateChat extends Activity {

    private EditText etMessage;
    private Button btSend;
    private ListView lvChat;

    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;

    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private static final String PARSE_CREATED_AT = "createdAt";
    private static final String PARSE_TO = "To";
    private static final String PARSE_PRIVATE = "Private";
    private static final String PARSE_ID = "Id";
    private static final String USER_ID = "UserID";
    private static final String TO_USER_ID = "ToUserID";
    private static final int TIME_INTERVAL = 100;


    private static String sUserId, toUserId;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_chat);

        Intent intent = getIntent();
        sUserId = intent.getStringExtra(USER_ID);
        toUserId = intent.getStringExtra(TO_USER_ID);

        refreshMessages();
        setupMessagePosting();
    }


    // Setup message field and posting
    private void setupMessagePosting() {
        etMessage = (EditText) findViewById(R.id.privateChatetMessage);
        btSend = (Button) findViewById(R.id.privateChatbtSend);

        mMessages = new ArrayList<Message>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat = (ListView) findViewById(R.id.privateChatlv);
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        mAdapter = new ChatListAdapter(PrivateChat.this, sUserId, mMessages);
        lvChat.setAdapter(mAdapter);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                // Use Message model to create new messages now
                Message message = new Message();
                message.setId(sUserId);
                message.setMessageText(body);
                message.setPrivateKey(true);
                message.setToUser(toUserId);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        receiveMessage();
                    }
                });
                etMessage.setText("");
            }
        });
    }

    // Defines a runnable which is run every 100ms
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, TIME_INTERVAL);
        }
    };


    private void refreshMessages() {
        receiveMessage();
    }

    // Query messages from Parse so we can load them into the chat adapter
    private void receiveMessage() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class).whereEqualTo(PARSE_PRIVATE, true);
        query.whereContains(PARSE_ID, sUserId);
        query.whereContains(PARSE_TO, toUserId);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending(PARSE_CREATED_AT);
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                        refreshMessages();
                    }
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }
}
