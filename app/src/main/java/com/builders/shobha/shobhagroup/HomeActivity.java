package com.builders.shobha.shobhagroup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button navigateToFormButton = (Button)findViewById(R.id.button_fill_form_page);
        navigateToFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, VisitFormActivity.class);
                startActivity(intent);
            }
        });
        Button navigateToSiteVisitForm = (Button)findViewById(R.id.button_fill_site_form_page);
        navigateToSiteVisitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SiteVisitFormActivity.class);
                startActivity(intent);
            }
        });
    }
}
