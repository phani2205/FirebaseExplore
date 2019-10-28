package com.firebase.explore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private String verificationCode ;
    private TextView question_tv;
    private Button optionA;
    private Button optionB;
    private Button optionC;
    private Button optionD;
    private Questions question;
    private int questionID = 1;
    private int correctAnswersCount = 0;
    private Button[] answerButtons = new Button[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
//        requestFirebaseOTP();
//        fetchQuestion(1);

        question_tv = findViewById(R.id.question_tv);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);

        optionA.setOnClickListener(this);
        optionB.setOnClickListener(this);
        optionC.setOnClickListener(this);
        optionD.setOnClickListener(this);

        answerButtons[0] = optionA;
        answerButtons[1] = optionB;
        answerButtons[2] = optionC;
        answerButtons[3] = optionD;
        setButtonColorToDefault();
        updateNextQuestion();

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
        setButtonColorToDefault();
        changeButtonsVisibility(false);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("questions").child(String.valueOf(id));


        System.out.println("ok firebase : "+reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("question : ");
                question = dataSnapshot.getValue(Questions.class);
                System.out.println("question : "+question);

                updateQuestionDetailsOnUI(question);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("question : "+databaseError);
            }


        });
    }

    private void updateQuestionDetailsOnUI(Questions question) {
        question_tv.setText(question.getQuestion());
        optionA.setText(question.getAnswers().get(0));
        optionB.setText(question.getAnswers().get(1));
        optionC.setText(question.getAnswers().get(2));
        optionD.setText(question.getAnswers().get(3));
    }

    @Override
    public void onClick(View view) {
        changeButtonClickStatus(true);
        Button b = (Button) view;
        if(b.getText().toString().equalsIgnoreCase(question.getAnswers().get(question.getCorrectIndex()))){
            correctAnswersCount++;
            Toast.makeText(MainActivity.this,"Correct Answer",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this,"Wrong Answer",Toast.LENGTH_LONG).show();
        }

        updateCorrectAnswerAndMoveToNextQuestion();
    }

    private void updateCorrectAnswerAndMoveToNextQuestion() {

        answerButtons[question.getCorrectIndex()].setBackgroundColor(Color.parseColor("#00ff00"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateNextQuestion();
            }
        },2000);
    }

    private void updateNextQuestion() {
        if(questionID<12){
            fetchQuestion(questionID);
            questionID++;
        }else{

            question_tv.setText("You have answered "+correctAnswersCount+" questions!");
            changeButtonsVisibility(true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    restartQuiz();
                }
            },5000);
        }
    }

    private void changeButtonsVisibility(boolean hide) {
        for (int i = 0; i <answerButtons.length ; i++) {
            if(hide){
                answerButtons[i].setVisibility(View.INVISIBLE);
            }else{
                answerButtons[i].setVisibility(View.VISIBLE);
            }

        }
    }

    private void restartQuiz() {
        questionID = 1;
        correctAnswersCount = 0;
        fetchQuestion(questionID);
        questionID++;
    }

    private void changeButtonClickStatus(boolean disable){
        optionA.setClickable(disable);
        optionB.setClickable(disable);
        optionC.setClickable(disable);
        optionD.setClickable(disable);
    }

    private void setButtonColorToDefault(){
        optionA.setBackgroundColor(Color.parseColor("#0000ff"));
        optionB.setBackgroundColor(Color.parseColor("#0000ff"));
        optionC.setBackgroundColor(Color.parseColor("#0000ff"));
        optionD.setBackgroundColor(Color.parseColor("#0000ff"));
    }
}
