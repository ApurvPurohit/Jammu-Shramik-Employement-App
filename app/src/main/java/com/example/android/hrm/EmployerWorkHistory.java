package com.example.android.hrm;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EmployerWorkHistory extends AppCompatActivity {
    ProgressBar s1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        s1 = findViewById(R.id.progressBar3);
        s1.setVisibility(View.VISIBLE);
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Employer_Work_History");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userid=user.getUid();
        Query checkUser = reference.child(userid);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                s1.setVisibility(View.GONE);
                if (snapshot.exists()) {
                    final TextView[] tv = new TextView[(Integer.parseInt(String.valueOf(snapshot.getChildrenCount())))];
                    int k = 0;
                    int t=1;
                    final LinearLayout rl = findViewById(R.id.linear_layout);
                    for (DataSnapshot data : snapshot.getChildren()) {
                        EmployerRequirementHelper user = data.getValue(EmployerRequirementHelper.class);
                        String nameDB = data.child("Dated").getValue(String.class);
                        tv[k] = new TextView(getApplicationContext());
                        assert user != null;
                        tv[k].setText((t++)+". "+nameDB+"\nकाम: " + user.getJob() + "\nविवरण: " + user.getJob_desp() + "\nश्रमिकों की संख्या: " + user.getNlab() + "\nदिनों की संख्या: " + user.getNdays());
                        tv[k].setTextSize((float) 20);
                        tv[k].setBackgroundColor(Color.parseColor("#f8fcee"));
                        tv[k].setPadding(20, 20, 20, 20);
                        rl.addView(tv[k]);
                        TextView border = new TextView(getApplicationContext());
                        border.setText("\n");
                        border.setBackgroundColor(Color.parseColor("#4FB5E6"));
                        rl.addView(border);
                        k++;
                    }
                } else {
                    final LinearLayout rl = findViewById(R.id.linear_layout);
                    TextView tv = new TextView(getApplicationContext());
                    tv.setText("कोई इतिहास नहीं मिला");
                    tv.setTextSize((float) 20);
                    tv.setPadding(20, 20, 20, 20);
                    rl.addView(tv);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        findViewById(R.id.button222).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

