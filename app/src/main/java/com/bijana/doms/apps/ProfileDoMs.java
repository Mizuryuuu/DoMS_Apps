package com.bijana.doms.apps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileDoMs extends AppCompatActivity implements DialogDoMs.dialogListener {

    TextView logout;

    TextView arrowBack;

    TextView editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_euy);

        arrowBack = this.findViewById(R.id.backArrow);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Intent);
            }
        });

        logout = findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        editProfile= findViewById(R.id.editUsername);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(getApplicationContext(), EditProfileDoMS.class);
                startActivity(Intent);
            }
        });
    }

    public void openDialog() {
        DialogDoMs dialog = new DialogDoMs();
        dialog.show(getSupportFragmentManager(), "dialog Logout");
    }

    @Override
    public void onYesClicked() {
        Intent intent =new Intent(getApplicationContext(), LoginDoMS.class);
        startActivity(intent);
        finish();
    }
}