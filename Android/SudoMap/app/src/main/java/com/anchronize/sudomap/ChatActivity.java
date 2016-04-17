package com.anchronize.sudomap;

import android.app.ListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Rohan on 4/13/16.
 */

public class ChatActivity extends ListActivity {

    private ArrayList<String> data = new ArrayList<String>();
    private static final String FIREBASE_URL = "https://anchronize.firebaseio.com";
    public static final String CHAT_KEY = "SUUUH";
    private String mUsername;
    private String mDescription;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_message);
        //get the eventID
        Intent i = getIntent();
        String eventID =  i.getStringExtra(CHAT_KEY);


        //Set up firebase reference
        //this should be changed to be under each event
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat").child(eventID);

        //TEST
        mDescription = "mDescription.";
        //main post is replaced by description
        TextView description = (TextView) findViewById(R.id.main_post);

        //set up input text field and send message button listener
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        //add listadapter to listview
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.list_item, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });


    }



    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, mUsername);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            Firebase mPostRef = mFirebaseRef.push();
            //mChatListAdapter.setReference(mPostRef.child("votes"));
            mPostRef.setValue(chat);
            inputText.setText("");
        }
    }
}
