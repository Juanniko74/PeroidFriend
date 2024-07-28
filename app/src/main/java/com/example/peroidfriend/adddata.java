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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class adddata extends AppCompatActivity {

    EditText datestart, datefinish, expressinput, scalepain;
    Button btninput, btncancel;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddata);

        datestart = findViewById(R.id.datestartinput);
        datefinish = findViewById(R.id.datefinishinput);
        expressinput = findViewById(R.id.expressinput);
        scalepain = findViewById(R.id.scalepain);
        btninput = findViewById(R.id.btninput);
        btncancel = findViewById(R.id.btncanceladd);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

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

        btninput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToFirestore();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adddata.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog(final EditText dateField) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(adddata.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String month = String.format("%02d", monthOfYear + 1);
                String day = String.format("%02d", dayOfMonth);
                dateField.setText(year + "-" + month+ "-" + day);
            }
        }, mYear, mMonth, mDay);
        datepickerdialog.show();
    }

    private void saveDataToFirestore() {
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

        db.collection("periodData")
                .add(periodData)
                .addOnSuccessListener(documentReference -> {
                    // Update the document with its ID
                    String documentId = documentReference.getId();
                    documentReference.update("documentId", documentId)
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Data successfully added with ID", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Error updating document ID: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding document: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        Intent intent = new Intent(adddata.this, MainActivity.class);
        startActivity(intent);
    }
}
