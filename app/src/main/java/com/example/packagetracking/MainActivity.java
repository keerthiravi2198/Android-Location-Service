package com.example.packagetracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText track;
    Button start;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        track = findViewById(R.id.login_track);
        start = findViewById(R.id.login_start);
        FirebaseApp.initializeApp(this);
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        final DatabaseReference dr = fd.getReference("tracking");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = track.getText().toString();
                DatabaseReference tr = dr.child(id).child("status");
                tr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(Integer.class) != null){
                            SharedPreferences.Editor editor = getSharedPreferences("VIRTUSA", MODE_PRIVATE).edit();
                            editor.putString("id", id);
                            editor.apply();
                            startService(new Intent(MainActivity.this, MyService.class));
                            Toast.makeText(MainActivity.this, "Tracking ID has been set!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Tracking ID does not exist!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
