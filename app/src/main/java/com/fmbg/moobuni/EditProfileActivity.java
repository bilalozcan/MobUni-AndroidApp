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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.fmbg.moobuni.Model.Department;
import com.fmbg.moobuni.Model.University;
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
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private CircleImageView img_profile;
    private ImageView close;
    private EditText name_edittext, username_edittext, email_edittext;
    private TextView name_text, username_text, university_text, department_text, email_text,change_profile_image;
    private Spinner university_spinner, department_spinner;
    private Button save_button;
    private String userid;
    private Uri selectedImage;
    private Toolbar actionbareditprofile;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private String usertype;

    private ArrayAdapter<String> adapterUniversity;
    private ArrayAdapter<String> adapterDepartment;
    private ArrayList<String> universityList;
    private ArrayList<String> departmentList;
    private XMLParserUniversity xmlParserUniversity;
    private XMLParserDepartment xmlParserDepartment;
    private ArrayList<University> universityArrayList;
    private ArrayList<Department> departmentArrayList;
    String universite_id;
    private void init() {
        actionbareditprofile = (Toolbar)findViewById(R.id.acionbareditprofile);
        setSupportActionBar(actionbareditprofile);
        getSupportActionBar().setTitle("Profil Düzenle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
        close = findViewById(R.id.close);
        change_profile_image = findViewById(R.id.change_profile_image);
        img_profile = findViewById(R.id.img_profile);
        name_edittext = findViewById(R.id.name_edittext);
        username_edittext = findViewById(R.id.username_edittext);
        //email_edittext = findViewById(R.id.email_edittext);
        name_text = findViewById(R.id.name_text);
        username_text = findViewById(R.id.username_text);
        university_text = findViewById(R.id.university_text);
        //email_text = findViewById(R.id.email_text);
        department_text = findViewById(R.id.department_text);
        university_spinner = findViewById(R.id.university_spinner);
        department_spinner = findViewById(R.id.department_spinner);
        save_button = findViewById(R.id.save_button);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getUsertype().equals("Üniversite")){
                    department_text.setVisibility(View.VISIBLE);
                    university_text.setVisibility(View.VISIBLE);
                    university_spinner.setVisibility(View.VISIBLE);
                    department_spinner.setVisibility(View.VISIBLE);
                    spinnerReadInfo();
                    university_spinner.setTag(user.getUniversity());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        readUserInfo();

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUserInfo();
                Toast.makeText(EditProfileActivity.this, "Bilgileriniz Güncellenmiştir!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        change_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);
            }
        });
    }

    private void selectImage(View view) {
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
            selectedImage = data.getData();

            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), selectedImage);
                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                    img_profile.setImageBitmap(bitmap);
                } else {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    img_profile.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            userid = firebaseUser.getUid();

            storageReference = storageReference.child("ProfileImage/" + userid);
            storageReference.putFile(selectedImage);
            storageReference.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newRefrence = FirebaseStorage.getInstance().getReference("ProfileImage/" + userid);
                    newRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            databaseReference.child("imageurl").setValue(downloadUrl);
                        }
                    });
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                }
            });


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void spinnerReadInfo() {
        universityList = new ArrayList<String>();
        departmentList = new ArrayList<String>();

        xmlParserUniversity = new XMLParserUniversity(getApplicationContext());
        universityArrayList = xmlParserUniversity.ParsUniversity();

        xmlParserDepartment = new XMLParserDepartment(getApplicationContext());
        departmentArrayList = xmlParserDepartment.ParsDepartment();

        for(int i = 0 ; i < universityArrayList.size(); ++i){
            universityList.add(universityArrayList.get(i).getName());
        }
        for (int i = 0; i < departmentArrayList.size(); ++i) {
            if (departmentArrayList.get(i).getUniversite_id().equals("1")){
                departmentList.add(departmentArrayList.get(i).getName());
            }
        }

        adapterUniversity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, universityList);
        adapterDepartment = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departmentList);

        adapterUniversity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        university_spinner.setAdapter(adapterUniversity);
        department_spinner.setAdapter(adapterDepartment);

        university_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                universite_id = "";
                for (int i = 0; i < universityArrayList.size(); i++) {
                    if(parent.getSelectedItem().toString().equals(universityArrayList.get(i).getName())){
                        universite_id = universityArrayList.get(i).getUniversite_id();
                    }
                }

                departmentList.clear();
                for (int j = 0; j < departmentArrayList.size(); j++) {
                    if(departmentArrayList.get(j).getUniversite_id().equals(universite_id)){
                        departmentList.add(departmentArrayList.get(j).getName());
                    }
                }
                adapterDepartment = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_item, departmentList);
                adapterUniversity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                department_spinner.setAdapter(adapterDepartment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void changeUserInfo() {
        databaseReference.child("username").setValue(username_edittext.getText().toString());
        databaseReference.child("fullname").setValue(name_edittext.getText().toString());
        if(university_spinner.getVisibility() != View.GONE){
            databaseReference.child("university").setValue(university_spinner.getSelectedItem().toString());
            databaseReference.child("department").setValue(department_spinner.getSelectedItem().toString());
        }
        /*firebaseUser.updateEmail(email_edittext.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    databaseReference.child("email").setValue(email_edittext.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/



    }

    private void readUserInfo() {
        userid = firebaseUser.getUid();
        databaseReference = databaseReference.child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username_edittext.setText(user.getUsername());
                name_edittext.setText(user.getFullname());
                //email_edittext.setText(user.getEmail());
                if (user.getImageurl().equals("default")) {
                    img_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MobUni.getContext()).load(user.getImageurl()).into(img_profile);
                    //bilal yat yat çalışma sen biz yazalım kodları
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
