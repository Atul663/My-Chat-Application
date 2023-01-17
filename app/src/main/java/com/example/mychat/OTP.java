package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {

    EditText otp;
    Button signIn;
    String userPhoneNumber;
    String otpId;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

//        Intent i =getIntent();
//        String UuserPhoneNumber = i.getStringExtra("mobile").toString();
        userPhoneNumber = "+917683959698";
        otp = findViewById(R.id.editTextOTP);
        signIn = findViewById(R.id.buttonSignIn);
        System.out.println("hiii");

        //getOtp();


       // String userPhoneNumber = phoneNumber.getText().toString();
//        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
//                .setPhoneNumber(userPhoneNumber)
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(OTP.this)
//                .setCallbacks(mCallbacks)
//                .build();
//
//        PhoneAuthProvider.verifyPhoneNumber(options);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signWithPhoneCode();

//                if(otp.getText().toString().isEmpty())
//                {
//                    Toast.makeText(OTP.this, "Please Enter the OTP", Toast.LENGTH_SHORT).show();
//                }
//                else if (otp.getText().toString().length()!=6)
//                {
//                    Toast.makeText(OTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId,otp.getText().toString());
//                    signInWithPhoneAuthCredential(credential);
//                }
            }
        });
    }
    public void signWithPhoneCode() {
        String userEnterCode = otp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId, userEnterCode);
        signInWithPhoneAuthCredential(credential);
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(OTP.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(OTP.this, "OTP don't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//        }
//
//        @Override
//        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            otpId = s;
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//
//        }
//    };
}