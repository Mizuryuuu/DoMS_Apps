package com.bijana.doms.apps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

public class RegisterDoMS extends AppCompatActivity {

    Button registerButton ;
    TextView wrapToLogin ;
    EditText username, email, password, confirm_password, phoneNo ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_euy);

        wrapToLogin = this.findViewById(R.id.loginLink);
        wrapToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wrapToLogin = new Intent(getApplicationContext(), LoginDoMS.class);
                startActivity(wrapToLogin);
            }
        });

        username = findViewById(R.id.et_username);
        email = findViewById(R.id.et_email);
        phoneNo = findViewById(R.id.et_phoneNo);
        password = findViewById(R.id.et_password);
        confirm_password = findViewById(R.id.et_confirm_password);

        registerButton = findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()){
                    inputData();
                }
            }
        });

    }

    private boolean validateData() {
        String Username = username.getText().toString().trim();
        if(Username.isEmpty()){
            Toast.makeText(RegisterDoMS.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void inputData() {

        final String Username = username.getText().toString();
        final String Email = email.getText().toString();
        final String PhoneNo = phoneNo.getText().toString();
        final String Password = password.getText().toString();
        final String Confirm_password = confirm_password.getText().toString();

        String url = new Server().baseUrl()+"register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.d("testCih", "kebaca?");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject dataObject = jsonArray.getJSONObject(0);
                            String status = dataObject.getString("Status");
                            String message = dataObject.getString("Message");

                            if (status.equals("SUCCESS")) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(RegisterDoMS.this, message.toString(), Toast.LENGTH_SHORT).show();
                            } else if (status.equals("FAIL"))
                            {
                                Toast.makeText(RegisterDoMS.this, message.toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterDoMS.this, "Data not valid", Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> form = new HashMap<>();
                form.put("username", Username);
                form.put("email", Email);
                form.put("phone_no", PhoneNo);
                form.put("password", Password);
                form.put("confirm_password", Confirm_password);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}