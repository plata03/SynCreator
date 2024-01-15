package com.ildreamteamjsx.syncreator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton searchBtn = view.findViewById(R.id.search_btn);

        searchBtn.setOnClickListener(v -> {

            EditText query = view.findViewById(R.id.search_result);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Project")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<QueryDocumentSnapshot> currentFieldList = new ArrayList<>();
                            int count = 0;
                            TextView isfound = view.findViewById(R.id.notfound);

                            if(task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getString("title").toLowerCase().contains(query.getText().toString().toLowerCase())) {
                                        count++;
                                        currentFieldList.add(document);
                                    }
                                }
                                if(count == 0) {
                                    isfound.setVisibility(View.VISIBLE);
                                } else {
                                /*Log.d("Firestore", "Collection: "+currentFieldList.get(0).getString("title"));
                                Log.d("Firestore", "Collection: "+currentFieldList.get(1).getString("title"));
                                Log.d("Firestore", "Collection: "+currentFieldList.get(2).getString("title"));*/
                                    isfound.setVisibility(View.INVISIBLE);
                                    RecyclerView recyclerView = view.findViewById(R.id.recycler_view_projects);
                                    ProjectsAllAdapter projectAdapter = new ProjectsAllAdapter(query.getText().toString(), count);
                                    recyclerView.setAdapter(projectAdapter);

                                    projectAdapter.OnItemClickListener((view, position) -> {
                                        //Log.d("Firestore", "Collection after click: "+currentFieldList.get(0).getString("title"));
                                        //Log.d("Firestore", "Collection after click: "+currentFieldList.get(1).getString("title"));
                                        //Log.d("Firestore", "Collection after click: "+currentFieldList.get(2).getString("title"));
                                        //Log.d("Firestore", "Collection SELECTED: "+currentFieldList.get(position).getString("title"));
                                        //Log.e("Firestore", "Invalid position index: " + position);

                                        Intent intento = new Intent(SearchActivity.this.getActivity(), ProjectDetailActivity.class);

                                        intento.putExtra("title", currentFieldList.get(position).getString("title"));
                                        intento.putExtra("desc", currentFieldList.get(position).getString("desc"));
                                        intento.putExtra("by", currentFieldList.get(position).getString("by"));
                                        intento.putExtra("category", currentFieldList.get(position).getString("category"));

                                        SearchActivity.this.startActivity(intento);

                                    });
                                }



                            }

                        }
                    });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}