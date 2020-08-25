package com.example.android.hrm;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.widget.Toast.*;

public class SettingsActivity extends AppCompatActivity {

    TextView curr;
    Switch avi;
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setiings_activity);
        curr= findViewById(R.id.current);
        curr.setText("उपलब्ध");
        avi= findViewById(R.id.switch1);
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

        avi.setVisibility(View.GONE);

        if((getIntent().getStringExtra("stat")).equals("नियोक्ता")){findViewById(R.id.scrollView3).setVisibility(View.GONE);}

        boolean test = Objects.requireNonNull(getIntent().getExtras()).getBoolean("available");
        if (test) {
            curr.setText("उपलब्ध");
            avi.setChecked(true);
            curr.setTextColor(Color.parseColor("#1e8449"));
        } else {
            curr.setText("अनुपलब्ध");
            avi.setChecked(false);
            curr.setTextColor(Color.parseColor("#ff5733"));
        }



        if((getIntent().getStringExtra("stat")).equals("कर्मचारी")){avi.setVisibility(View.VISIBLE);}

        avi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("emp");
                String userid = user.getUid();
                Query checkUser = reference.child(userid);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            makeText(getApplicationContext(), "उपलब्धता की स्थिति सफलतापूर्वक बदल दी गई है", LENGTH_LONG).show();
                            boolean a = (boolean) snapshot.child("avail").getValue();
                            if (a) {
                                curr.setText("अनुपलब्ध");
                                curr.setTextColor(Color.parseColor("#ff5733"));
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("emp").child(userid);
                                reference.child("avail").setValue(false);
                            } else if(!a){
                                curr.setText("उपलब्ध");
                                curr.setTextColor(Color.parseColor("#1e8449"));
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("emp").child(userid);
                                reference.child("avail").setValue(true);
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


                if((getIntent().getStringExtra("stat")).equals("कर्मचारी")){
                    DialogPlus dialog = DialogPlus.newDialog(SettingsActivity.this)
                            .setGravity(Gravity.CENTER)
                            .setMargin(50, 0, 50, 0)
                            .setContentHolder(new ViewHolder(R.layout.dialog_employee))
                            .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                            .create();
                    View holderview=(LinearLayout)dialog.getHolderView();
                    EditText name=holderview.findViewById(R.id.change_Name);
                    EditText phone=holderview.findViewById(R.id.change_Phone);
                    Spinner exp_spinner= holderview. findViewById(R.id.spinner_exp);
                    Spinner occ_spinner= holderview. findViewById(R.id.spinner_profile);
                    Button update=holderview.findViewById(R.id.update);

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String,Object> map=new HashMap<>();
                            map.put("name",name.getText().toString());
                            map.put("phone",phone.getText().toString());
                            map.put("occ",(occ_spinner.getSelectedItem()).toString());
                            map.put("exp",(exp_spinner.getSelectedItem()).toString());
                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                            if(user!=null)
                            {
                                String userid=user.getUid();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("emp");
                                reference.child(userid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(),"सफलतापूर्वक संपादित किया गया",LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists())
                                                {
                                                    Intent i= new Intent(getApplicationContext(),ProfileActivity.class);
                                                    EmployeeHelperClass u=dataSnapshot.getValue(EmployeeHelperClass.class);
                                                    assert u != null;
                                                    i.putExtra("name",u.getName());
                                                    i.putExtra("phone",u.getPhone());
                                                    i.putExtra("gen",u.getGen());
                                                    i.putExtra("occ",u.getOcc());
                                                    i.putExtra("exp",u.getExp());
                                                    i.putExtra("available",u.isAvail());
                                                    i.putExtra("stat","कर्मचारी");
                                                    startActivity(i);// Code
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Code
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                    dialog.show();
                }
                if((getIntent().getStringExtra("stat")).equals("नियोक्ता")){
                    DialogPlus dialog = DialogPlus.newDialog(SettingsActivity.this)
                            .setGravity(Gravity.CENTER)
                            .setMargin(50, 0, 50, 0)
                            .setContentHolder(new ViewHolder(R.layout.dialog_content))
                            .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                            .create();
                    View holderview=(LinearLayout)dialog.getHolderView();
                    EditText empname=holderview.findViewById(R.id.editTextPersonName);
                    EditText empaddress=holderview. findViewById(R.id.editTextTextPersonAddress);
                    EditText empphone=holderview.findViewById(R.id.editTextPersonPhone);
                    Button update=holderview.findViewById(R.id.update);

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String,Object> map=new HashMap<>();
                            map.put("empname",empname.getText().toString());
                            map.put("empaddress",empaddress.getText().toString());
                            map.put("empnumber",empphone.getText().toString());
                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                            if(user!=null)
                            {
                                String userid=user.getUid();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Employer");
                                reference.child(userid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "सफलतापूर्वक संपादित किया गया",LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                               if(dataSnapshot.exists())
                                               {
                                                   Intent i= new Intent(getApplicationContext(),ProfileActivity.class);
                                                   EmployerRegistrationHelper u=dataSnapshot.getValue(EmployerRegistrationHelper.class);
                                                   assert u != null;
                                                   i.putExtra("name",u.getEmpname());
                                                   i.putExtra("phone",u.getEmpnumber());
                                                   i.putExtra("occ",u.getEmpaddress());
                                                   i.putExtra("stat","नियोक्ता");
                                                   startActivity(i);// Code
                                               }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Code
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                    dialog.show();
                }
            }
        });
    }
}
