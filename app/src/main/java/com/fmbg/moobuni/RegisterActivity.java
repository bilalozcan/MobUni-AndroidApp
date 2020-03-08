package com.fmbg.moobuni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fmbg.moobuni.Model.Department;
import com.fmbg.moobuni.Model.University;
import com.fmbg.moobuni.Model.User;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private Spinner spinnerUniversiteler;
    private Spinner spinnerBolumler;
    private ArrayAdapter<String> dataAdapterForUniversiteler;
    private ArrayAdapter<String> dataAdapterForBolumler;

    private ScrollView scrollView;
    private RelativeLayout frameLayout;
    private CheckBox typeSwitch;

    private Toolbar actionbarLogina;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    EditText mailtext, passwordtext, passwordtextagain, nametext, usertext;
    List<University> universityList;

    SharedPreferences preferences;
    ProgressDialog progressDialog;

    public void init() {
        actionbarLogina = (Toolbar) findViewById(R.id.actionbarregisterid);
        setSupportActionBar(actionbarLogina);
        getSupportActionBar().setTitle("Hesap Oluştur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        frameLayout = findViewById(R.id.frameLayout);
        typeSwitch = findViewById(R.id.typeSwitch);

        scrollView = findViewById(R.id.scrollView);
        spinnerUniversiteler = (Spinner) findViewById(R.id.spinner);
        spinnerBolumler = (Spinner) findViewById(R.id.spinner2);
        universityList = new ArrayList<>();
        spinnerOlusturma();

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        mailtext = findViewById(R.id.mailtxt);
        passwordtext = findViewById(R.id.passwordtxt);
        passwordtextagain = findViewById(R.id.passwordtxtagain);
        nametext = findViewById(R.id.nametxt);
        usertext = findViewById(R.id.usertxt);

        switchKontrol();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    private void switchKontrol() {
        typeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    frameLayout.setVisibility(View.VISIBLE);
                } else {
                    frameLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void spinnerOlusturma() {
        final ArrayList<String> nameList = new ArrayList<String>();
        final ArrayList<String> departmentList = new ArrayList<String>();

        XMLParserUniversity xmlParserUniversity = new XMLParserUniversity(getApplicationContext());
        final ArrayList<University> universityArrayList = xmlParserUniversity.ParsUniversity();

        XMLParserDepartment xmlParserDepartment = new XMLParserDepartment(getApplicationContext());
        final ArrayList<Department> departmentArrayList = xmlParserDepartment.ParsDepartment();

        for(int i = 0 ; i < universityArrayList.size(); ++i){
            nameList.add(universityArrayList.get(i).getName());
        }
        for (int i = 0; i < departmentArrayList.size(); ++i) {
            if (departmentArrayList.get(i).getUniversite_id().equals("1")){
                departmentList.add(departmentArrayList.get(i).getName());
            }
        }

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForUniversiteler = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nameList);
        dataAdapterForBolumler = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departmentList);

        //Listelenecek verilerin görünümünü belirliyoruz.
        dataAdapterForUniversiteler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForBolumler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinnerUniversiteler.setAdapter(dataAdapterForUniversiteler);
        spinnerBolumler.setAdapter(dataAdapterForBolumler);

        // Listelerden bir eleman seçildiğinde yapılacakları tanımlıyoruz.
        spinnerUniversiteler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Hangi il seçilmişse onun ilçeleri adapter'e ekleniyor.
                String universite_id = "";
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
                dataAdapterForBolumler = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, departmentList);
                dataAdapterForBolumler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerBolumler.setAdapter(dataAdapterForBolumler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerBolumler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Seçilen il ve ilçeyi ekranda gösteriyoruz.
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void signUp(View view) {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Kontrol Ediliyor");
        progressDialog.show();
        if(!mailtext.getText().toString().isEmpty() && !passwordtext.getText().toString().isEmpty() && !nametext.getText().toString().isEmpty() &&
        !usertext.getText().toString().isEmpty() && !passwordtextagain.getText().toString().isEmpty()) {
            checkUsername();
            if(passwordtext.getText().toString().equals(passwordtextagain.getText().toString())) {
               mAuth.createUserWithEmailAndPassword(mailtext.getText().toString(), passwordtext.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.setMessage("Kontrol Ediliyor");
                        progressDialog.show();
                        final FirebaseUser user = mAuth.getCurrentUser();
                        String userUid = user.getUid();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    writeDatabaseUser();
                                }
                            }
                        });

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                        }
                    }).addOnFailureListener(new OnFailureListener() {

                        public void onFailure(Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            }else {
                progressDialog.dismiss();
                Toast.makeText(this, "Şifreler aynı değil!", Toast.LENGTH_SHORT).show();
            }
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Lütfen tüm alanları doldurunuz", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkUsername(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void writeDatabaseUser(){
        final FirebaseUser user = mAuth.getCurrentUser();
        String userUid = user.getUid();
        myRef.child("Users").child(userUid).child("email").setValue(mailtext.getText().toString());
        myRef.child("Users").child(userUid).child("fullname").setValue(nametext.getText().toString());
        myRef.child("Users").child(userUid).child("username").setValue(usertext.getText().toString());
        if(typeSwitch.isChecked()){
            myRef.child("Users").child(userUid).child("usertype").setValue("Üniversite");
        }else {
            myRef.child("Users").child(userUid).child("usertype").setValue("Lise");

        }
        myRef.child("Users").child(userUid).child("id").setValue(userUid);
        myRef.child("Users").child(userUid).child("imageurl").setValue("https://firebasestorage.googleapis.com/v0/b/mobuni-b9f65.appspot.com/o/icrax-kullan%C4%B1c%C4%B1-256x256.png?alt=media&token=75c2f010-60af-4fee-aeca-883f6436b736");
        myRef.child("Users").child(userUid).child("password") .setValue(passwordtext.getText().toString());

        if (typeSwitch.isChecked()) {
            myRef.child("Users").child(userUid).child("university").setValue(spinnerUniversiteler.getSelectedItem().toString());
            myRef.child("Users").child(userUid).child("department").setValue(spinnerBolumler.getSelectedItem().toString());

        }
        else{
            myRef.child("Users").child(userUid).child("university").setValue("null");
            myRef.child("Users").child(userUid).child("department").setValue("null");
        }
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        Toast.makeText(getApplicationContext(), "Kayıt Başarılı! Lütfen E-mail hesabınızı doğrulayın.", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        mAuth.signOut();
        startActivity(intent);
    }
}