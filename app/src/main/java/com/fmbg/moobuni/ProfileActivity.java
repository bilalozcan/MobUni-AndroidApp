package com.fmbg.moobuni;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.fmbg.moobuni.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView image_profile;
    TextView username, department, school;
    Button profile_setting, show_question, show_post;
    private Toolbar actionbarprofile;
    ImageButton profileMessageButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    StorageReference mStorageRef;
    FirebaseUser fuser;

    User user;
    Uri selectedImage;
    String myUrl = "";
    String userUid;


    private static final int IMAGE_REQUEST = 1;
    private StorageTask uploadTask;
    private void init() {
        actionbarprofile = (Toolbar)findViewById(R.id.profileactionbar);
        setSupportActionBar(actionbarprofile);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        //mStorageRef = FirebaseStorage.getInstance().getReference();

        image_profile = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        school = findViewById(R.id.school_name);
        department = findViewById(R.id.department);
        profile_setting = findViewById(R.id.send_message);
        show_question = findViewById(R.id.show_questions);
        show_post = findViewById(R.id.show_post);

        show_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, CurrentUserQuestionsActivity.class);
                intent.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
            }
        });
        show_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, CurrentUserPostActivity.class);
                intent.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
            }
        });
        //storageReference = FirebaseStorage.getInstance().getReference("uploads");
        profile_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        mStorageRef = FirebaseStorage.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("OnData 1");
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
                System.out.println("OnData 2");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);
            }
        });
    }




    public void selectImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            System.out.println("Activity result 1");
            selectedImage = data.getData();

            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), selectedImage);
                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                    image_profile.setImageBitmap(bitmap);
                } else {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    image_profile.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("F1");
            FirebaseUser user = mAuth.getCurrentUser();
            userUid = user.getUid();

            StorageReference storageReference = mStorageRef.child("ProfileImage/" + userUid);
            storageReference.putFile(selectedImage);
            System.out.println("F2");
            storageReference.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Success 1");
                    StorageReference newRefrence = FirebaseStorage.getInstance().getReference("ProfileImage/" + userUid);
                    newRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            System.out.println("Success2");
                            String downloadUrl = uri.toString();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
                            databaseReference.child("imageurl").setValue(downloadUrl);
                            System.out.println("Success3");
                        }
                    });
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                }
            });


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}