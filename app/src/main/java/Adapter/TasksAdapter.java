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
import Models.Task;

public class TasksAdapter extends FirestoreRecyclerAdapter<Task,TasksAdapter.TaskHolder> {


    public TasksAdapter(@NonNull FirestoreRecyclerOptions<Task> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull TasksAdapter.TaskHolder holder,int position, @NonNull Task model) {

        holder.moduleName.setText(model.getModuleName());                                           //Binding data
        holder.instructions.setText(model.getInstruction());
        holder.dueTime.setText(model.getDueTime());
        holder.dueDay.setText(model.getDueDay());

    }

    @NonNull
    @Override
    public TasksAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_list,parent,false);
        return new TasksAdapter.TaskHolder(view);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    class TaskHolder extends RecyclerView.ViewHolder{
        private TextView instructions;
        private TextView moduleName;
        private TextView dueTime;
        private TextView dueDay;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            moduleName = itemView.findViewById(R.id.textViewModuleName);
            instructions = itemView.findViewById(R.id.textViewInstructions);
            dueTime = itemView.findViewById(R.id.textViewdueTime);
            dueDay = itemView.findViewById(R.id.textViewdueDay);

        }

    }

}
