package com.builders.shobha.shobhagroup;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class SiteVisitFormActivity extends AppCompatActivity {

    Spinner spinner1, spinner2, spinner3;
    LinearLayout plotSize, flatType, othersContainer;
    RadioGroup rGroupKnow,rGroupFeel,rGroupHow,rGroup;
    EditText knowOthers;
    LinearLayout feelDissatisContainer, howDissatisContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_visit_form);

        knowOthers = (EditText)findViewById(R.id.input_other_heard_source);

        spinner3 =  ((Spinner)findViewById(R.id.spinner3));
        plotSize = (LinearLayout)findViewById(R.id.plot_size_container);
        flatType = (LinearLayout)findViewById(R.id.flat_type_container);

        rGroupKnow= (RadioGroup)findViewById(R.id.radioGroupSobhaLtd);
        rGroupKnow.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.radioButtonOthers) {
                    knowOthers.setVisibility(View.VISIBLE);
                }else {
                    knowOthers.setVisibility(View.GONE);
                }
            }
        });
        rGroupFeel= (RadioGroup)findViewById(R.id.radioGroupFeel);
        feelDissatisContainer = (LinearLayout)findViewById(R.id.input_feel_dissatisfication_container);
        rGroupFeel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.radioButtonImprove) {
                    feelDissatisContainer.setVisibility(View.VISIBLE);
                }else {
                    feelDissatisContainer.setVisibility(View.GONE);
                }
            }
        });
        rGroupHow= (RadioGroup)findViewById(R.id.radioGroupHow);
        howDissatisContainer = (LinearLayout)findViewById(R.id.input_how_dissatisfication_container);
        rGroupHow.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.radioButtonHowImprove) {
                    howDissatisContainer.setVisibility(View.VISIBLE);
                }else {
                    howDissatisContainer.setVisibility(View.GONE);
                }
            }
        });


        rGroup= (RadioGroup)findViewById(R.id.radioGroup);
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.radioButton){
                    plotSize.setVisibility(View.VISIBLE);
                    flatType.setVisibility(View.GONE);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SiteVisitFormActivity.this,
                            R.array.budget_types_villa, R.layout.spinner_budget_text);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter);
                }else if(i == R.id.radioButton2){
                    flatType.setVisibility(View.VISIBLE);
                    plotSize.setVisibility(View.GONE);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SiteVisitFormActivity.this,
                            R.array.budget_types_apartmnent, R.layout.spinner_budget_text);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter);
                }else if(i == R.id.radioButton3){
                    plotSize.setVisibility(View.GONE);
                    flatType.setVisibility(View.GONE);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SiteVisitFormActivity.this,
                            R.array.budget_types_others, R.layout.spinner_budget_text);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter);
                }
            }
        });
    }
}
