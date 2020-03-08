package com.fmbg.moobuni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class LoadingActivity extends AppCompatActivity {
    private FirebaseAuth currentAuth;
    private FirebaseDatabase firebaseDatabase;

    private ImageView imageView;
    private TextView textView;
    private ProgressBar progressBar, progressBar2;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        //imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.appName);
        progressBar = findViewById(R.id.progressBar);
        currentAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        //kullaniciTipKontrol();
       final FirebaseUser user = currentAuth.getCurrentUser();
        boolean a=preferences.getBoolean("youareuniversity",true);
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else if(preferences.getBoolean("login",true)) {
            if(a==true){

                Intent intent = new Intent(getApplicationContext(), UniversityMainActivity.class);
                startActivity(intent);
            }
            else if (a==false) {
                Intent intent = new Intent(getApplicationContext(), HighschoolMainActivity.class);
                startActivity(intent);
            }

        }
    }
   /* private void kullaniciTipKontrol() {
        final FirebaseUser user = currentAuth.getCurrentUser();
        if(user != null) {
            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String type = dataSnapshot.child(user.getUid()).child("usertype").getValue().toString();
                    if(type.equals("Lise")){
                        Intent intent = new Intent(getApplicationContext(), HighschoolMainActivity.class);
                        startActivity(intent);
                    }
                    else if (type.equals("Üniversite")){
                        Intent intent = new Intent(getApplicationContext(), UniversityMainActivity.class);
                        startActivity(intent);
                    }
                }
                public void onCancelled(DatabaseError databaseError) {}
            });
        }else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }*/
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
