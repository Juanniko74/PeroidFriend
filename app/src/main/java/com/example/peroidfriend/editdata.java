package com.example.peroidfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class editdata extends AppCompatActivity {

    private EditText datestart, datefinish, expressinput, scalepain;
    private Button btnupdate, btncancel;
    private FirebaseFirestore db;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdata);

        datestart = findViewById(R.id.editstartdate);
        datefinish = findViewById(R.id.editfinishdate);
        expressinput = findViewById(R.id.editexpression);
        scalepain = findViewById(R.id.editpainscale);
        btnupdate = findViewById(R.id.savebutton);
        btncancel = findViewById(R.id.btncanceledit);

        datestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(datestart);
            }
        });

        datefinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(datefinish);
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editdata.this, updatedata.class);
                startActivity(intent);
            }
        });

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Mendapatkan ID dokumen dan data lainnya dari Intent
        documentId = getIntent().getStringExtra("DOCUMENT_ID");
        String startDate = getIntent().getStringExtra("START_DATE");
        String finishDate = getIntent().getStringExtra("FINISH_DATE");
        String expression = getIntent().getStringExtra("EXPRESSION");
        String painScale = getIntent().getStringExtra("PAIN_SCALE");

        // Log untuk verifikasi data dari Intent
        Log.d("EditData", "Document ID: " + documentId);
        Log.d("EditData", "Start Date: " + startDate);
        Log.d("EditData", "Finish Date: " + finishDate);
        Log.d("EditData", "Expression: " + expression);
        Log.d("EditData", "Pain Scale: " + painScale);

        if (documentId == null) {
            Toast.makeText(this, "No document ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set the data in EditText fields
        datestart.setText(startDate);
        datefinish.setText(finishDate);
        expressinput.setText(expression);
        scalepain.setText(painScale);

        btnupdate.setOnClickListener(v -> updateData());
    }

    private void showDatePickerDialog(final EditText dateField) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(editdata.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String month = String.format("%02d", monthOfYear + 1);
                String day = String.format("%02d", dayOfMonth);
                dateField.setText(year + "-" + month+ "-" + day);
            }
        }, mYear, mMonth, mDay);
        datepickerdialog.show();
    }
    private void updateData() {
        String startDate = datestart.getText().toString();
        String finishDate = datefinish.getText().toString();
        String expression = expressinput.getText().toString();
        String painScale = scalepain.getText().toString();

        if (startDate.isEmpty() || finishDate.isEmpty() || expression.isEmpty() || painScale.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> periodData = new HashMap<>();
        periodData.put("startDate", startDate);
        periodData.put("finishDate", finishDate);
        periodData.put("expression", expression);
        periodData.put("painScale", painScale);

        // Log untuk verifikasi data sebelum memperbarui Firestore
        Log.d("EditData", "Updating document with ID: " + documentId);

        db.collection("periodData")
                .document(documentId)
                .set(periodData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("EditData", "DocumentSnapshot successfully updated!");
                    Toast.makeText(this, "Data successfully updated", Toast.LENGTH_SHORT).show();
                    // Return the document ID to updatedata
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("DOCUMENT_ID", documentId);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w("EditData", "Error updating document", e);
                    Toast.makeText(this, "Error updating document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
