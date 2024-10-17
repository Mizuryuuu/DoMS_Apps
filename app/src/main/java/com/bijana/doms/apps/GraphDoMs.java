package com.bijana.doms.apps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GraphDoMs extends AppCompatActivity {

    RelativeLayout profileUser;

    TextView homeNav;

    TextView addNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_euy);

        profileUser = this.findViewById(R.id.topContent);
        profileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(getApplicationContext(), ProfileDoMs.class);
                startActivity(Intent);
            }
        });

        homeNav = this.findViewById(R.id.home_cuy);
        homeNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wrapHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(wrapHome);
            }
        });

        addNav = this.findViewById(R.id.add_cuy);
        addNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wrapAdd = new Intent(getApplicationContext(), AddDoMs.class);
                startActivity(wrapAdd);
            }
        });

    }
}