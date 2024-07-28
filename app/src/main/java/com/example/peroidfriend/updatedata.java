package com.example.peroidfriend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class updatedata extends AppCompatActivity implements AdapterUpdateData.OnItemClickListener {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private AdapterUpdateData adapter;
    private List<ModelData> dataList;
    private ImageView imgNoData;
    private TextView textNoData;
    private Button btnreturn;

    private static final int REQUEST_CODE_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedata);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        imgNoData = findViewById(R.id.imgnodata);
        textNoData = findViewById(R.id.textNoData);
        btnreturn = findViewById(R.id.btnreturn);
        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new AdapterUpdateData(dataList, this);
        recyclerView.setAdapter(adapter);

        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(updatedata.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optionally, finish this activity to remove it from the back stack
            }
        });

        // Load data from Firestore
        loadDataFromFirestore();
    }

    private void loadDataFromFirestore() {
        db.collection("periodData")
                .orderBy("startDate", Query.Direction.DESCENDING) // Order by startDate in descending order
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            // Handle the error
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : value) {
                                ModelData data = document.toObject(ModelData.class);
                                data.setDocumentId(document.getId()); // Set document ID
                                dataList.add(data);
                            }
                            adapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.VISIBLE);
                            imgNoData.setVisibility(View.GONE);
                            textNoData.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            imgNoData.setVisibility(View.VISIBLE);
                            textNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(String documentId) {
        if (documentId == null) {
            // Log error
            Log.e("updatedata", "Document ID is null");
            return;
        }

        // Log documentId
        Log.d("updatedata", "Document ID: " + documentId);

        // Find the ModelData associated with the documentId
        for (ModelData data : dataList) {
            if (documentId.equals(data.getDocumentId())) {
                // Create an Intent to start editdata activity
                Intent intent = new Intent(updatedata.this, editdata.class);
                intent.putExtra("DOCUMENT_ID", documentId);
                intent.putExtra("START_DATE", data.getStartDate());
                intent.putExtra("FINISH_DATE", data.getFinishDate());
                intent.putExtra("EXPRESSION", data.getExpression());
                intent.putExtra("PAIN_SCALE", data.getPainScale());
                startActivityForResult(intent, REQUEST_CODE_EDIT);
                return; // Exit the method once we found and used the data
            }
        }

        // Log error if documentId is not found
        Log.e("updatedata", "Document ID not found in data list: " + documentId);
    }

    @Override
    public void onDeleteClick(String documentId, int position) {
        if (documentId == null) {
            // Log error
            Log.e("updatedata", "Document ID is null");
            return;
        }

        // Log the documentId
        Log.d("updatedata", "Deleting document with ID: " + documentId);

        // Remove the document from Firestore
        db.collection("periodData").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove the item from the dataList and notify the adapter
                    dataList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Log.d("updatedata", "DocumentSnapshot successfully deleted!");

                    // Redirect to MainActivity
                    Intent intent = new Intent(updatedata.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Optionally, finish this activity to remove it from the back stack
                })
                .addOnFailureListener(e -> Log.w("updatedata", "Error deleting document", e));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("DOCUMENT_ID")) {
                String documentId = data.getStringExtra("DOCUMENT_ID");
                Log.d("updatedata", "Returned document ID: " + documentId);
            }
            // Refresh data after returning from editdata
            loadDataFromFirestore();
        }
    }
}
