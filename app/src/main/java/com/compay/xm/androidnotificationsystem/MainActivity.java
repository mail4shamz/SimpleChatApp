package com.compay.xm.androidnotificationsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView mChatRoomNamelistView;
    private EditText mchatRoomName_editText;
    private Button maddchatroom_button;
    private ArrayAdapter chatRoomAdapter;
    private ArrayList<String> listOfChatRoom;
    private String userName;
    private DatabaseReference rootDatabaseReference = FirebaseDatabase.getInstance().getReference().getRoot();
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuthenticate;
    private FirebaseAuth.AuthStateListener mAuthenticationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        requestUserAcess();
        rootDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> nameSet = new HashSet<String>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    nameSet.add(((DataSnapshot) iterator.next()).getKey());
                }
                listOfChatRoom.clear();
                listOfChatRoom.addAll(nameSet);
                chatRoomAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuthenticate.addAuthStateListener(mAuthenticationListener);
    }

    private void initViews() {
        mChatRoomNamelistView = (ListView) findViewById(R.id.ChatRoomNamelistView);
        mchatRoomName_editText = (EditText) findViewById(R.id.chatRoomName_editText);
        maddchatroom_button = (Button) findViewById(R.id.addchatroom_button);
        maddchatroom_button.setOnClickListener(this);
        mChatRoomNamelistView.setOnItemClickListener(this);
        listOfChatRoom = new ArrayList<>();
        chatRoomAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, listOfChatRoom);
        mChatRoomNamelistView.setAdapter(chatRoomAdapter);
        // Firebase Authentication
        mAuthenticate=FirebaseAuth.getInstance();


    }

    private void requestUserAcess() {
        mAuthenticationListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent signUpIntent=new Intent(MainActivity.this,LoginActivity.class);
                    signUpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MainActivity.this.startActivity(signUpIntent);

                }

            }
        };





      /*  AlertDialog.Builder userDetailsAlertDialog = new AlertDialog.Builder(MainActivity.this);
        userDetailsAlertDialog.setCancelable(false);
        userDetailsAlertDialog.setTitle("Enter Details To get in");
        final EditText userdetailsEditText = new EditText(MainActivity.this);
        userDetailsAlertDialog.setView(userdetailsEditText);
        userDetailsAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userName = userdetailsEditText.getText().toString();
            }
        });
        userDetailsAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                requestUserAcess();


            }
        });
        userDetailsAlertDialog.show();*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addchatroom_button:
                Map<String, Object> mapObject = new HashMap<>();
                mapObject.put(mchatRoomName_editText.getText().toString(), "");
                rootDatabaseReference.updateChildren(mapObject);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent chatRoomIntent = new Intent(MainActivity.this, ChatRoomActivity.class);
        chatRoomIntent.putExtra("chatRoomName", ((TextView) view).getText().toString());
        chatRoomIntent.putExtra("userName", userName);
        this.startActivity(chatRoomIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.logout){
            LogoutUser();
        }
        return super.onOptionsItemSelected(item);
    }

    private void LogoutUser() {
        mAuthenticate.signOut();
    }
}
