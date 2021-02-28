package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crud_plug.AddModuleActivity;
import com.example.crud_plug.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import Adapter.CoursesAdapter;
import Models.Course;

public class Natural_ResourcesFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference coursesRef = db.collection("Courses").document(
            "NUST_courses").collection("Natural Resources and Spatial Sciences");

    private int selected;



    private RecyclerView recyclerView;
    private CoursesAdapter coursesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.natural_resources_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.natural_resources_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        selected = 6;

        setUpRecyclerView();
        return  v;
    }

    private void setUpRecyclerView() {
        Query query = coursesRef.orderBy("courseName",Query.Direction.ASCENDING);

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
