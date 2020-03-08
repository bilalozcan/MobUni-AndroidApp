package com.fmbg.moobuni;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.fmbg.moobuni.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileOtherActivity extends AppCompatActivity {


    private CircleImageView image_profile;
    private TextView username, department, school;
    private Button send_message, show_question, show_post;
    private Toolbar actionbarprofile;
    private ImageButton profileMessageButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private FirebaseUser fuser;

    private String userid;

    private static final int IMAGE_REQUEST = 1;
    private StorageTask uploadTask;
    private void init() {
        actionbarprofile = (Toolbar)findViewById(R.id.profileactionbar);
        setSupportActionBar(actionbarprofile);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionbarprofile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_other);
        init();

        image_profile = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        school = findViewById(R.id.school_name);
        department = findViewById(R.id.department);
        send_message = findViewById(R.id.send_message);
        show_question = findViewById(R.id.show_questions);
        show_post = findViewById(R.id.show_post);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        fuser = mAuth.getCurrentUser();

        Bundle intent = getIntent().getExtras();
        userid = intent.getString("userid");
        if(userid.equals(fuser.getUid())){
            startActivity(new Intent(ProfileOtherActivity.this, ProfileActivity.class));
        }


        show_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileOtherActivity.this, CurrentUserQuestionsActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });
        show_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileOtherActivity.this, CurrentUserPostActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileOtherActivity.this, MessageActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });

      reference = firebaseDatabase.getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(!user.getUniversity().equals("null")){
                    school.setText(user.getUniversity());
                }
                if(!user.getDepartment().equals("null")){
                    department.setText(user.getDepartment());
                }
                if (user.getImageurl().equals("default")) {
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MobUni.getContext()).load(user.getImageurl()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
