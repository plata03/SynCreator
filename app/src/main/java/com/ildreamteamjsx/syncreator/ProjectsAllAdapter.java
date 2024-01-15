package com.ildreamteamjsx.syncreator;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProjectsAllAdapter extends RecyclerView.Adapter<ProjectsAllAdapter.ProjectViewHolder> {

    private OnItemClickListener onItemClickListener;
    private String fragmento;
    private int superCount;

    public ProjectsAllAdapter(String fragmento, int superCount) {
        this.fragmento = fragmento;
        this.superCount = superCount;
    }

    public void OnItemClickListener (OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ProjectsAllAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_category_item, parent, false);
        return new ProjectsAllAdapter.ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsAllAdapter.ProjectViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        private final TextView category;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_view_project_title);
            description = itemView.findViewById(R.id.text_view_project_author);
            icon = itemView.findViewById(R.id.image_view_project_icon);
            category = itemView.findViewById(R.id.text_view_project_category);
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
                                    if(document.getString("title").toLowerCase().contains(frag.toLowerCase())) {
                                        if(count == pos) {
                                            //Log.d("FirestoreData", "Category matched: " + frag);
                                            name.setText(document.getString("title"));
                                            description.setText("by "+document.getString("by"));
                                            icon.setImageResource(R.drawable.syncreatorslogo);
                                            category.setText(document.getString("category"));
                                            if(document.getString("category").equals("Games")) {
                                                category.setTextColor(ContextCompat.getColor(category.getContext(), R.color.c_games));
                                            }
                                            if(document.getString("category").equals("Art")) {
                                                category.setTextColor(ContextCompat.getColor(category.getContext(), R.color.c_art));
                                            }
                                            if(document.getString("category").equals("Video")) {
                                                category.setTextColor(ContextCompat.getColor(category.getContext(), R.color.c_video));
                                            }
                                            if(document.getString("category").equals("Audio")) {
                                                category.setTextColor(ContextCompat.getColor(category.getContext(), R.color.c_audio_2));
                                            }
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
