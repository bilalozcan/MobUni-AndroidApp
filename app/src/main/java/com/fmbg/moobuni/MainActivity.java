package com.fmbg.moobuni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.fmbg.moobuni.RegisterActivity;

public class MainActivity extends AppCompatActivity {
    //Sign up and sign in button
    private Button accesButton,registerButton;
    private FirebaseAuth mAuth;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        kullaniciTipKontrol();

        //Sign up and sign in button
        accesButton=(Button)findViewById(R.id.accesButtonId);
        registerButton=(Button)findViewById(R.id.registerButtonId);

        accesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenylogin =new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intenylogin);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenyregister =new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intenyregister);
            }
        });


    }

    private void kullaniciTipKontrol() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Toast.makeText(MainActivity.this,dataSnapshot.child(user.getUid()).child("Kullanıcı Tipi").getValue().toString(), Toast.LENGTH_SHORT).show();
                    String type = dataSnapshot.child(user.getUid()).child("usertype").getValue().toString();
                    if(type.equals("Lise")){
                        Intent intent = new Intent(getApplicationContext(), UniversityMainActivity.class);
                        startActivity(intent);
                    }
                    else if (type.equals("Üniversite")){
                        Intent intent = new Intent(getApplicationContext(), HighschoolMainActivity.class);
                        startActivity(intent);
                    }
                }
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

    }
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setIcon()
        builder.setCancelable(false);
        builder.setMessage("Çıkış Yapmak istiyor musunuz?");
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override            public void onClick(DialogInterface dialog, int which) {
                // Evet'e basılınca yapılacak işlemleri yazınız
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override            public void onClick(DialogInterface dialog, int which) {
                // Hayır'a baslınca yapılacak işmeleri yazınız
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
