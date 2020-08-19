package com.example.android.hrm;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private Handler handler = new Handler();
    MediaPlayer player;
    private SeekBar s;
    TextView  textcurrenttime, totalduration;
    ImageButton playButton;
    private boolean playOn;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userid = user.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("emp");
            Query checkUser = reference.child(userid);
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Employer");
            Query checkUser2 = reference2.child(userid);
            checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nameDB = snapshot.child("empname").getValue(String.class);
                        String phoneDB = snapshot.child("empnumber").getValue(String.class);
                        String genDB = snapshot.child("empaddress").getValue(String.class);
                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("stat", "नियोक्ता");
                        i.putExtra("name", nameDB);
                        i.putExtra("phone", phoneDB);
                        i.putExtra("occ", genDB);
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
                    if (snapshot.exists()) {
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
                        intent.putExtra("stat", "कर्मचारी");
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            setContentView(R.layout.activity_login_screen);
            editText = findViewById(R.id.editTextPhone3);
            playButton = findViewById(R.id.button_play);
            player = MediaPlayer.create(this, R.raw.guide);
            s = findViewById(R.id.seekBar);
            textcurrenttime =  findViewById(R.id.time);
            textcurrenttime.setText("0:00");
            totalduration=findViewById(R.id.time_end);
            totalduration.setText("1:07");
            s.setMax(100);
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

    @SuppressLint("ClickableViewAccessibility")
    public void play(View view) {
        if (player.isPlaying()) {
            handler.removeCallbacks(updater);
            player.pause();
            playButton.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        } else {
            player.start();
            playButton.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
            updateSeekBar();
        }
        prepareMediaPlayer();
        s.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SeekBar s = (SeekBar) v;
                int playposition = (player.getDuration() / 100) * s.getProgress();
                player.seekTo(playposition);
                textcurrenttime.setText(millisecondtotimer(player.getCurrentPosition()));
                return false;
            }
        });
    }
    private void prepareMediaPlayer() {
        try {

            totalduration.setText(millisecondtotimer(player.getDuration()));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentduration = player.getCurrentPosition();
            textcurrenttime.setText(millisecondtotimer(currentduration));
        }
    };
    private void updateSeekBar() {
        if (player.isPlaying()) {
            s.setProgress((int) (((float) player.getCurrentPosition() / player.getDuration()) * 100));
            handler.postDelayed(updater, 1000);

        }
    }
    private String millisecondtotimer(long milliseconds) {
        String timerstring = "";
        String secondstring;
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int second = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            timerstring = hours + ":";
        }
        if (second < 10) {
            secondstring = "0" + second;
        } else {
            secondstring = "" + second;
        }
        timerstring = timerstring + minutes + ":" + secondstring;
        return timerstring;
    }
}


