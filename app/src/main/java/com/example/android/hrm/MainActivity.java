package com.example.android.hrm;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onStart() {
        super.onStart();
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
                            i.putExtra("stat","नियोक्ता");
                            i.putExtra("name",nameDB);
                            i.putExtra("phone",phoneDB);
                            i.putExtra("occ",genDB);
                            startActivity(i);
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String nameDB = snapshot.child("name").getValue(String.class);
                            String phoneDB = snapshot.child("phone").getValue(String.class);
                            String genDB = snapshot.child("gen").getValue(String.class);
                            String occDB = snapshot.child("occ").getValue(String.class);
                            String expDB = snapshot.child("exp").getValue(String.class);
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("name", nameDB);
                            intent.putExtra("phone", phoneDB);
                            intent.putExtra("gen", genDB);
                            intent.putExtra("occ", occDB);
                            intent.putExtra("exp", expDB);
                            intent.putExtra("stat","कर्मचारी");
                            startActivity(intent);
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });}
            else
            {
                setContentView(R.layout.activity_login_screen);
                editText = findViewById(R.id.editTextPhone3);
                findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String number = editText.getText().toString().trim();

                        if (number.isEmpty()) {
                            editText.setError("Field Empty");
                            editText.requestFocus();
                            return;
                        }
                        if (number.length() != 10) {
                            editText.setError("Valid number is required");
                            editText.requestFocus();
                            return;
                        }

                        String phoneNumber = "+91" + number;
                        Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
                        intent.putExtra("phonenumber", phoneNumber);
                        startActivity(intent);

                    }
                });
            }
        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

    }
}