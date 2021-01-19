package com.google.whatsappclone.Main.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.whatsappclone.Main.MainActivity;
import com.google.whatsappclone.Main.Start.WelcomeScreenActivity;
import com.google.whatsappclone.R;
import com.google.whatsappclone.databinding.ActivityUserDataBinding;
import com.google.whatsappclone.model.user.User;

import java.util.HashMap;
import java.util.Objects;


public class UserDataActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityUserDataBinding binding;
    private Boolean PRESENT = true;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        binding.profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 
            }
        });
      binding.button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button)
        {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            if(PRESENT) {
                doUpdate();
            }else{
                intender();
            }
        }
    }

    private void doUpdate() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(!Objects.requireNonNull(binding.info.getText()).toString().equals("")){
            assert firebaseUser != null;
            User users = new User(
                    firebaseUser.getUid(),
                    binding.info.getText().toString() ,
                    firebaseUser.getPhoneNumber() ,
                    "",
                    "",
                    ""
            );
            firebaseFirestore.collection("users").document(firebaseUser.getUid()).set(users)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //progressDialog.dismiss();
                                intender();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                 }
            });

        }else{
            binding.info.setError("username not given");
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    binding.info.setText(documentSnapshot.getString("userName"));
                    PRESENT = false;

                }
            }
        });

    }

    public void intender(){
        startActivity(new Intent(UserDataActivity.this, MainActivity.class));
        progressDialog.dismiss();
    }
}