package Adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.crud_plug.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import Models.Course;


public class CoursesAdapter extends FirestoreRecyclerAdapter<Course,CoursesAdapter.CourseHolder> {

    private OnItemClickListener listener;


    public CoursesAdapter(@NonNull FirestoreRecyclerOptions<Course> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CourseHolder holder, int position, @NonNull Course model) {
        holder.courseName.setText(model.getCourseName());                                           // getting and setting data from firestore to textVies

    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_list,parent,false);
        return new CourseHolder(view);
    }


 class CourseHolder extends RecyclerView.ViewHolder{

     private TextView courseName;

     public CourseHolder(@NonNull View itemView) {
         super(itemView);
         courseName = itemView.findViewById(R.id.textViewCourseName);

         itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 int position = getAdapterPosition();

                 //SETTING THE ON ITEM CLICK LISTENER
                 if (position != RecyclerView.NO_POSITION && listener != null) {
                     listener.onItemClick(getSnapshots().getSnapshot(position), position);
                 }
             }
         });
     }


 }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
