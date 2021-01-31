package com.google.whatsappclone.Main.Start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.whatsappclone.Main.Auth.PhoneLoginActivity;
import com.google.whatsappclone.Main.MainActivity;
import com.google.whatsappclone.Main.chats.ChatsActivity;
import com.google.whatsappclone.R;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        findViewById(R.id.agree).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  startActivity(new Intent(WelcomeScreenActivity.this, PhoneLoginActivity.class));
            }
        });
    }
}