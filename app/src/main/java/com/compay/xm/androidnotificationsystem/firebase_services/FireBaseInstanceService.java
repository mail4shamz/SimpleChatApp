package com.compay.xm.androidnotificationsystem.firebase_services;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by vivek on 21/9/16.
 */
public class FireBaseInstanceService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        String firebaseId=FirebaseInstanceId.getInstance().getId();
        Log.e(getClass().getSimpleName(),"FirebaseId "+firebaseId+" Refresh Token"+refreshToken);
    }
    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}
