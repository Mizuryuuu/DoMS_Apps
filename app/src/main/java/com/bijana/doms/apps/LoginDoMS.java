package com.bijana.doms.apps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginDoMS extends AppCompatActivity {

    TextView wrapToRegister;
    Button loginButton;
    EditText username, password;
    private String idAccount;
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_ID = "id";
    private static final String KEY_BALANCE = "balance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_euy);

        wrapToRegister = this.findViewById(R.id.registLink);

        wrapToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wrapRegister = new Intent(getApplicationContext(), RegisterDoMS.class);
                startActivity(wrapRegister);
            }
        });

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);

        loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()){
                    inputData();
                }
            }
        });

    }

    private boolean validateData() {

        String Username = username.getText().toString().trim();

        if (Username.isEmpty() ){
            Toast.makeText(LoginDoMS.this, "Please fill fields", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void inputData() {

        final String Username = username.getText().toString();
        final String Password = password.getText().toString();

        String url = new Server().baseUrl()+"login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Test", "Ada Coy");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject dataObject = jsonArray.getJSONObject(0);
                            String status = dataObject.getString("Status");
                            String message = dataObject.getString("Message");

                            if(status.equals("SUCCESS")){

                                idAccount = dataObject.getString("id_akun");
                                idUser(idAccount);

                                Intent Intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(Intent);

                                Toast.makeText(LoginDoMS.this, message.toString(), Toast.LENGTH_LONG).show();
                            }else if(status.equals("FAIL"))
                            {
                                Toast.makeText(LoginDoMS.this, message.toString(), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    private void idUser(String idAccount) {
                        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_ID, idAccount); // Mengganti nilai ID dengan nilai baru
                        editor.apply(); // Menyimpan perubahan
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginDoMS.this, "Error to login", Toast.LENGTH_LONG).show();
            }
        }){
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> form = new HashMap<String, String>();
                form.put("username",Username);
                form.put("password",Password);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}