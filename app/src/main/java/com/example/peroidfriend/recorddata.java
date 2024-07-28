package com.example.peroidfriend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class recorddata extends AppCompatActivity {
    private TableLayout tableLayout;
    private FirebaseFirestore db;
    private Button btnbalik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorddata);
        tableLayout = findViewById(R.id.tableperiod);
        db = FirebaseFirestore.getInstance();
        btnbalik = findViewById(R.id.btnreturn);

        btnbalik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(recorddata.this, MainActivity.class);
                startActivity(intent);
            }
        });

        fetchData();
    }

    private void fetchData() {
        CollectionReference periodDataRef = db.collection("periodData");
        periodDataRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    tableLayout.removeAllViews(); // Clear previous data
                    addHeaderRow(); // Add header row

                    List<QueryDocumentSnapshot> documents = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        documents.add(document);
                    }

                    Collections.sort(documents, new Comparator<QueryDocumentSnapshot>() {
                        @Override
                        public int compare(QueryDocumentSnapshot doc1, QueryDocumentSnapshot doc2) {
                            LocalDate date1 = LocalDate.parse(doc1.getString("startDate"), DateTimeFormatter.ISO_DATE);
                            LocalDate date2 = LocalDate.parse(doc2.getString("startDate"), DateTimeFormatter.ISO_DATE);
                            return date1.compareTo(date2);
                        }
                    });

                    for (int i = 0; i < documents.size(); i++) {
                        QueryDocumentSnapshot document = documents.get(i);
                        String menstruationStatus = calculateMenstruationStatus(documents, i);
                        addRowToTable(document, menstruationStatus);
                    }
                } else {
                    Log.d("Firestore", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void addHeaderRow() {
        TableRow headerRow = new TableRow(this);

        TextView header1 = createHeaderTextView("Start Date");
        TextView header2 = createHeaderTextView("Finish Date");
        TextView header3 = createHeaderTextView("Menstruation Status");

        headerRow.addView(header1);
        headerRow.addView(createDivider());
        headerRow.addView(header2);
        headerRow.addView(createDivider());
        headerRow.addView(header3);
        tableLayout.addView(headerRow);
    }

    private void addRowToTable(QueryDocumentSnapshot document, String menstruationStatus) {
        TableRow tableRow = new TableRow(this);

        TextView textView1 = createTextView(document.getString("startDate"));
        TextView textView2 = createTextView(document.getString("finishDate"));
        TextView textView3 = createTextView(menstruationStatus);

        tableRow.addView(textView1);
        tableRow.addView(createDivider());
        tableRow.addView(textView2);
        tableRow.addView(createDivider());
        tableRow.addView(textView3);
        tableLayout.addView(tableRow);
    }

    private String calculateMenstruationStatus(List<QueryDocumentSnapshot> documents, int currentIndex) {
        if (currentIndex == 0 || currentIndex >= documents.size()) {
            return "N/A";
        }

        QueryDocumentSnapshot previousDocument = documents.get(currentIndex - 1);
        QueryDocumentSnapshot currentDocument = documents.get(currentIndex);

        String previousFinishDateStr = previousDocument.getString("finishDate");
        String currentStartDateStr = currentDocument.getString("startDate");

        if (previousFinishDateStr == null || currentStartDateStr == null) {
            return "N/A";
        }

        LocalDate previousFinishDate;
        LocalDate currentStartDate;

        try {
            previousFinishDate = LocalDate.parse(previousFinishDateStr, DateTimeFormatter.ISO_DATE);
            currentStartDate = LocalDate.parse(currentStartDateStr, DateTimeFormatter.ISO_DATE);
        } catch (Exception e) {
            return "N/A";
        }

        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(previousFinishDate, currentStartDate);

        if (daysBetween >= 23 && daysBetween <= 35) {
            return "Normal";
        } else {
            return "Not Normal";
        }
    }

    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(dpToPx(7), dpToPx(7), dpToPx(7), dpToPx(7));
        textView.setTextSize(16);
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_semibold));
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(dpToPx(7), dpToPx(7), dpToPx(7), dpToPx(7));
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_semibold));
        return textView;
    }

    private View createDivider() {
        View divider = new View(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                dpToPx(1),
                TableRow.LayoutParams.MATCH_PARENT
        );
        divider.setLayoutParams(params);
        divider.setBackgroundColor(Color.BLACK);
        return divider;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
