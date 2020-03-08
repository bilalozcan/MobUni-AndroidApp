package com.fmbg.moobuni;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UniversityMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth currentAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ImageView imageViewUniversity;
    private Toolbar toolbar;
    private ViewPager viewpagermain;
    private TabLayout tableMain;
    private TabsAdappterUNI tabsadapter;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public TextView universityName, departmentName;

    FloatingActionButton fab;

    private void init(){
        toolbar = findViewById(R.id.toolbaruniversitymain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle =toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("MobUni Üniversite");
        viewpagermain =(ViewPager) findViewById(R.id.viewmain2id);
        tabsadapter = new TabsAdappterUNI(getSupportFragmentManager());
        viewpagermain.setAdapter(tabsadapter);

        tableMain = (TabLayout) findViewById(R.id.tabsuniversitymainid);
        tableMain.setupWithViewPager(viewpagermain);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
/*
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Uni_AKIS_Fragment()).commit();

        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Uni_AKIS_Fragment()).commit();

        }*/
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_main);
        init();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        universityName = findViewById(R.id.univeresityName);
        departmentName = findViewById(R.id.departmentName);
        imageViewUniversity = findViewById(R.id.imageViewUniversity);
        currentAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        viewProfile();
        viewpagermain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                changeFab(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewPostActivity.class);
                startActivity(intent);
            }
        });

    }
    @SuppressLint("RestrictedApi")
    private void changeFab(final int currentTabPosition) {

        switch (currentTabPosition) {
            case 0:
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), NewQuestionUniversityActivity.class);
                        startActivity(intent);
                    }
                });
                break;

            case 1:
                fab.setVisibility(View.GONE);
                break;

            case 2:
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), NewPostActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            default:
                fab.setVisibility(View.VISIBLE);
                break;
        }

    }
    private void viewProfile() {
        final FirebaseUser user =currentAuth.getCurrentUser();
        if(user != null){
            databaseReference = firebaseDatabase.getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   universityName.setText(dataSnapshot.child(user.getUid()).child("fullname").getValue().toString());
                   departmentName.setText(dataSnapshot.child(user.getUid()).child("department").getValue().toString());
                    Picasso.get().load(dataSnapshot.child(user.getUid()).child("imageurl").getValue().toString()).into(imageViewUniversity);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setIcon()
            builder.setCancelable(false);
            builder.setMessage("Uygulamadan Çıkış Yapmak istiyor musunuz?");
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
            //super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            startActivity( new Intent(UniversityMainActivity.this, ProfileActivity.class));

            // Handle the camera action
        } else if (id == R.id.tools) {
            Intent intent = new Intent(UniversityMainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.exit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setIcon()
            builder.setCancelable(false);
            builder.setMessage("Hesaptan Çıkış Yapmak istiyor musunuz?");

            builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                @Override            public void onClick(DialogInterface dialog, int which) {
                    currentAuth.signOut();
                    editor.putBoolean("login",false);
                    editor.putString("university_name",null);
                    editor.putString("department_name",null);
                    editor.commit();
                    startActivity(new Intent(UniversityMainActivity.this, MainActivity.class));
                    finish();
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

        } else if (id == R.id.about) {
            //startActivity(new Intent(UniversityMainActivity.this, AboutActivity.class));

        } else if (id == R.id.search) {
            startActivity(new Intent(UniversityMainActivity.this, SearchActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void message(View view){
        Intent intent = new Intent(getApplicationContext(),MessageUniversityActivity.class);
        startActivity(intent);
    }
}
