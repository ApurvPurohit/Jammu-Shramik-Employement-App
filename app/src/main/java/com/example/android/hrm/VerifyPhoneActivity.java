package com.example.android.hrm;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        editText = findViewById(R.id.editTextCode);
        phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {
                    editText.setError("Invalid Code");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void verifyCode(String code) {
        progressBar.setVisibility(View.VISIBLE);
        try {PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);} catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            isUser();
                        } else {
                            Toast.makeText(VerifyPhoneActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void isUser() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        { String userid=user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("emp");
        Query checkUser = reference.child(userid);
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Employer");
            Query checkUser2 = reference2.child(userid);
            checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        String nameDB = snapshot.child("empname").getValue(String.class);
                        String phoneDB = snapshot.child("empnumber").getValue(String.class);
                        String genDB = snapshot.child("empaddress").getValue(String.class);
                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("name",nameDB);
                        i.putExtra("phone",phoneDB);
                        i.putExtra("occ",genDB);
                        i.putExtra("stat","नियोक्ता");
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    String nameDB = snapshot.child("name").getValue(String.class);
                                    String phoneDB = snapshot.child("phone").getValue(String.class);
                                    String genDB = snapshot.child("gen").getValue(String.class);
                                    String occDB = snapshot.child("occ").getValue(String.class);
                                    String expDB = snapshot.child("exp").getValue(String.class);
                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("name",nameDB);
                                    intent.putExtra("phone",phoneDB);
                                    intent.putExtra("gen",genDB);
                                    intent.putExtra("occ",occDB);
                                    intent.putExtra("exp",expDB);
                                    intent.putExtra("stat","कर्मचारी");
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(VerifyPhoneActivity.this, TradeSelector.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
