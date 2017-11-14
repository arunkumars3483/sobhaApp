package com.builders.shobha.shobhagroup;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.sendgrid.SendGrid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SiteVisitFormActivity extends AppCompatActivity {

    Dialog dialog;
    Spinner spinner1, spinner2, spinner3;
    LinearLayout plotSize, flatType, othersContainer;
    RadioGroup rGroupKnow,rGroupFeel,rGroupHow,rGroup;
    EditText knowOthers;
    LinearLayout feelDissatisContainer, howDissatisContainer;
    Button submitButton;
    String name="", phone="", email_id="", type_home="", unit_type="", budget="", activity="", remarks="", site_visit_date="";
    String optional_number = "", residence="", employee_serviced="", q1 = "", q2="", q3="", q4="", q8="",q9="",q10="";
    CountryCodePicker ccp, ccp_optional;
    //String send_email = "dilzsebastian@gmail.com";
    String send_email = "sales.coimbatore@sobha.com";
    //String send_email = "arunkumars3483@gmail.com";
    String api_key = "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";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_visit_form);

        knowOthers = (EditText)findViewById(R.id.input_other_heard_source);

        spinner3 =  ((Spinner)findViewById(R.id.spinner3));
        plotSize = (LinearLayout)findViewById(R.id.plot_size_container);
        flatType = (LinearLayout)findViewById(R.id.flat_type_container);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp_optional = (CountryCodePicker) findViewById(R.id.ccp_optional);
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

        submitButton = (Button)findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()){
                    new SiteVisitFormActivity.SendMailTask().execute();
                }
            }
        });
    }

    private boolean validateFields(){


        name = ((EditText)findViewById(R.id.input_name)).getText().toString();
        if(!validateName()){
            showToast("Please enter your name");
            return false;
        }

        phone = ((EditText)findViewById(R.id.input_phone)).getText().toString();
        if(!isValidMobile(phone)){
            showToast("Enter valid phone number");
            return false;
        }
        optional_number = ((EditText)findViewById(R.id.input_phone_optional)).getText().toString();
        if(optional_number.trim().length() > 0) {
            if (!isValidMobile(optional_number)) {
                showToast("Enter valid optional phone number");
                return false;
            }
        }

        if(ccp.getSelectedCountryNameCode().contentEquals("IN")){
            if(phone.length() != 10){
                showToast("Enter valid phone number");
                return false;
            }
        }
        if(ccp_optional.getSelectedCountryNameCode().contentEquals("IN")){
            if(optional_number.trim().length() > 0) {
                if (optional_number.length() != 10) {
                    showToast("Enter valid optional phone number");
                    return false;
                }
            }
        }
        phone = "+"+ccp.getSelectedCountryCode()+" "+phone;
        if(optional_number.trim().length() > 0) {
            optional_number = "+" + ccp_optional.getSelectedCountryCode() + " " + optional_number;
        }
        email_id = ((EditText)findViewById(R.id.input_email)).getText().toString();
        if(!isValidEmail(email_id)){
            showToast("Enter valid email");
            return false;
        }

        residence = ((EditText)findViewById(R.id.input_location)).getText().toString();
        if(residence.trim().length() <= 0) {
                showToast("Enter Residence Location");
                return false;
        }

        employee_serviced = ((EditText)findViewById(R.id.input_sobha_employee)).getText().toString();
        if(employee_serviced.trim().length() <= 0) {
            showToast("Enter Sobha Employee Serviced");
            return false;
        }


        int id1 = rGroupKnow.getCheckedRadioButtonId();
        if(id1 == -1){
            showToast("Please answer question 1.");
            return false;
        }else {
            if(id1 == R.id.radioButtonOthers){
                q1 = ((EditText)findViewById(R.id.input_other_heard_source)).getText().toString().trim();
                if(q1.length() <= 0){
                    showToast("Please fill others field in question 1");
                    return false;
                }
            }else {
                q1 = ((RadioButton) findViewById(id1)).getText().toString();
            }

        }

        int id2 = ((RadioGroup)findViewById(R.id.radioGroupPurpose)).getCheckedRadioButtonId();
        if(id2 == -1){
            showToast("Please answer question 2.");
            return false;
        }else {
            q2 = ((RadioButton) findViewById(id2)).getText().toString();
        }


        RadioGroup feel= (RadioGroup)findViewById(R.id.radioGroupFeel);
        int id3 = feel.getCheckedRadioButtonId();
        if(id3 == -1){
            showToast("Please answer question 3.");
            return false;
        }else {
            if(id3 == R.id.radioButtonImprove){
                EditText disField = (EditText)findViewById(R.id.input_feel_dissatisfication);
                if(disField.getText().toString().trim().length() > 0){
                    q3 = ((RadioButton) findViewById(id3)).getText().toString();
                    q3 = q3 + " (Dissatisfication: " + disField.getText().toString().trim() + " )";
                }else {
                    showToast("Fill dissatisfication field in question 3.");
                    return false;
                }
            }else {
                q3 = ((RadioButton) findViewById(id3)).getText().toString();
            }
        }


        RadioGroup feel2= (RadioGroup)findViewById(R.id.radioGroupHow);
        int id4 = feel2.getCheckedRadioButtonId();
        if(id4 == -1){
            showToast("Please answer question 4.");
            return false;
        }else {
            if(id4 == R.id.radioButtonHowImprove){
                EditText disField = (EditText)findViewById(R.id.input_how_dissatisfication);
                if(disField.getText().toString().trim().length() > 0){
                    q4 = ((RadioButton) findViewById(id3)).getText().toString();
                    q4 = q4 + " (Dissatisfication: " + disField.getText().toString().trim() + " )";
                }else {
                    showToast("Fill dissatisfication field in question 4.");
                    return false;
                }
            }else {
                q4 = ((RadioButton) findViewById(id4)).getText().toString();
            }
        }



        int selectedId = rGroup.getCheckedRadioButtonId();

        if(selectedId == -1){
            showToast("Please answer question 5.");
            return false;
        }
        // find the radiobutton by returned id
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        type_home = radioButton.getText().toString();
        if(type_home.contentEquals("VILLA")){
            unit_type = ((Spinner)findViewById(R.id.spinner1)).getSelectedItem().toString();
        }else if(type_home.contentEquals("APARTMENT")){
            unit_type = ((Spinner)findViewById(R.id.spinner2)).getSelectedItem().toString();
        }else{
            unit_type = "";
        }
        if(((Spinner)findViewById(R.id.spinner3)).getSelectedItemPosition() == 0){
            showToast("Please Choose a Budget");
            return false;
        }
        budget = ((Spinner)findViewById(R.id.spinner3)).getSelectedItem().toString();


        RadioGroup r8 = (RadioGroup)findViewById(R.id.radioGroupFunding);
        if(r8.getCheckedRadioButtonId() == -1){
            showToast("Please answer question 8.");
            return false;
        }else {
            q8 = ((RadioButton)findViewById(r8.getCheckedRadioButtonId())).getText().toString();
        }

        RadioGroup r9 = (RadioGroup)findViewById(R.id.radioGroupPurchase);
        if(r9.getCheckedRadioButtonId() == -1){
            showToast("Please answer question 9.");
            return false;
        }else {
            q9 = ((RadioButton)findViewById(r9.getCheckedRadioButtonId())).getText().toString();
        }

        String stm_remarks = ((EditText)findViewById(R.id.input_remarks)).getText().toString().trim();
        if(stm_remarks.length() <= 0){
            showToast("Please enter STM/GRE remarks.");
            return false;
        }
        q10 = stm_remarks;

        if(!haveNetworkConnection()){
            showToast("No internet connection");
            return false;
        }

        return true;
    }

    private void showToast(String message){
        Toast.makeText(SiteVisitFormActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private boolean validateName() {
        if (name.trim().isEmpty()) {
            return false;
        }
        return true;
    }
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;

    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }




    private class SendMailTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... urls) {

            try {
                SendGrid sendgrid = new SendGrid(api_key);

                SendGrid.Email email = new SendGrid.Email();

                // Get values from edit text to compose email
                // TODO: Validate edit texts
                email.addTo(send_email);
                email.setFrom("sitevisitor@sobha-group.com");
                email.setSubject("Site Visit Form");
                //email.setText("");
                email.setHtml("<html>\n" +
                        "<head>\n" +
                        "<style>\n" +
                        "table {\n" +
                        "    border-collapse: collapse;\n" +
                        "    width:100%;\n" +
                        "}\n" +
                        "\n" +
                        "table, td, th {\n" +
                        "    border: 2px solid black;\n" +
                        "}\n" +
                        "td{\n" +
                        "\tpadding: 10px;\n" +
                        "    width: 50%;\n" +
                        "}\n" +
                        "</style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<table>\n" +
                        "\n" +
                        "  <tr>\n" +
                        "    <td>Name</td>\n" +
                        "    <td>"+ name +"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Contact number</td>\n" +
                        "    <td>"+ phone +"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Optional number</td>\n" +
                        "    <td>"+optional_number+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Email Id</td>\n" +
                        "    <td>"+email_id+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Current Residence Location</td>\n" +
                        "    <td>"+residence+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Name of the Sobha Employee Serviced</td>\n" +
                        "    <td>"+employee_serviced+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>1. How did you know about Sobha Limited?</td>\n" +
                        "    <td>"+q1+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>2. Your Purpose of looking for a property with Sobha Limited?</td>\n" +
                        "    <td>"+q2+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>3. How do you feel the Model apartment at the Site?</td>\n" +
                        "    <td>"+q3+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>4. How was the Site Visit Process?</td>\n" +
                        "    <td>"+q4+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>5. Preferred type of Home?</td>\n" +
                        "    <td>"+type_home+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>6. Preferred type of Unit</td>\n" +
                        "    <td>"+unit_type+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>7. Budget</td>\n" +
                        "    <td>"+budget+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>8. Funding by</td>\n" +
                        "    <td>"+q8+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>9. Expected time to purchase?</td>\n" +
                        "    <td>"+q9+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>10. STM/GRE remarks</td>\n" +
                        "    <td>"+q10+"</td>\n" +
                        "  </tr>\n" +
                        "</table>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>\n");

                // Send email, execute http request
                SendGrid.Response response = sendgrid.send(email);
                String mMsgResponse = response.getMessage();

                Log.d("SendAppExample", mMsgResponse);

            } catch (Exception e) {
                Log.e("SendAppExample", e.toString());
            }
            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dismissDialog();
            showToast("Thanks for your feedback");
            finish();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }
    }
    private void showDialog(){
        dialog = new Dialog(SiteVisitFormActivity.this, R.style.TranslucentDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void dismissDialog(){
        dialog.dismiss();
    }

}
