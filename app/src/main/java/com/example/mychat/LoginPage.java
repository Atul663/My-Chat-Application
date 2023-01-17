package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginPage extends AppCompatActivity {

    EditText phoneNumber, otp;
    Button sendOtp, signIn;
    String codeSent = "123456";
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        phoneNumber = findViewById(R.id.editTextPhone);
        otp = findViewById(R.id.editTextNumberPassword);
        signIn = findViewById(R.id.buttonSignIn);
        sendOtp = findViewById(R.id.atul);

        sendOtp.setOnClickListener(view -> {
            String userPhoneNumber = "+91" + phoneNumber.getText().toString();
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(userPhoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(LoginPage.this)
                    .setCallbacks(mCallbacks)
                    .build();

            PhoneAuthProvider.verifyPhoneNumber(options);
        });

        signIn.setOnClickListener(view -> signWithPhoneCode());
    }

    public void signWithPhoneCode() {
        String userEnterCode = otp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, userEnterCode);
        signInWithPhoneAuthCredential(credential);
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(LoginPage.this, FirstLoginPage.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(LoginPage.this, "OTP don't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            //super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            Intent i = new Intent(LoginPage.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}












//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.google.firebase.FirebaseException;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthOptions;
//import com.google.firebase.auth.PhoneAuthProvider;
//import com.hbb20.CountryCodePicker;
//
//import java.net.Inet4Address;
//import java.util.concurrent.TimeUnit;
//
//public class LoginPage extends AppCompatActivity {
//
//    EditText phoneNumber;
//    Button getOTP;
//    CountryCodePicker countryCodePicker;
//    FirebaseAuth auth = FirebaseAuth.getInstance();
//    FirebaseUser user = auth.getCurrentUser();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_page);
//
//        phoneNumber = findViewById(R.id.editTextPhoneNumber);
//        getOTP = findViewById(R.id.buttonGetOTP);
//        countryCodePicker = findViewById(R.id.ccp);
//
////        if(user!=null)
////        {
////            Intent i = new Intent(LoginPage.this,MainActivity.class);
////            startActivity(i);
////            finish();
////        }
//
//        getOTP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String userPhoneNumber = phoneNumber.getText().toString();
//
//                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(userPhoneNumber)
//                        .setTimeout(60L, TimeUnit.SECONDS)
//                        .setActivity(LoginPage.this)
//                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onCodeSent(String verificationId,
//                                                   PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                // Save the verification id somewhere
//                                // ...
//
//                                // The corresponding whitelisted code above should be used to complete sign-in.
////                                LoginPage.this.enableUserManuallyInputCode();
//                                Intent i = new Intent(LoginPage.this,OTP.class);
//            startActivity(i);
//            finish();
//
//                            }
//
//                            @Override
//                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                                // Sign in with the credential
//                                // ...
//                            }
//
//                            @Override
//                            public void onVerificationFailed(FirebaseException e) {
//                                // ...
//                            }
//                        })
//                        .build();
//                PhoneAuthProvider.verifyPhoneNumber(options);
//
//             //   Intent i = new Intent(LoginPage.this, OTP.class);
//                //i.putExtra("mobile",countryCodePicker.getFullNumberWithPlus().replaceAll(" ",""));
//                //i.putExtra("mobile",userPhoneNumber);
//             //   startActivity(i);
//            }
//        });
//    }
////
////    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
////        @Override
////        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
////
////        }
////
////        @Override
////        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//////            super.onCodeSent(s, forceResendingToken);
////            Intent i = new Intent(LoginPage.this,OTP.class);
////            startActivity(i);
////            finish();
////        }
////
////        @Override
////        public void onVerificationFailed(@NonNull FirebaseException e) {
////
////        }
////    };
//}