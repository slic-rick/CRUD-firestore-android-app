package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crud_plug.AddModuleActivity;
import com.example.crud_plug.MainActivity;
import com.example.crud_plug.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import Adapter.CoursesAdapter;
import Models.Course;

public class Computing_Fragment extends Fragment {

    private static final String TAG = "Computing_Fragment";
    private RecyclerView recyclerView;
    private CoursesAdapter coursesAdapter;

    private int selected;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference coursesRef = db.collection("Courses").document(
            "NUST_courses").collection("Computing and Informatics");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.computing_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.computing_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        selected = 1;
        setUpRecyclerView();
        return  v;
    }

    private void setUpRecyclerView() {
        Query query = coursesRef.orderBy("courseName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Course> options = new FirestoreRecyclerOptions.Builder<Course>().
                setQuery(query,Course.class).build();

        coursesAdapter = new CoursesAdapter(options);
        recyclerView.setAdapter(coursesAdapter);



        coursesAdapter.setOnItemClickListener(new CoursesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Course  course = documentSnapshot.toObject(Course.class);
                String courseId = documentSnapshot.getId();
                String courseName = course.getCourseName();


                Intent intent = new Intent(getActivity(), AddModuleActivity.class);
                intent.putExtra("courseId",courseId);
                intent.putExtra("courseName",courseName);
                intent.putExtra("selected",selected);
                startActivity(intent);


                Toast.makeText(getActivity(),
                        "name: " + courseName + " ID: " + courseId, Toast.LENGTH_SHORT).show();
            }
        });


        Log.d(TAG, "setUpRecyclerView: Has finished executing");

    }

    private void initRecyclerView() {
    }

    @Override
    public void onStart() {
        super.onStart();

        coursesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        coursesAdapter.stopListening();
    }
}
