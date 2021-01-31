package com.google.whatsappclone.Main.chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.google.whatsappclone.Main.manager.chats.ChatsServices;
import com.google.whatsappclone.Main.manager.interfaces.OnReadCallBack;
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
    int IMAGE_GALLERY_REQUEST =111 ;
    private ActivityChatsBinding binding;
     private String recieveID;
    private ChatAdapter adapter;
    private List<Chats> list = new ArrayList<>();
    private ChatsServices services;
    private boolean isActionstate = false;
    private Uri imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chats);
        initialize();
        readChats();
    }

    private void initialize(){

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
       services = new ChatsServices(this,recieveID);
       binding.file.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(isActionstate){
                  binding.layoutActions.setVisibility(View.GONE);
                  isActionstate=false;
               }else{
                   binding.layoutActions.setVisibility(View.VISIBLE);
                   isActionstate=true;
               }
           }
       });
       binding.gallery.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               opengallery();
           }
       });
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
       adapter=new ChatAdapter(list,this);
       binding.recyclerView2.setAdapter(adapter);

   }

    private void opengallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image"),IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==IMAGE_GALLERY_REQUEST
            && requestCode ==RESULT_OK
             && data!=null
              &&data.getData()!=null){

            imageuri = data.getData();

        }
        uploadtoFirebase();
    }

    private void uploadtoFirebase() {
    }

    private void readChats() {
      services.readChatdata(new OnReadCallBack() {
          @Override
          public void onReadSuccess(List<Chats> list) {
              adapter.setList(list);
          }

          @Override
          public void onReadFailed() {
              Log.d("on success:","failed");
          }
      });
    }

    private void initBtnclick(){
        try {
            if(!TextUtils.isEmpty(binding.tvmessage.getText().toString())){
                services.sendTextmessages(binding.tvmessage.getText().toString());
                binding.tvmessage.setText("");
            }

        }catch (Exception e){
            binding.tvmessage.setText("");
            binding.sendbttn.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_24));
        }


    }
    public void setImage(){
        
    }

}