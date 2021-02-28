package com.example.crud_plug;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import Fragment.TimePickerFragment;
import Models.Task;

public class NewTaskActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "NewTaskActivity";

    private EditText taskInstructions;
    private Spinner taskWeek,taskDueDay;
    private Button dueTime;


    private String courseId,courseName,moduleId,moduleName;
    private String taskDueAt = "";
    TextView taskDueTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        taskInstructions = findViewById(R.id.task_instructions);
        taskWeek = findViewById(R.id.weekSpinner);
        taskDueDay = findViewById(R.id.task_daySpinner);
        dueTime = findViewById(R.id.task_due_time);
        taskDueTime = findViewById(R.id.timeTextView);

        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseId");
        courseName = intent.getStringExtra("courseName");
        moduleId = intent.getStringExtra("moduleId");
        moduleName = intent.getStringExtra("moduleName");

        dueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"Choose the due time");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_task:
                saveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveTask() {

        String instruction = taskInstructions.getText().toString();
        String weekCategory = taskWeek.getSelectedItem().toString();
        String taskDueDay =  this.taskDueDay.getSelectedItem().toString();

        if (instruction.trim().isEmpty() || taskDueAt.isEmpty()){
            Toast.makeText(this, "Please insert the Task's instruction and pick the due time: ", Toast.LENGTH_LONG).show();
            return;
        }

        if (!weekCategory.isEmpty() && weekCategory.equalsIgnoreCase("This Week")){

            CollectionReference thisWeekTask = FirebaseFirestore.getInstance().
                    collection("Tasks").document("Week 1")
                    .collection("ThisWeek");

            thisWeekTask.add(new Task(courseId,courseName,moduleId,moduleName,instruction,taskDueDay,taskDueAt))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(NewTaskActivity.this, "Task Saved uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(NewTaskActivity.this, "An error occured "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        }
        else if (!weekCategory.isEmpty() && weekCategory.equalsIgnoreCase("Next Week")){

            CollectionReference nextWeekRef = FirebaseFirestore.getInstance().
                    collection("Tasks").document("Week 2")
                    .collection("NextWeek");

            nextWeekRef.add(new Task(courseId,courseName,moduleId,moduleName,instruction,taskDueDay,taskDueAt))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(NewTaskActivity.this, "Task Saved uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewTaskActivity.this, "An error occured "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        else{

            Toast.makeText(this, "WEEK CATEGORY IS EMPTY, internal error", Toast.LENGTH_SHORT).show();
        }

        }




    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        taskDueTime.setText("Task due at: "+hourOfDay+"H"+minute);
            taskDueAt = ""+hourOfDay+"H"+minute;

        Log.d(TAG, "onTimeSet: DUE AT: "+ taskDueAt);

    }
}
