package com.example.crud_plug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

import Adapter.CoursesAdapter;
import Fragment.Computing_Fragment;
import Fragment.EngeeneringFragment;
import Fragment.HealthAndAppliedScienceFragment;
import Fragment.HumanScienceFragment;
import Fragment.ManagementScienceFragment;
import Fragment.Natural_ResourcesFragment;
import Models.Course;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final String COURSE_NAME = "course name";
    public static final String COURSE_ID = "course ID";
    private static final String TAG = "MainActivity";

    Button buttonAddCourse;
    //EditText editTextCourseName;


    //adapter
    private CoursesAdapter coursesAdapter;

    DatabaseReference coursesDatabase;

    //database ref
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference coursesRef = db.collection("Courses");


    List<Course> courses;
    private DrawerLayout drawer;


    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStatelistener;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


//    private void setUpRecyclerView() {
//        Query query = coursesRef.orderBy("courseName",Query.Direction.ASCENDING);
//
//        FirestoreRecyclerOptions<Course> options = new FirestoreRecyclerOptions.Builder<Course>().
//                setQuery(query,Course.class).build();
//
//        coursesAdapter = new CoursesAdapter(options);
//
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    initRecyclerView();
//                } else {
//                    Toast.makeText(getApplicationContext(),"Query not working",Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });
//
//        coursesAdapter.setOnItemClickListener(new CoursesAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                Course  course = documentSnapshot.toObject(Course.class);
//                String courseId = documentSnapshot.getId();
//                String courseName = course.getCourseName();
//
//                Intent intent = new Intent(MainActivity.this,AddModuleActivity.class);
//                intent.putExtra("courseId",courseId);
//                intent.putExtra("courseName",courseName);
//                startActivity(intent);
//                finish();
//
//                Toast.makeText(MainActivity.this,
//                        "name: " + courseName + " ID: " + courseId, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        Log.d(TAG, "setUpRecyclerView: Has finished executing");
//
//    }

//    private void initRecyclerView() {
//        RecyclerView recyclerView = findViewById(R.id.courses_recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(coursesAdapter);
//
//        coursesAdapter.startListening();
//
//    }

//
////PERFORMING A WRITE OPERATION
//    private void addNewCourse() {
//        String courseName = editTextCourseName.getText().toString().trim();
//
//        if (!TextUtils.isEmpty(courseName)){
////            String courseId = coursesDatabase.push().getKey();    // getting the primary key
//
//            Course course = new Course(courseName);
//            coursesRef.add(course).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                @Override
//                public void onSuccess(DocumentReference documentReference) {
//                    Toast.makeText(getApplicationContext(),"Course added",Toast.LENGTH_LONG).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(),"An error occurred: "+e.getMessage(),Toast.LENGTH_LONG).show();
//                }
//            });
//
//            //coursesDatabase.child(courseId).setValue(course);     //setting the course to database
//
//        }
//        else{
//            Toast.makeText(this,"Course name cannot be empty, please fill it",Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.course_one:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Computing_Fragment()).commit();
                break;
            case R.id.course_two:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EngeeneringFragment()).commit();
                break;
            case R.id.course_three:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HealthAndAppliedScienceFragment()).commit();
                break;
            case R.id.course_four:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HumanScienceFragment()).commit();
                break;
            case R.id.course_five:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ManagementScienceFragment()).commit();
            case R.id.course_six:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Natural_ResourcesFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

        if (currentUser == null) {
            Log.d(TAG, "USER IS NOT LOGGED IN ,SENDING USER TO SIGN IN ACTIVITY: ");
            startActivity(new Intent(MainActivity.this,Sign_in_Activity.class));
        }
    }
    //    @Override
//    protected void onStart() {
//
//        super.onStart();
//        if (coursesAdapter != null){
//            coursesAdapter.startListening();
//        }
//        Log.d(TAG, "onStart: Has finished executing");
//
//   }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (coursesAdapter != null){
//            coursesAdapter.stopListening();
//        }
//        Log.d(TAG, "onStop: Has finished executing");
//    }
}
