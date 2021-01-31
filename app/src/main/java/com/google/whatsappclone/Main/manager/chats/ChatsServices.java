package com.google.whatsappclone.Main.manager.chats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.whatsappclone.Main.Contacts.ContactsActivity;
import com.google.whatsappclone.Main.chats.ChatsActivity;
import com.google.whatsappclone.Main.manager.interfaces.OnReadCallBack;
import com.google.whatsappclone.adapter.ChatAdapter;
import com.google.whatsappclone.model.chat.Chats;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class ChatsServices  {
    private Context context;
    private FirebaseUser firebaseUser;
    private DatabaseReference Reference = FirebaseDatabase.getInstance().getReference();
    private String recieveID;

    public ChatsServices(Context context, String recieveID) {
        this.context = context;
        this.recieveID = recieveID;
    }

    public void readChatdata(final OnReadCallBack onReadCallBack){
        Reference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Chats> list = new ArrayList<>();
                for (DataSnapshot datasnapshot : snapshot.getChildren())
                {
                    Chats chats = datasnapshot.getValue(Chats.class);
                    String ref =  datasnapshot.getKey().toString();
                    try {
                        if(chats!=null && chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(recieveID)
                                || Objects.requireNonNull(chats).getReceiver().equals(firebaseUser.getUid()) && chats.getSender().equals(recieveID)){
                            list.add(chats);

                            FirebaseDatabase.getInstance().getReference().child("Chats").child(ref).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "this was deleted", Toast.LENGTH_SHORT).show();
                                }
                            }) ;
                        }
                    }catch (Exception e){
                       e.printStackTrace();
                    }


                }
                onReadCallBack.onReadSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onReadCallBack.onReadFailed();
            }
        });
    }

    public void sendTextmessages(String text){
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String today = format.format(date);

        Calendar currentDate =  Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String currentTime = df.format(currentDate.getTime());

        Chats chats = new Chats(today+""+currentTime,
                text,
                firebaseUser.getUid(),
                recieveID,
                "TEXT"
        );

        Reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}
