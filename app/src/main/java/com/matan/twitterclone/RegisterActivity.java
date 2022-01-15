package com.matan.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtBirthDate;
    private Calendar calendar;
    private String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText edtName = findViewById(R.id.edt_name);
        EditText edtEmail = findViewById(R.id.edt_email);
        edtBirthDate = findViewById(R.id.edt_birth);

        ImageButton btnBack = findViewById(R.id.btn_close);
        Button btnNext = findViewById(R.id.btn_next);

        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateFormatDate();
            }
        };

        edtBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String email = edtEmail.getText().toString();
                String date = edtBirthDate.getText().toString();

                if(!validateFields(name, email, date)) {
                    Toast.makeText(RegisterActivity.this, "Must fill all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
                intent.putExtra("Name", name);
                intent.putExtra("Email", email);
                intent.putExtra("BirthDate", dateString);
                startActivity(intent);
            }
        });
    }

    private boolean validateFields(String name, String email, String dob) {
        if(name.equals("") || email.equals("") || dob.equals("")) {
            // User has not entered any data
            return false;
        }
        else {
            return true;
        }
    }

    void updateFormatDate() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        edtBirthDate.setText(simpleDateFormat.format(calendar.getTime()));
        dateString = simpleDateFormat.format(calendar.getTime());
    }
}