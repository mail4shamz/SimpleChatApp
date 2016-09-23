package com.compay.xm.androidnotificationsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class SignUpActivity extends AppCompatActivity {
    private EditText mEditTextUserName;
    private EditText mEditTextUserEmail;
    private EditText mEditTextUserPassword;
    private Button mButtonSignUp;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String firebaseUserId;
    private FirebaseAuth firebaseAuthSignUp;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference signUpUsersDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up Now");
        signUpUsersDatabaseReference= FirebaseDatabase.getInstance().getReference().child("signedUpUsers");
        firebaseAuthSignUp=FirebaseAuth.getInstance();
        initViews();
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();


            }
        });
    }

    private void RegisterUser() {
        userName = mEditTextUserName.getText().toString();
        userEmail = mEditTextUserEmail.getText().toString();
        userPassword = mEditTextUserPassword.getText().toString();
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword)) {
            firebaseAuthSignUp.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Log.e("SignUpActivity","Success   >>>>>> "+userName);
                        firebaseUserId=firebaseAuthSignUp.getCurrentUser().getUid();
                        DatabaseReference currentUserDatabase=signUpUsersDatabaseReference.child(firebaseUserId);
                        currentUserDatabase.child("name").setValue(userName);
                        Intent signUpIntent=new Intent(SignUpActivity.this,MainActivity.class);
                        signUpIntent.putExtra("userName",userName);
                        signUpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SignUpActivity.this.startActivity(signUpIntent);
                    }else {

                        task.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("SignUpActivity","Failed   >>>>>> "+userName + e.getMessage());

                            }
                        });
                    }

                }
            });
        } else {

        }
        Log.e("SignUpActivity","UserName "+userName);
        Log.e("SignUpActivity","UserEmail "+userEmail);
        Log.e("SignUpActivity","UserPassword "+userPassword);



    }

    private void initViews() {
        mEditTextUserName = (EditText) findViewById(R.id.editTextUserName);
        mEditTextUserEmail = (EditText) findViewById(R.id.editTextEmail);
        mEditTextUserPassword = (EditText) findViewById(R.id.editTextPassword);
        mButtonSignUp = (Button) findViewById(R.id.buttonSignUp);


    }
}
