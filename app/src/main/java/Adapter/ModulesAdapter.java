package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.crud_plug.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import Models.Module;

public class ModulesAdapter extends FirestoreRecyclerAdapter<Module,ModulesAdapter.ModuleHolder> {

    private ModulesAdapter.OnItemClickListener listener;

    public ModulesAdapter(@NonNull FirestoreRecyclerOptions<Module> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull ModulesAdapter.ModuleHolder holder, int position, @NonNull Module model) {
        holder.moduleName.setText(model.getModuleName());                                           // getting and setting data from firestore to textVies
    }

    @NonNull
    @Override
    public ModulesAdapter.ModuleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modules_list,parent,false);
        return new ModulesAdapter.ModuleHolder(view);
    }



    class ModuleHolder extends RecyclerView.ViewHolder{
        private TextView moduleName;

        public ModuleHolder(@NonNull View itemView) {
            super(itemView);
            moduleName = itemView.findViewById(R.id.textViewModuleName);

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

    public void setOnItemClickListener(ModulesAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
