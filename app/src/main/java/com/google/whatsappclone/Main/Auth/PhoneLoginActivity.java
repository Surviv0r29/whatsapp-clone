package com.google.whatsappclone.Main.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.whatsappclone.Main.MainActivity;
import com.google.whatsappclone.R;
import com.google.whatsappclone.databinding.ActivityPhoneLoginBinding;
import com.google.whatsappclone.model.user.User;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityPhoneLoginBinding binding;
    private FirebaseAuth mAuth;
    private final String TAG ="phone Activity";
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String VerificationId;
    String phone;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(savedInstanceState != null){
            onRestoreInstanceState(savedInstanceState);
        }
        firestore = FirebaseFirestore.getInstance();
       binding.nxtBttn.setOnClickListener(this);
       binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
           @Override
           public void onCountrySelected() {
               binding.countrycode.setText(binding.ccp.getSelectedCountryCodeWithPlus());
           }
       });
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG,"onVerification complete");
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                VerificationId = verificationId;
                mResendToken = token;
                binding.nxtBttn.setText("confirm");
                progressDialog.dismiss();
            }
        };
    }

    private void startPhoneVerification(String phoneNumber){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            assert user != null;
                            startActivity(new Intent(PhoneLoginActivity.this, UserDataActivity.class));


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                // [END_EXCLUDE]
                            }
                        }
                    }
                });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.nxt_bttn) {
           if (binding.nxtBttn.getText().toString().equals("Next")) {
               progressDialog.setMessage("please wait!!");
               progressDialog.show();
               phone = binding.countrycode.getText().toString() + binding.phoneNumber.getText().toString();
               startPhoneVerification(phone);
           }else {
               progressDialog.setMessage("verifying...");
               progressDialog.show();
               verifyPhoneNumberWithCode(VerificationId,binding.code.getText().toString());
           }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser =mAuth.getCurrentUser();
        /*if(firebaseUser!= null){
            startActivity(new Intent(PhoneLoginActivity.this, MainActivity.class));

        }*/
    }
}