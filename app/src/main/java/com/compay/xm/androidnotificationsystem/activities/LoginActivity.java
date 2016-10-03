package com.compay.xm.androidnotificationsystem.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.compay.xm.androidnotificationsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextView textViewforgotPassword;
    private EditText mEditTextUserEmail;
    private EditText mEditTextUserPassword;
    private Button mButtonSignIp;
    private Button bottonGoToSignUp;
    private FirebaseAuth firebaseAuthSignIn;
    private String UserEmail;
    private String UserPassword;
    private String UserName;
    private DatabaseReference databaseReferenceforUserExists;
    private String emailAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setTitle("Log In");
        mButtonSignIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
        bottonGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                signUpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                LoginActivity.this.startActivity(signUpIntent);

            }
        });
        textViewforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPasswodAPI();

            }
        });
    }

    private void ForgotPasswodAPI() {
        final AlertDialog.Builder userDetailsAlertDialog = new AlertDialog.Builder(LoginActivity.this);
        userDetailsAlertDialog.setCancelable(false);
        userDetailsAlertDialog.setTitle("Enter Registered Email");
        final EditText userdetailsEditText = new EditText(LoginActivity.this);
        userDetailsAlertDialog.setView(userdetailsEditText);
        userDetailsAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface OkButton, int i) {
                emailAddress = userdetailsEditText.getText().toString();
                if (!TextUtils.isEmpty(emailAddress)) {
                    Log.e("LogIn","If Condition");
                    FirebaseForgotPasswordAPI(userDetailsAlertDialog);
                    OkButton.dismiss();

                }

            }
        });
        userDetailsAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        userDetailsAlertDialog.create();
        userDetailsAlertDialog.show();
    }

    private void FirebaseForgotPasswordAPI(final AlertDialog.Builder userDetailsAlertDialog) {
        firebaseAuthSignIn.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Email has been sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    userDetailsAlertDialog.show();

                }
            }
        });
    }

    private void initViews() {
        mEditTextUserEmail = (EditText) findViewById(R.id.editTextEmail);
        mEditTextUserPassword = (EditText) findViewById(R.id.editTextPassword);
        mButtonSignIp = (Button) findViewById(R.id.buttonSignIn);
        textViewforgotPassword = (TextView) findViewById(R.id.textViewforgotPassword);
        bottonGoToSignUp = (Button) findViewById(R.id.buttonGoToSignUp);
        firebaseAuthSignIn = FirebaseAuth.getInstance();
      /*  UserName = getIntent().getExtras().get("userName").toString();*/
        databaseReferenceforUserExists = FirebaseDatabase.getInstance().getReference().child("signedUpUsers");
    }

    private void LoginUser() {
        UserEmail = mEditTextUserEmail.getText().toString().trim();
        UserPassword = mEditTextUserPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(UserEmail) && !TextUtils.isEmpty(UserPassword)) {
            firebaseAuthSignIn.signInWithEmailAndPassword(UserEmail, UserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        CheckUserExists();
                    } else {
                        Toast.makeText(LoginActivity.this, " User Does Not Exists", Toast.LENGTH_LONG).show();

                    }

                }
            });
        } else {

        }


    }

    private void CheckUserExists() {
        final String UserID = firebaseAuthSignIn.getCurrentUser().getUid();
        databaseReferenceforUserExists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(UserID)) {
                    Intent signUpIntent = new Intent(LoginActivity.this, MainActivity.class);
                    signUpIntent.putExtra("userName", UserName);
                    signUpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    LoginActivity.this.startActivity(signUpIntent);
                } else {
                    Toast.makeText(LoginActivity.this, " Please Create Account", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
