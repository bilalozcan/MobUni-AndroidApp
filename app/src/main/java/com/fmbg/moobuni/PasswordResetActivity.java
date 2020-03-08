package com.fmbg.moobuni;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PasswordResetActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText old_password, new_password, new_password_again;
    private Button save_button;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Şifre Değiştirme");
        toolbar.setTitleTextColor(Color.WHITE);

        old_password = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        new_password_again = findViewById(R.id.new_password_again);
        save_button = findViewById(R.id.save_button);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());
        userid = firebaseUser.getUid();
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!old_password.getText().toString().isEmpty() && !new_password.getText().toString().isEmpty() &&
                        !new_password_again.getText().toString().isEmpty()){
                    if(new_password.getText().toString().equals(new_password_again.getText().toString())) {
                        firebaseUser.updatePassword(new_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Toast.makeText(PasswordResetActivity.this, "Şifreniz değiştirildi", Toast.LENGTH_SHORT).show();
                                    databaseReference.child("password").setValue(new_password.getText().toString());
                                    onBackPressed();
                                } else {
                                    Toast.makeText(PasswordResetActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(PasswordResetActivity.this, "Şifre ve şifre tekrarı aynı değil!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PasswordResetActivity.this, "Lütfen tüm alanları doldurunuz", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
