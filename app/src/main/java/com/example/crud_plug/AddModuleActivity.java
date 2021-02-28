package com.example.crud_plug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;


import Adapter.CoursesAdapter;
import Adapter.ModulesAdapter;

import Fragment.Computing_Fragment;
import Fragment.EngeeneringFragment;
import Fragment.HealthAndAppliedScienceFragment;
import Fragment.HumanScienceFragment;
import Fragment.ManagementScienceFragment;
import Fragment.Natural_ResourcesFragment;
import Models.Course;
import Models.Module;

public class AddModuleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MODULE_NAME = "module name";
    public static final String MODULE_ID = "module ID";
    private static final String TAG = "AddModuleActivity";

//    TextView courseName;
//    EditText editTextModuleName;
//    Button buttonAddModule;
//    String courseId;
//    Toolbar toolbar;
//    Spinner spinner;

    private TextView choose, fieldName,instruction;
    private Button yearOne, yearTwo, yearThree, yearFour;
    private RecyclerView mRecyclerView;
    private DrawerLayout drawer;

    List<Module> modules;
    String moduleYear = "";


    //ADAPTER DECLARATION
     ModulesAdapter modulesAdapter;


    String userCourseName = "";
    private String courseId;
    private int selectedField;


    //database ref
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference coursesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate: METHOD CALLED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);

        Toolbar toolbar = findViewById(R.id.toolbar_modules);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Load Modules");

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        mRecyclerView = (RecyclerView) findViewById(R.id.add_modules_recyclerView);
        yearOne = (Button) findViewById(R.id.year_1);
        yearTwo = (Button) findViewById(R.id.year_2);
        yearThree = (Button) findViewById(R.id.year_3);
        yearFour = (Button) findViewById(R.id.year_4);

        choose =    (TextView) findViewById(R.id.textView_chooseModules);
        fieldName = (TextView) findViewById(R.id.textView_field_name);
        instruction = (TextView) findViewById(R.id.textView_instruction);

        instruction.setVisibility(View.GONE);

        modules = new ArrayList<>();

        //getting ID and name from CourseActivity
        Intent intent = getIntent();
        String courseID = intent.getStringExtra("courseId");
        String courseName = intent.getStringExtra("courseName");
        selectedField = intent.getIntExtra("selected", 0);

        this.courseId = courseID;
        this.userCourseName = courseName;

        Log.d(TAG, "onCreate: COURSE NAME:   "+ userCourseName);
        //choose.setText("Choose "+userCourseName+" modules according to their years");
        fieldName.setText(userCourseName);

        setUpCollection();
        yearOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (instruction.getVisibility() == View.GONE){
                    instruction.setVisibility(View.VISIBLE);
                }
                yearOne();
            }
        });

        yearTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (instruction.getVisibility() == View.GONE){
                    instruction.setVisibility(View.VISIBLE);
                }
                yearTwo();
            }
        });

        yearThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instruction.getVisibility() == View.GONE){
                    instruction.setVisibility(View.VISIBLE);
                }
                yearThree();
            }
        });

        yearFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (instruction.getVisibility() == View.GONE){
                    instruction.setVisibility(View.VISIBLE);
                }
                yearFour();
            }
        });

        
        if (coursesRef != null){
            //setUpRecyclerView();
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
           // mRecyclerView.setAdapter(modulesAdapter);
            Log.d(TAG, "onCreate: COURSE REF IS INITILALISED");
            
        } else{
            Log.d(TAG, "onCreate: COURSE TEF IS NOT INITIALISED");
            Toast.makeText(this, "Collection is not initialised, can't get modules", Toast.LENGTH_SHORT).show();
        }



    }

    private void yearFour() {
        Log.d(TAG, "yearFour: Button clicked");
        Query query = coursesRef.document(this.courseId).collection("Modules")
                .whereEqualTo("yearCategory",4)
                .orderBy("moduleName", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Module> options = new FirestoreRecyclerOptions.Builder<Module>().
                setQuery(query, Module.class).build();

        modulesAdapter = new ModulesAdapter(options);
        mRecyclerView.setAdapter(modulesAdapter);
        modulesAdapter.startListening();

        modulesAdapter.setOnItemClickListener(new ModulesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Module module = documentSnapshot.toObject(Module.class);
                String moduleId = documentSnapshot.getId();
                String moduleName = module.getModuleName();


                Intent intent = new Intent(AddModuleActivity.this, AddTaskActivity.class);

                intent.putExtra("courseId",courseId);
                intent.putExtra("courseName",userCourseName);
                intent.putExtra("moduleID",moduleId);
                intent.putExtra("moduleName",moduleName);
                startActivity(intent);
                finish();

                Toast.makeText(AddModuleActivity.this, "module " +moduleName +" \nmodule id: "+moduleId, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void yearThree() {
        Log.d(TAG, "yearThree: YEAR THREE BUTTON CLICKED");
        Query query = coursesRef.document(this.courseId).collection("Modules")
                .whereEqualTo("yearCategory",3)
                .orderBy("moduleName", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Module> options = new FirestoreRecyclerOptions.Builder<Module>().
                setQuery(query, Module.class).build();

        ModulesAdapter modulesAdapter = new ModulesAdapter(options);
        mRecyclerView.setAdapter(modulesAdapter);
        modulesAdapter.startListening();

        modulesAdapter.setOnItemClickListener(new ModulesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Module module = documentSnapshot.toObject(Module.class);
                String moduleId = documentSnapshot.getId();
                String moduleName = module.getModuleName();


                Intent intent = new Intent(AddModuleActivity.this, AddTaskActivity.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("courseName",userCourseName);
                intent.putExtra("moduleID",moduleId);
                intent.putExtra("moduleName",moduleName);
                startActivity(intent);
                finish();

                Toast.makeText(AddModuleActivity.this, "module " +moduleName +" \nmodule id: "+moduleId, Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void yearTwo() {

        Log.d(TAG, "yearTwo: YEAR TWO BUTTON CLICKED");
        Query query = coursesRef.document(this.courseId).collection("Modules").whereEqualTo("yearCategory",2).orderBy("moduleName", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Module> options = new FirestoreRecyclerOptions.Builder<Module>().
                setQuery(query, Module.class).build();

        modulesAdapter = new ModulesAdapter(options);
        mRecyclerView.setAdapter(modulesAdapter);
        modulesAdapter.startListening();

        modulesAdapter.setOnItemClickListener(new ModulesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Module module = documentSnapshot.toObject(Module.class);
                String moduleId = documentSnapshot.getId();
                String moduleName = module.getModuleName();


                Intent intent = new Intent(AddModuleActivity.this, AddTaskActivity.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("courseName",userCourseName);
                intent.putExtra("moduleID",moduleId);
                intent.putExtra("moduleName",moduleName);
                startActivity(intent);
                finish();

                Toast.makeText(AddModuleActivity.this, "module " +moduleName +" \nmodule id: "+moduleId, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void yearOne() {

        Log.d(TAG, "yearOne: YEAR ONE BUTTON CLICKED "+ courseId);


        Query query = coursesRef.document(courseId).collection("Modules").
                whereEqualTo("yearCategory",1)
                .orderBy("moduleName", Query.Direction.DESCENDING);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot dc :queryDocumentSnapshots){
                     Module module = dc.toObject(Module.class);
                    Log.d(TAG, "onSuccess:  QUERY WORKING "+ module.getModuleName());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: An error occurred "+ e.getMessage());
                Toast.makeText(AddModuleActivity.this, "An error occurred "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        

        FirestoreRecyclerOptions<Module> options = new FirestoreRecyclerOptions.Builder<Module>().
                setQuery(query, Module.class).build();

        modulesAdapter = new ModulesAdapter(options);
        mRecyclerView.setAdapter(modulesAdapter);


        Log.d(TAG, "yearOne: courseID "+courseId);
        modulesAdapter.startListening();

        modulesAdapter.setOnItemClickListener(new ModulesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Module module = documentSnapshot.toObject(Module.class);
                String moduleId = documentSnapshot.getId();
                String moduleName = module.getModuleName();


                Intent intent = new Intent(AddModuleActivity.this, AddTaskActivity.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("courseName",userCourseName);
                intent.putExtra("moduleID",moduleId);
                intent.putExtra("moduleName",moduleName);
                startActivity(intent);
                finish();

                Toast.makeText(AddModuleActivity.this, "module " +moduleName +" \nmodule id: "+moduleId, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    private void setUpRecyclerView() {

        //READING ALL MODULES UNDER COURSE FROM CLOUD
        //Query query = coursesRef.document(this.courseId).collection("Modules").orderBy("moduleName", Query.Direction.DESCENDING);

        Query query = coursesRef.document(this.courseId).collection("Modules").orderBy("moduleName", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Module> options = new FirestoreRecyclerOptions.Builder<Module>().
                setQuery(query, Module.class).build();

        ModulesAdapter modulesAdapter = new ModulesAdapter(options);

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return true;
    }

    @Override
    protected void onStart() {

        super.onStart();
//        if (modulesAdapter != null) {
//            modulesAdapter.startListening();
//            Log.d(TAG, "onStart: MODULES ADAPTER STARTED LISTENING");
//        }


    }

    @Override
    protected void onStop() {
        super.onStop();

//        if (modulesAdapter != null) {
//            modulesAdapter.stopListening();
//            Log.d(TAG, "onStop: MODULES ADAPTER STOPPED LISTENING");
//        }

    }
    
    
    private void setUpCollection() {

        Log.d(TAG, "setUpCollection: METHOD CALLED");

        if (selectedField != 0) {
            if (selectedField == 1) {
                Log.d(TAG, "setUpCollection: COMPUTING_COURSES COLLECTION REF INITIALISED");
                coursesRef = db.collection("Courses").document(
                        "NUST_courses").collection("Computing and Informatics");
            } else if (selectedField == 2) {
                Log.d(TAG, "setUpCollection: ENGINEERING_COURSES COLLECTION REF INITIALISED");
                coursesRef = db.collection("Courses").document(
                        "NUST_courses").collection("Engineering Sciences");
            } else if (selectedField == 3) {
                Log.d(TAG, "setUpCollection: HEALTH AND APPLIED_COURSES COLLECTION REF INITIALISED");
                coursesRef = db.collection("Courses").document(
                        "NUST_courses").collection("Health and Applied Sciences");

            } else if (selectedField == 4) {
                Log.d(TAG, "setUpCollection: HUMAN SCIENCES_COURSES COLLECTION REF INITIALISED");
                coursesRef = db.collection("Courses").document(
                        "NUST_courses").collection("Human Sciences");

            } else if (selectedField == 5) {
                Log.d(TAG, "setUpCollection: MANAGEMENT SCIENCES_COURSES COLLECTION REF INITIALISED");

                coursesRef = db.collection("Courses").document(
                        "NUST_courses").collection("Management Sciences");

            } else if (selectedField == 6) {
                Log.d(TAG, "setUpCollection: NATURAL RESOURCES_COURSES COLLECTION REF INITIALISED");
                coursesRef = db.collection("Courses").document(
                        "NUST_courses").collection("Natural Resources and Spatial Sciences");

            } else {
                Log.d(TAG, "setUpCollection: DIDN'T GET THE RIGHT SELECTED NUMBER!!!");
                Toast.makeText(this, "setUpCollection: DIDN'T GET THE RIGHT SELECTED FIELD NUMBER!!!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "setUpCollection: SELECTED FIELD IS NULL, ERROR OCCURRED, Collection NOT INIT..");
            Toast.makeText(this, "Collection  ref not initialised SELECTED FIELD == 0", Toast.LENGTH_SHORT).show();
        }
    }
}
