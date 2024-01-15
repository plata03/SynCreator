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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ArtActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_art, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Project")
                .whereEqualTo("category", "Art")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore", "Listen failed.", error);
                            return;
                        }

                        ArrayList<QueryDocumentSnapshot> currentFieldList = new ArrayList<>();
                        int count = 0;

                        for (QueryDocumentSnapshot document : value) {
                            count++;
                            currentFieldList.add(document);
                        }

                        //Log.d("Firestore", "Collection: " + currentFieldList.get(0).getString("title"));
                        //Log.d("Firestore", "Collection: " + currentFieldList.get(1).getString("title"));
                        //Log.d("Firestore", "Collection: " + currentFieldList.get(2).getString("title"));

                        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_projects);
                        ProjectsAdapter projectAdapter = new ProjectsAdapter("Art", count);
                        recyclerView.setAdapter(projectAdapter);

                        projectAdapter.OnItemClickListener((view, position) -> {
                            //Log.d("Firestore", "Collection after click: "+currentFieldList.get(0).getString("title"));
                            //Log.d("Firestore", "Collection after click: "+currentFieldList.get(1).getString("title"));
                            //Log.d("Firestore", "Collection after click: "+currentFieldList.get(2).getString("title"));
                            //Log.d("Firestore", "Collection SELECTED: "+currentFieldList.get(position).getString("title"));
                            //Log.e("Firestore", "Invalid position index: " + position);

                            Intent intento = new Intent(ArtActivity.this.getActivity(), ProjectDetailActivity.class);

                            intento.putExtra("title", currentFieldList.get(position).getString("title"));
                            intento.putExtra("desc", currentFieldList.get(position).getString("desc"));
                            intento.putExtra("by", currentFieldList.get(position).getString("by"));
                            intento.putExtra("category", currentFieldList.get(position).getString("category"));

                            ArtActivity.this.startActivity(intento);

                        });
                    }
                });
        //BUTTON ONCLICKLISTENER HERE!!!
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}