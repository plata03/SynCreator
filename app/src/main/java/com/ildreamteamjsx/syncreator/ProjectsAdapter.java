package com.ildreamteamjsx.syncreator;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>{

    private OnItemClickListener onItemClickListener;
    private String fragmento;
    private int superCount;

    public ProjectsAdapter(String fragmento, int superCount) {
        this.fragmento = fragmento;
        this.superCount = superCount;
    }

    public void OnItemClickListener (OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_layout_items, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(fragmento, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(v.getContext(),
                        R.anim.button_press_animation);
                holder.itemView.startAnimation(animation);

                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return superCount;
    }


    static class ProjectViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView description;
        private final ImageView icon;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_view_project_title);
            description = itemView.findViewById(R.id.text_view_project_author);
            icon = itemView.findViewById(R.id.image_view_project_icon);
        }

        public void bind(String frag, int pos) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Project")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                int count = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("FirestoreData", "Document ID: " + document.getId());
                                    if(frag.equals(document.getString("category"))) {
                                        if(count == pos) {
                                            Log.d("FirestoreData", "Category matched: " + frag);
                                            name.setText(document.getString("title"));
                                            description.setText("by "+document.getString("by"));
                                            icon.setImageResource(R.drawable.syncreatorslogo);
                                            break;
                                        }
                                        count++;

                                    }
                                }

                            }
                        }
                    });
        }
    }

}
