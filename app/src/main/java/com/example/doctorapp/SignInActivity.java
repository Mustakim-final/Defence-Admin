package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SignInActivity extends AppCompatActivity {

    private EditText gmailEditText,passwordEditText;
    private Button button;
    private ProgressBar progressBar;
    private TextView goSignUp,forgotPass;

    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        gmailEditText=findViewById(R.id.signEmail_ID);
        passwordEditText=findViewById(R.id.signPassword_ID);
        button=findViewById(R.id.signSubmit_ID);
        progressBar=findViewById(R.id.signProgressbar_ID);
        goSignUp=findViewById(R.id.goSignUP_ID);
        forgotPass=findViewById(R.id.forgotPass_ID);

        goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignInActivity.this,RegActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gmail=gmailEditText.getText().toString().trim();
                String password=passwordEditText.getText().toString().trim();


                if (gmail.isEmpty()){
                    gmailEditText.setError("Enter Gmail !!");
                    gmailEditText.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(gmail).matches()){
                    gmailEditText.setError("Enter Valid gmail !!");
                    gmailEditText.requestFocus();
                }else if (password.isEmpty()){
                    passwordEditText.setError("Enter Password !!");
                    passwordEditText.requestFocus();
                }else if (password.length()<6){
                    passwordEditText.setError("Enter 6 digit password !!");
                    passwordEditText.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    signInUser(gmail,password);
                }
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=new EditText(v.getContext());
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setTitle("Reset Password");
                builder.setMessage("আপনার মেইল দিন।মেইল এর মাধ্যমে পাসওয়ার্ড রিসেট করার জন্য লিঙ্ক দেওয়া হবে।");
                builder.setView(editText);

                builder.setPositiveButton("হ্যা", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email=editText.getText().toString();
                        if (email.isEmpty()){
                            editText.setError("Enter gmail");
                            editText.requestFocus();
                            return;
                        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                            editText.setError("Enter a valid gmail");
                            editText.requestFocus();
                            return;
                        }else {
                            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SignInActivity.this,"আপনার মেইল চেক করুন।",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignInActivity.this,"সঠিক মেইল দিন।",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });

                builder.setNegativeButton("না", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                builder.create().show();

            }
        });


    }

    private void signInUser(String gmail, String password) {
        firebaseAuth.signInWithEmailAndPassword(gmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent=new Intent(SignInActivity.this, Home_Doctor_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(SignInActivity.this,"Sign in successfully",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}