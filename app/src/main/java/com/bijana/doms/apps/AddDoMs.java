package com.bijana.doms.apps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bijana.doms.apps.Adapter.SharePreferenceData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddDoMs extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    EditText inputNominal, inputNote;
    RadioGroup radioGroupType;
    RadioButton radioButtonType, buttonIncome, radioButtonSpending;
    Button inputDate, buttonSubmit;
    String typeChose;
    public String getID;
    public int finalNominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_euy);
        initDatePicker();

        inputDate = findViewById(R.id.inputDate);
        inputDate.setText(getTodaysDate());
        inputNominal = findViewById(R.id.inputNominal);
        inputNote = findViewById(R.id.inputNote);
        radioGroupType = findViewById(R.id.radioButtonGrup);
        buttonSubmit = findViewById(R.id.buttonAddValue);

        inputNominal.addTextChangedListener(new TextWatcher() {
            private String setEditText = inputNominal.getText().toString().trim();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if(!s.toString().equals(setEditText)) {
                    inputNominal.removeTextChangedListener(this);
                    String replace = s.toString().replaceAll("[Rp .]", "");
                    if (!replace.isEmpty()) {
                        setEditText = formatRupiah(Double.parseDouble(replace));
                        finalNominal = (int) Double.parseDouble(replace);
                    } else {
                        setEditText = "";
                    }

                    Log.d("Test Angka", String.valueOf(finalNominal));

                    inputNominal.setText(setEditText);
                    inputNominal.setSelection(setEditText.length());
                    inputNominal.addTextChangedListener(this);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radioIncome) {
                    // Tindakan yang diambil ketika RadioButton "Income" dipilih
                    typeChose = "Income";
                } else if (checkedId == R.id.radioSpending) {
                    // Tindakan yang diambil ketika RadioButton "Spending" dipilih
                    typeChose = "Spending";
                } else if (checkedId == R.id.radioChecked){
                    typeChose = "";
                }
            }
        });


        getID = SharePreferenceData.getId(this);


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Log.d("Type Pilihan coiii", typeChose);

                if(validateData()){
                    inputData();
                }
            }

            private boolean validateData() {
                String nominal = inputNominal.getText().toString().trim();
                String note = inputNote.getText().toString().trim();

                if (nominal.isEmpty() || note.isEmpty() ){
                    Toast.makeText(AddDoMs.this, "Please fill fields", Toast.LENGTH_LONG).show();
                    return false;
                }

                return true;
            }

            private void inputData() {

                final String date = inputDate.getText().toString();
                final String nominal = String.valueOf(finalNominal);
                final String note = inputNote.getText().toString();
                final String type = typeChose;

                String url = new Server().baseUrl() + "transaksi.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                    JSONObject dataObject = jsonArray.getJSONObject(0);
                                    String status = dataObject.getString("Status");
                                    String message = dataObject.getString("Message");

                                    if(status.equals("SUCCESS")){

                                        Intent Intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(Intent);

                                        Toast.makeText(AddDoMs.this, message.toString(), Toast.LENGTH_LONG).show();
                                    }else if(status.equals("FAIL"))
                                    {
                                        Toast.makeText(AddDoMs.this, message.toString(), Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddDoMs.this, "Error to login", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> form = new HashMap<String, String>();
                        form.put("date", date);
                        form.put("nominal", nominal);
                        form.put("note", note);
                        form.put("id_akun",getID);

                        if (type != null) {
                            form.put("type", type);
                        } else {
                            // Handle jika type bernilai null, misalnya dengan memberikan nilai default
                            form.put("type", "Unknown");
                        }

                        return form;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            }
        });

    }

    private String  getTodaysDate()
    {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        month = month + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                inputDate.setText(date);
            }
        };

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        return year + ", " + getMonthFormat(month) + " " + day;
    }

    private String getMonthFormat(int month)
    {
        if (month == 1)
            return "January";
        if (month == 2)
            return "February";
        if (month == 3)
            return "March";
        if (month == 4)
            return "April";
        if (month == 5)
            return "Mei";
        if (month == 6)
            return "June";
        if (month == 7)
            return "July";
        if (month == 8)
            return "Augustus";
        if (month == 9)
            return "September";
        if (month == 10)
            return "October";
        if (month == 11)
            return "November";
        if (month == 12)
            return "December";

        return "January";
    }

    private String formatRupiah(Double number){
        Locale LocaleId = new Locale("IND", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(LocaleId);
        String formatrupiah = numberFormat.format(number);
        String[] split = formatrupiah.split(",");
        int lenght = split[0].length();
        return split[0].substring(0,2)+". "+split[0].substring(2,lenght);
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}