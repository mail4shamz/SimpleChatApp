package com.compay.xm.androidnotificationsystem.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.compay.xm.androidnotificationsystem.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {
    private TextView mTextViewChatRoomName;
    private TextView mTextViewChatRoomMessage;
    private EditText mEditTextUserMessage;
    private ImageButton mButtonSendButton;
    private String userName, chatRoomName;
    private String uniqueKey;
    private DatabaseReference rootDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        initViews();
        rootDatabaseReference = FirebaseDatabase.getInstance().getReference().child(chatRoomName);
        mButtonSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mEditTextUserMessage.getText().toString())){
                    Map<String, Object> mapObject = new HashMap<String, Object>();
                    uniqueKey = rootDatabaseReference.push().getKey();
                    rootDatabaseReference.updateChildren(mapObject);
                    DatabaseReference messageRoot = rootDatabaseReference.child(uniqueKey);
                    Map<String, Object> userNameAndMessageMap = new HashMap<String, Object>();
                    userNameAndMessageMap.put("userName", userName);
                    userNameAndMessageMap.put("sendMessage", mEditTextUserMessage.getText().toString().trim());
                    messageRoot.updateChildren(userNameAndMessageMap);
                    mEditTextUserMessage.setText("");
                }
                else {
                    Toast.makeText(ChatRoomActivity.this, "Please Enter Name", Toast.LENGTH_LONG).show();
                }

            }
        });
        rootDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                appendChatConvesation(dataSnapshot);
            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatConvesation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void appendChatConvesation(DataSnapshot dataSnapshot) {
        String userName,userMessage;
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()){
            userName= (String) ((DataSnapshot)iterator.next()).getValue();
            userMessage= (String) ((DataSnapshot)iterator.next()).getValue();
            mTextViewChatRoomMessage.append(userMessage+" : " +userName+"\n\n");
        }
    }

    private void initViews() {
        mTextViewChatRoomName = (TextView) findViewById(R.id.textViewRoomName);
        mTextViewChatRoomMessage = (TextView) findViewById(R.id.textViewChatMessage);
        mEditTextUserMessage = (EditText) findViewById(R.id.send_message_editText);
        mButtonSendButton = (ImageButton) findViewById(R.id.send_message_button);
//=============other Values=====================================//
        userName = getIntent().getExtras().getString("userName");
        chatRoomName = getIntent().getExtras().getString("chatRoomName");
        mTextViewChatRoomName.setText("Room Name: " + chatRoomName);
        setTitle("Room Name: " + chatRoomName);

    }
}
