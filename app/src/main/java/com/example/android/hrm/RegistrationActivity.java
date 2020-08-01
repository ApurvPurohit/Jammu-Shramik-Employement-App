package com.example.android.hrm;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class RegistrationActivity extends AppCompatActivity {

    Button employer, employee;
    EditText Name, Phone;
    Spinner occ_spinner1;
    Spinner exp_spinner2;
    Spinner gen_spinner3;
    String gen,occ,exp;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        employee = findViewById(R.id.button3);
        employer = findViewById(R.id.button4);
        Name = findViewById(R.id.editTextTextPersonName2);
        Phone = findViewById(R.id.editTextPhone2);
        occ_spinner1= (Spinner) findViewById(R.id.spinner1);
        exp_spinner2= (Spinner) findViewById(R.id.spinner2);
        gen_spinner3 = (Spinner) findViewById(R.id.spinner3);
        employee.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(Name.getText()==null)
                {
                    Toast.makeText(getApplicationContext(), "नाम आवश्यक है", Toast.LENGTH_SHORT).show();
                    Name.requestFocus();
                }
                else if(Phone.getText()==null || Phone.getText().length()!=10)
                {
                    Toast.makeText(getApplicationContext(), "फ़ोन नंबर मान्य नहीं है", Toast.LENGTH_SHORT).show();
                    Phone.requestFocus();
                }
                else if((occ_spinner1.getSelectedItem()).toString().equals("काम चुने"))
                {
                    Toast.makeText(getApplicationContext(), "सभी फ़ील्ड भरें", Toast.LENGTH_SHORT).show();
                }
                else if((gen_spinner3.getSelectedItem()).toString().equals("लिंग"))
                {
                    Toast.makeText(getApplicationContext(), "सभी फ़ील्ड भरें", Toast.LENGTH_SHORT).show();
                }
                else if((exp_spinner2.getSelectedItem()).toString().equals("अनुभव"))
                {
                    Toast.makeText(getApplicationContext(), "सभी फ़ील्ड भरें", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    UpdateToken();
                    occ = (occ_spinner1.getSelectedItem()).toString();
                    gen = (gen_spinner3.getSelectedItem()).toString();
                    exp = (exp_spinner2.getSelectedItem()).toString();
                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+occ_spinner1.getSelectedItemPosition())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "FAILED", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                                }
                            });
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("emp");
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    String userid=user.getUid();
                    EmployeeHelperClass helperClass = new EmployeeHelperClass(Name.getText().toString(),Phone.getText().toString(),gen,occ,exp);
                    reference.child(userid).setValue(helperClass);
                    Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                    i.putExtra("name",Name.getText().toString());
                    i.putExtra("phone",Phone.getText().toString());
                    i.putExtra("gen",gen);
                    i.putExtra("occ",occ);
                    i.putExtra("stat","कर्मचारी");
                    i.putExtra("exp",exp);
                    startActivity(i);
                }
            }
        });
    }
    private void UpdateToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);
            String job_selected = (occ_spinner1.getSelectedItem()).toString();
            FirebaseDatabase.getInstance().getReference("EmployeeTokens").child(job_selected).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newToken);
        });

    }
}

