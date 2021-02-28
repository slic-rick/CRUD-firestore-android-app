package com.example.crud_plug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapter.TasksAdapter;
import Fragment.Computing_Fragment;
import Fragment.EngeeneringFragment;
import Fragment.HealthAndAppliedScienceFragment;
import Fragment.HumanScienceFragment;
import Fragment.ManagementScienceFragment;
import Fragment.Natural_ResourcesFragment;
import Models.Task;

public class AddTaskActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "AddTaskActivity";

    public static boolean btn_thisWeek = false;
    public static boolean btn_nextWeek = false;

    private Button thisWeek_btn, nextweek_btn;
    private RecyclerView recyclerView;
    private TextView title;
    FloatingActionButton addTask;

    private DrawerLayout drawer;
    List<Task> tasks;

    TasksAdapter tasksAdapter;


    String moduleID = "";
    String courseName ="";
    String moduleName ="";
    String courseId = "";

    //database ref
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference taskRef;
    //= db.collection("Tasks");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar toolbar = findViewById(R.id.toolbar_tasks);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Tasks");

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        nextweek_btn = findViewById(R.id.btn_next_week_tasks);
        thisWeek_btn= findViewById(R.id.btn_this_week_task);
        recyclerView = findViewById(R.id.tasks_recyclerview);
        title = findViewById(R.id.tasks_title);
        addTask = findViewById(R.id.button_add_task);

        title.setText("This Week Tasks");

        // getting data from modulesActivity;
        Intent intent = getIntent();
        String moduleId = intent.getStringExtra("moduleID");
        String userModuleName = intent.getStringExtra("moduleName");
        String userCourseName = intent.getStringExtra("courseName");
        String userCourseID = intent.getStringExtra("courseId");

        this.courseName = userCourseName;
        this.courseId = userCourseID;
        this.moduleName = userModuleName;
        this.moduleID = moduleId;

        addTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(AddTaskActivity.this,NewTaskActivity.class);
                    intent.putExtra("courseId",courseId);
                    intent.putExtra("courseName",courseName);
                    intent.putExtra("moduleName",moduleName);
                    intent.putExtra("moduleId",moduleID);
                    startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasks = new ArrayList<>();

        thisWeek_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title.setText("This Week Tasks");

                thisWeek_btn();
            }
        });

        nextweek_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
        title.setText("Next Week Tasks");
                nextweek_btn();
            }
        });

        setUpRef();

        //getting tasks from

        //READING ALL MODULES UNDER COURSE FROM CLOUD
        Query query = taskRef.whereEqualTo("moduleId",moduleID).orderBy("dueDay", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>().
                setQuery(query,Task.class).build();

        tasksAdapter = new TasksAdapter(options);
        recyclerView.setAdapter(tasksAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                tasksAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }



    private void thisWeek_btn() {

        Log.d(TAG, "thisWeek_btn: BUTTON CLICKED");

        setUpRef();

        //READING ALL MODULES UNDER COURSE FROM CLOUD
        Query query = taskRef.whereEqualTo("moduleId",moduleID).orderBy("dueDay", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>().
                setQuery(query,Task.class).build();

        tasksAdapter = new TasksAdapter(options);
        recyclerView.setAdapter(tasksAdapter);
        tasksAdapter.startListening();

        Log.d(TAG, "thisWeek_btn: moduleId: "+moduleID);

    }

    private void nextweek_btn() {
        Log.d(TAG, "nextweek_btn: BUTTON CLICKED");

        setUpRefWeek2();

        //READING ALL MODULES UNDER COURSE FROM CLOUD
        Query query = taskRef.whereEqualTo("moduleId",moduleID).orderBy("dueDay", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>().
                setQuery(query,Task.class).build();

        tasksAdapter = new TasksAdapter(options);
        recyclerView.setAdapter(tasksAdapter);
        tasksAdapter.startListening();

        Log.d(TAG, "nextweek_btn: moduleId: "+moduleID);

    }

    private void setUpRefWeek2() {
        Log.d(TAG, "setUpRefWeek2: next week CLOUD REFERENCE INITIALISED");

        taskRef = db.collection("Tasks").document("Week 2").collection("NextWeek");
    }

    private void setUpRef() {

        Log.d(TAG, "setUpRef: this week CLOUD REFERENCE INITIALISED");

        taskRef = db.collection("Tasks").document("Week 1").collection("ThisWeek");
    }

//    private void setUpRecyclerView() {
//
//        //READING ALL MODULES UNDER COURSE FROM CLOUD
//        Query query = taskRef.whereEqualTo("moduleId",moduleID).orderBy("dueDay", Query.Direction.DESCENDING);
//
//        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>().
//                setQuery(query,Task.class).build();
//
//        tasksAdapter = new TasksAdapter(options);
//        tasksAdapter.startListening();
//
//
//
////        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////            @Override
////            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
////                if (task.isSuccessful()){
////                    initRecyclerView();
////                } else {
////                    Toast.makeText(getApplicationContext(),"Query not working",Toast.LENGTH_LONG).show();
////                }
////
////            }
////        });
//
//
//
//    }

    @Override
    protected void onStart() {
        super.onStart();

        if (tasksAdapter != null){
            tasksAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (tasksAdapter != null) {
            tasksAdapter.stopListening();
        }
    }

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
}

