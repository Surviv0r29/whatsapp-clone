package com.google.whatsappclone.Main.Start;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.whatsappclone.Main.Auth.UserDataActivity;
import com.google.whatsappclone.Main.MainActivity;
import com.google.whatsappclone.Main.chats.ChatsActivity;
import com.google.whatsappclone.R;

import java.util.Objects;

import static java.lang.Thread.sleep;

public class splash_screen extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        /*intent = new Intent(splash_screen.this, ChatsActivity.class);
        startActivity(intent);*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1000);

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user != null) {

                        intent = new Intent(splash_screen.this, MainActivity.class);
                    }else {
                        intent = new Intent(splash_screen.this, WelcomeScreenActivity.class);

                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });thread.start();
    }
}