package com.builders.shobha.shobhagroup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class VisitFormActivity extends AppCompatActivity {

    Spinner spinner1, spinner2, spinner3;
    LinearLayout plotSize, flatType, othersContainer;
    Button submitButton;
    EditText dateSelected;
    Dialog dialog;
    String name="", phone="", email_id="", type_home="", unit_type="", budget="", activity="", remarks="", site_visit_date="";
    String send_email = "sales.coimbatore@sobha.com";
    RadioGroup rGroup;
    CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_form);
        dateSelected = (EditText)findViewById(R.id.date_chooser);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 =  ((Spinner)findViewById(R.id.spinner3));
        plotSize = (LinearLayout)findViewById(R.id.plot_size_container);
        flatType = (LinearLayout)findViewById(R.id.flat_type_container);
        othersContainer = (LinearLayout)findViewById(R.id.others_container);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        submitButton = (Button)findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()){
                    new SendMailTask().execute();
                }
            }
        });
        EditText inputEmail =(EditText)findViewById(R.id.input_email);
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));

        //spinner1.setVisibility(View.GONE);
        rGroup= (RadioGroup)findViewById(R.id.radioGroup);
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.radioButton){
                    plotSize.setVisibility(View.VISIBLE);
                    flatType.setVisibility(View.GONE);
                    othersContainer.setVisibility(View.VISIBLE);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(VisitFormActivity.this,
                            R.array.budget_types_villa, R.layout.spinner_budget_text);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter);
                }else if(i == R.id.radioButton2){
                    flatType.setVisibility(View.VISIBLE);
                    plotSize.setVisibility(View.GONE);
                    othersContainer.setVisibility(View.VISIBLE);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(VisitFormActivity.this,
                            R.array.budget_types_apartmnent, R.layout.spinner_budget_text);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter);
                }else if(i == R.id.radioButton3){
                    othersContainer.setVisibility(View.VISIBLE);
                    plotSize.setVisibility(View.GONE);
                    flatType.setVisibility(View.GONE);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(VisitFormActivity.this,
                            R.array.budget_types_others, R.layout.spinner_budget_text);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter);
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
        if(ccp.getSelectedCountryNameCode().contentEquals("IN")){
            if(phone.length() != 10){
                showToast("Enter valid phone number");
                return false;
            }
        }
        phone = "+"+ccp.getSelectedCountryCode()+" "+phone;
        email_id = ((EditText)findViewById(R.id.input_email)).getText().toString();
        if(!isValidEmail(email_id)){
            showToast("Enter valid email");
            return false;
        }
        int selectedId = rGroup.getCheckedRadioButtonId();

        if(selectedId == -1){
            showToast("Please select a home type");
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
        if(dateSelected.getText().toString().trim().isEmpty()){
            showToast("Invalid Date");
            return false;
        }
        site_visit_date = dateSelected.getText().toString().trim();
        try {
            SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            sd.setLenient(false);
            Date d1 = sd.parse(site_visit_date);
            Date d2 = new Date();
            d2.setHours(0);
            d2.setMinutes(0);
            d2.setSeconds(0);
            if( !(d1.after(d2) || d1.equals(d2) || d1.getDate() == d2.getDate()) ){
                showToast("Invalid Date");
                return false;
            }
        }catch (Exception e){

        }

        activity = ((EditText)findViewById(R.id.input_activity)).getText().toString();
        if(activity.trim().isEmpty()){
            showToast("Please fill activity field");
            return false;
        }
        remarks = ((EditText)findViewById(R.id.input_remarks)).getText().toString();
        if(remarks.trim().isEmpty()){
            showToast("Please fill remarks field");
            return false;
        }

        if(!haveNetworkConnection()){
            showToast("No internet connection");
            return false;
        }

        return true;
    }

    private class SendMailTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... urls) {

            try {
                SendGrid sendgrid = new SendGrid("shobha_group", "005qwert005");

                SendGrid.Email email = new SendGrid.Email();

                // Get values from edit text to compose email
                // TODO: Validate edit texts
                email.addTo(send_email);
                email.setFrom("visitor@sobha-group.com");
                email.setSubject("Visitor Feedback");
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
                        "    <td>Customer name</td>\n" +
                        "    <td>"+ name +"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Phone number</td>\n" +
                        "    <td>"+ phone +"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Email id</td>\n" +
                        "    <td>"+email_id+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Type of Home</td>\n" +
                        "    <td>"+type_home+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Unit type</td>\n" +
                        "    <td>"+unit_type+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Budget</td>\n" +
                        "    <td>"+budget+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Expected site visit date</td>\n" +
                        "    <td>"+site_visit_date+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Activity</td>\n" +
                        "    <td>"+activity+"</td>\n" +
                        "  </tr>\n" +
                        "  <tr>\n" +
                        "    <td>Remarks</td>\n" +
                        "    <td>"+remarks+"</td>\n" +
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
        dialog = new Dialog(VisitFormActivity.this, R.style.TranslucentDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void dismissDialog(){
        dialog.dismiss();
    }

    private void showToast(String message){
        Toast.makeText(VisitFormActivity.this, message, Toast.LENGTH_LONG).show();
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            ((VisitFormActivity)getActivity()).setDateSelected(year, month, day);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void setDateSelected(int year, int month, int day){
        dateSelected.setText(""+day+"/"+(month+1)+"/"+year);
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_email:
                    validateEmail();
                    break;

            }
        }
    }

    private boolean validateEmail() {
        EditText inputEmail=(EditText)findViewById(R.id.input_email);
        TextInputLayout inputLayoutEmail = (TextInputLayout)findViewById(R.id.input_layout_email);
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }
}
