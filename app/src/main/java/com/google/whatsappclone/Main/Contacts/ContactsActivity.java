package com.google.whatsappclone.Main.Contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.whatsappclone.R;
import com.google.whatsappclone.adapter.ContactAdapter;
import com.google.whatsappclone.databinding.ActivityContactsBinding;
import com.google.whatsappclone.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding binding;
    private List<User> list =  new ArrayList<>();
    private ContactAdapter Adapter;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView( this,R.layout.activity_contacts);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore =  FirebaseFirestore.getInstance();
        
        if(firebaseUser!=null){
            getContacts();
        }
        
    }

    private void getContacts() {
        firestore.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snapshots : queryDocumentSnapshots){
                            String userName =snapshots.getString("userName");
                            String userID =snapshots.getString("userId");
                            //String imageurl =snapshots.getString("imageProfile");
                           // String imageurl = "https://thumbs.dreamstime.com/b/profile-anonymous-face-icon-gray-silhouette-person-male-businessman-profile-default-avatar-photo-placeholder-isolated-white-117831744.jpg";
                            String desc = snapshots.getString("Bio");


                            User user = new User();
                            user.setUserId(userID);
                            user.setUserName(userName);
                            user.setBio(desc);
                            //user.setImageProfile(imageurl);

                            assert userID != null;
                            if(!userID.equals(firebaseUser.getUid()))
                            list.add(user);

                        }

                        Adapter = new ContactAdapter(list,ContactsActivity.this);
                        binding.recyclerView.setAdapter(Adapter);
                    }
                });
    }


}