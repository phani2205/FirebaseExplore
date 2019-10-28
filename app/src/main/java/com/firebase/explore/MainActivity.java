package com.firebase.explore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationCode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
//        requestFirebaseOTP();
//        fetchQuestion(1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchQuestion(2);
            }
        },3000);

    }

    private void requestFirebaseOTP(){

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                System.out.println("Phani onVerificationCompleted : "+phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                System.out.println("Phani onVerificationFailed : "+e);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                super.onCodeSent(s, forceResendingToken);
                System.out.println("Phani onCodeSent : "+s);
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+919985788376",        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


    }


    private void fetchQuestion(int id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("questions").child(String.valueOf(id));


        System.out.println("ok firebase : "+reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("question : ");
                Questions question = dataSnapshot.getValue(Questions.class);
                System.out.println("question : "+question);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("question : "+databaseError);
            }


        });
    }
}
