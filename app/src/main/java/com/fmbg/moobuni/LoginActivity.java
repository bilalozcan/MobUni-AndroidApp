package com.fmbg.moobuni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    private Toolbar actionbarLogin;
    EditText mailtext,passwordtext;
    TextView forgotPassword;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public void init(){
        actionbarLogin = (Toolbar)findViewById(R.id.actionbarloginid);
        setSupportActionBar(actionbarLogin);
        getSupportActionBar().setTitle("Giriş Yap");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        System.out.println("Logine geldi");
        init();
        mAuth = FirebaseAuth.getInstance();
        mailtext=findViewById(R.id.mailtxt1);
        passwordtext=findViewById(R.id.passwordtxt2);
        firebaseDatabase = FirebaseDatabase.getInstance();
        forgotPassword = findViewById(R.id.forgotpassword);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        GoToResetPasswordScreen();
    }

    public void signIn(View view ){
        if(!mailtext.getText().toString().isEmpty() && !passwordtext.getText().toString().isEmpty()) {
            mAuth.signInWithEmailAndPassword(mailtext.getText().toString(), passwordtext.getText().toString()).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if(mAuth.getCurrentUser().isEmailVerified()) {
                                    editor.putBoolean("login", true);
                                    Toast.makeText(LoginActivity.this, "Hoşgeldin", Toast.LENGTH_SHORT).show();
                                    kullaniciTipKontrol();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Lütfen e--mail adresinizi doğrulayın", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
        }
        editor.commit();
    }
    private void kullaniciTipKontrol() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String type = dataSnapshot.child(user.getUid()).child("usertype").getValue().toString();
                    if(type.equals("Lise")){
                        editor.putBoolean("youareuniversity", false);
                        Intent intent = new Intent(getApplicationContext(), HighschoolSelectUniversityActivity.class);
                        editor.commit();
                        startActivity(intent);
                    }
                    else if (type.equals("Üniversite")){
                        editor.putBoolean("youareuniversity", true);
                        editor.putString("university_name",dataSnapshot.child(user.getUid()).child("university").getValue().toString());
                        editor.putString("department_name",dataSnapshot.child(user.getUid()).child("department").getValue().toString());
                        Intent intent = new Intent(getApplicationContext(), UniversityMainActivity.class);
                        editor.commit();
                        startActivity(intent);
                    }
                }
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
    private void GoToResetPasswordScreen(){
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
    }
}
