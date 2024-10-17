package com.bijana.doms.apps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bijana.doms.apps.Adapter.Adaptor;
import com.bijana.doms.apps.Adapter.GetData;
import com.bijana.doms.apps.Adapter.SharePreferenceData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    TextView balanceTotal, balanceDate;
    ArrayList<GetData> model;
    RelativeLayout profileUser;
    TextView addNav, chartNav;
    public String getID;

    private static final String PREF_NAME = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout .activity_main);

        listView = findViewById(R.id.list);
        balanceTotal = findViewById(R.id.jumlahBalance);

        load_dataBalance();
        load_data();

        getID = SharePreferenceData.getId(this);

        Log.d("GetID KONTOL",getID);

        profileUser = this.findViewById(R.id.topContent);
        addNav      = this.findViewById(R.id.add_cuy);
        chartNav    = this.findViewById(R.id.chart_cuy);

        profileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(getApplicationContext(), ProfileDoMs.class);
                startActivity(Intent);
            }
        });

        addNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wrapAdd = new Intent(getApplicationContext(), AddDoMs.class);
                startActivity(wrapAdd);
            }
        });

        chartNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wrapChart = new Intent(getApplicationContext(), GraphDoMs.class);
                startActivity(wrapChart);
            }
        });

    }

    private void load_dataBalance() {

        String url = new Server().baseUrl() + "showbalance.php";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            JSONObject getData = jsonArray.getJSONObject(0);
                            String balance = getData.getString("balance");

                            String Rupiah = formatRupiah(balance);

                            balanceTotal.setText(Rupiah);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Data not found", Toast.LENGTH_LONG).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> form = new HashMap<String, String>();
                form.put("id_akun",getID);
                return form;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private String formatRupiah(String balance) {

        Double balanceFormat = Double.parseDouble(balance);
        DecimalFormat kursIndonesia = (DecimalFormat)
                DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setCurrencySymbol("Rp. ");
        formatSymbols.setMonetaryDecimalSeparator(',');
        formatSymbols.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatSymbols);

        String Balance = String.valueOf(kursIndonesia.format(balanceFormat));

        return Balance;
    }

    void load_data() {

        String url = new Server().baseUrl() + "history.php";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            model = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++ )
                            {
                                JSONObject getData = jsonArray.getJSONObject(i);
                                String nominal = getData.getString("nominal");

                                String nominalRupiah = formatRupiah(nominal);

//                                String getNote = getData.getString("note");
//                                String getType = getData.getString("type");
                                 model.add(new GetData(
                                        getData.getString("date"),
                                         nominalRupiah,
                                        getData.getString("type")
                                ));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Adaptor adaptor = new Adaptor(getApplicationContext(),model);
                        listView.setAdapter(adaptor);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> form = new HashMap<String, String>();
                form.put("id_akun",getID);
                return form;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}