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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HighschoolMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private ViewPager viewpagermain;
    private TabLayout tableMain;
    private TabsAdappterSCH tabsadaptersch;
    Boolean intentSuccesful = false;
    TextView userName;
    ImageView profileImage;

    FloatingActionButton newQuesiton;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private void init(){
        toolbar = findViewById(R.id.toolbarhighschoolmain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MobUni Lise");
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        viewpagermain =(ViewPager) findViewById(R.id.viewmainschid);
        tabsadaptersch = new TabsAdappterSCH(getSupportFragmentManager());
        viewpagermain.setAdapter(tabsadaptersch);

        tableMain = (TabLayout) findViewById(R.id.tabshighschoolmainid);
        tableMain.setupWithViewPager(viewpagermain);

        userName = findViewById(R.id.nameHighschool);
        userName.setText("Lise");
        profileImage = findViewById(R.id.imageViewHighschool);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highschool_main);
        init();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        viewProfile();
        Bundle intent = getIntent().getExtras();
        if(intent != null){
            intentSuccesful = intent.getBoolean("intent");
            if(intentSuccesful)
                Snackbar.make(viewpagermain, "Bilgi Alınmak İstenen Üniversite ve Bölüm Güncellendi", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }
        newQuesiton = findViewById(R.id.fabsch);

        viewpagermain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                changeFab(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void changeFab(final int currentTabPosition) {

        switch (currentTabPosition) {
            case 0:
                newQuesiton.setVisibility(View.VISIBLE);
                newQuesiton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), NewQuestionHighschoolActivity.class);
                        startActivity(intent);
                    }
                });
                break;

            case 1:
                newQuesiton.setVisibility(View.GONE);
                break;

            default:
                newQuesiton.setVisibility(View.VISIBLE);
                break;
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
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            startActivity(new Intent(HighschoolMainActivity.this, ProfileActivity.class));
        } else if (id == R.id.tools) {
            Intent intent = new Intent(HighschoolMainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.exit) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setIcon()
            builder.setCancelable(false);
            builder.setMessage("Hesaptan Çıkış Yapmak istiyor musunuz?");

            builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                @Override            public void onClick(DialogInterface dialog, int which) {

                    auth.signOut();
                    editor.putBoolean("login",false);
                    startActivity(new Intent(HighschoolMainActivity.this, MainActivity.class));
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
            editor.commit();

        } else if (id == R.id.about) {

        } else if (id == R.id.search) {
            startActivity(new Intent(HighschoolMainActivity.this, SearchActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void selectUniversity(View view){
        startActivity(new Intent(HighschoolMainActivity.this, HighschoolSelectUniversityActivity.class));
    }
    private void viewProfile() {
        final FirebaseUser user =auth.getCurrentUser();
        if(user != null){
            databaseReference = firebaseDatabase.getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   userName.setText(dataSnapshot.child(user.getUid()).child("fullname").getValue().toString());
                    Picasso.get().load(dataSnapshot.child(user.getUid()).child("imageurl").getValue().toString()).into(profileImage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
