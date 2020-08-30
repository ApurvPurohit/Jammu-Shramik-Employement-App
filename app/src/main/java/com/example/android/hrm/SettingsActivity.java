package com.example.android.hrm;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Build;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    Button available;
    TextView curr, textt;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setiings_activity);
        curr= findViewById(R.id.current);
        textt = findViewById(R.id.te);

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        available = findViewById(R.id.availability);
        available.setVisibility(View.GONE);
        if((getIntent().getStringExtra("stat")).equals("कर्मचारी"))
        {
            available.setVisibility(View.VISIBLE);
            String test = getIntent().getStringExtra("available");
            if (test.equals("1")) {
                curr.setText("उपलब्ध");
                curr.setTextColor(Color.parseColor("#1e8449"));
            } else {
                curr.setText("अनुपलब्ध");
                curr.setTextColor(Color.parseColor("#1e8449"));
            }
        }
        else {curr.setVisibility(View.GONE);textt.setVisibility(View.GONE);}
        findViewById(R.id.availability).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String available = getIntent().getStringExtra("available");
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                assert user!=null;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("emp");
                String userid = user.getUid();
                Query checkUser = reference.child(userid);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            Toast.makeText(getApplicationContext(),"उपलब्धता की स्थिति सफलतापूर्वक बदल दी गई है",Toast.LENGTH_LONG).show();
                            String t = snapshot.child("status").getValue(String.class);
                            if (t.equals("1")) {
                                curr.setText("अनुपलब्ध");
                                curr.setTextColor(Color.parseColor("#1e8449"));
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("emp").child(userid);
                                reference.child("status").setValue("0");
                            } else {
                                curr.setText("उपलब्ध");
                                curr.setTextColor(Color.parseColor("#1e8449"));
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("emp").child(userid);
                                reference.child("status").setValue("1");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        findViewById(R.id.editdetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((getIntent().getStringExtra("stat")).equals("कर्मचारी")){startActivity(new Intent(getApplicationContext(),EmployeeRegistrationActivity.class));}
                if((getIntent().getStringExtra("stat")).equals("नियोक्ता")){startActivity(new Intent(getApplicationContext(),EmployerRegistration.class));}
            }
        });
    }
}
