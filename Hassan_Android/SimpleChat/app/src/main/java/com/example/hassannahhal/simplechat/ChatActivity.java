package com.example.hassannahhal.simplechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {
    private static String sUserId;
    private static String userReceiverId;


    private static final String TAG = ChatActivity.class.getName();
    private static final String PARSE_CREATED_AT = "createdAt";
    private static final String PARSE_ALL = "All";
    private static final String PARSE_USER_ID = "UserID";
    private static final String TO_USER_ID = "ToUserID";
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 300;
    private static final String MY_ID = "id";
    private static final int TIME_INTERVAL = 100;


    private EditText etMessage;
    private Button btSend;

    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;


    // Create a handler which can run code periodically
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        // Register your parse models here
        ParseObject.registerSubclass(Message.class);

        startWithCurrentUser();

        // Run the runnable object defined every 100ms
        handler.postDelayed(runnable, TIME_INTERVAL);


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


    // Setup message field and posting
    private void setupMessagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);

        mMessages = new ArrayList<Message>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        mAdapter = new ChatListAdapter(ChatActivity.this, sUserId, mMessages);
        lvChat.setAdapter(mAdapter);

        lvChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent privateChat = new Intent(ChatActivity.this, PrivateChat.class);
                privateChat.putExtra(PARSE_USER_ID, sUserId);
                userReceiverId = mMessages.get(position).getReceiverId();
                Log.d(userReceiverId, "" + userReceiverId);
                Log.d("bool value", "" + ((!userReceiverId.equals(null)) && (userReceiverId.equals(sUserId)) &&
                        (!userReceiverId.equals(PARSE_ALL)) && (userReceiverId.equals(""))));
                Log.d("valueeee", "" + (!userReceiverId.equals(PARSE_ALL)));


                if ((!userReceiverId.equals(null)) && (!userReceiverId.equals(sUserId)) &&
                        (!userReceiverId.equals(PARSE_ALL)) && (!userReceiverId.equals(""))) {
                    privateChat.putExtra(TO_USER_ID, userReceiverId);
                    startActivity(privateChat);
                } else {
                    Toast.makeText(ChatActivity.this, "User have null ID",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                // Use Message model to create new messages now
                Message message = new Message();
                message.setId(sUserId);
                message.setMessageText(body);
                message.setPrivateKey(false);
                message.setToUser(PARSE_ALL);
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


    // Query messages from Parse so we can load them into the chat adapter
    private void receiveMessage() {

        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
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
                    }
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }

    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        //sUserId = ParseUser.getCurrentUser().getObjectId();
        Intent intent = getIntent();
        sUserId = intent.getStringExtra(MY_ID);
        setupMessagePosting();

    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    private void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Anonymous login failed: " + e.toString());
                } else {
                    startWithCurrentUser();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
