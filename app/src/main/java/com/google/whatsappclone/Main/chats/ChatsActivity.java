package com.google.whatsappclone.Main.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.whatsappclone.R;
import com.google.whatsappclone.adapter.ChatAdapter;
import com.google.whatsappclone.databinding.ActivityChatsBinding;
import com.google.whatsappclone.model.chat.Chats;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.SimpleTimeZone;

public class ChatsActivity extends AppCompatActivity {
    private ActivityChatsBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String recieveID;
    private ChatAdapter adapter;
    private List<Chats> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chats);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        recieveID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String userProfile = intent.getStringExtra("userProfile");


        binding.backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(recieveID!=null){
            binding.username.setText(userName);
            Glide.with(this).load(userProfile).into(binding.imageProfile);

        }
        binding.backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        binding.sendbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBtnclick();

            }
        });

        binding.tvmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              if(TextUtils.isEmpty(binding.tvmessage.getText().toString())){
                  binding.sendbttn.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_24));
              }else{
                  binding.sendbttn.setImageDrawable(getDrawable(R.drawable.ic_baseline_send_24));
              }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView2.setLayoutManager(layoutManager);
        binding.recyclerView2.setHasFixedSize(false);
        readChats();
    }

    private void readChats() {
        try {
            DatabaseReference Reference = FirebaseDatabase.getInstance().getReference();
            Reference.child("Chats").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                        Chats chats = datasnapshot.getValue(Chats.class);

                        if(chats!=null && chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(recieveID)
                        || Objects.requireNonNull(chats).getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(recieveID)){
                            list.add(chats);
                        }
                    }
                    if (adapter != null) {
                        
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter = new ChatAdapter(list, ChatsActivity.this);
                        binding.recyclerView2.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
          e.printStackTrace();
        }
    }

    private void initBtnclick(){
        if(!TextUtils.isEmpty(binding.tvmessage.getText().toString())){
            sendTextMessage(binding.tvmessage.getText().toString());
            binding.tvmessage.setText("");
        }

    }

    private void sendTextMessage(String message) {

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String today = format.format(date);

        Calendar currentDate =  Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String currentTime = df.format(currentDate.getTime());

        Chats chats = new Chats(today+""+currentTime,
                message,
                firebaseUser.getUid(),
                recieveID,
                "TEXT"
                );

        reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("send","onSucess:");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("send","onFail:"+e.getMessage());
            }
        });
        //Add to chatlist
        DatabaseReference chatref1 = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(recieveID);
        chatref1.child("chatid").setValue(recieveID);

        DatabaseReference chatref2 = FirebaseDatabase.getInstance().getReference("ChatList").child(recieveID).child(firebaseUser.getUid());
        chatref2.child("chatid").setValue(firebaseUser.getUid());

    }

    @Override
    protected void onStart() {
        super.onStart();
        readChats();
    }

}