package com.nepalpolice.smartdoctor.feedback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.nepalpolice.smartdoctor.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private EditText feedbackInputField;

    private EditText emailInputField;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbar.setTitle("Feedback");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        feedbackInputField = (EditText) findViewById(R.id.feedback_input);

        emailInputField = (EditText) findViewById(R.id.email_input);


        findViewById(R.id.buttonSubmit).setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        validateInput();
                        pd = new ProgressDialog(MainActivity.this);
                        pd.setTitle("Submitting Details");
                        pd.setMessage("Please wait....");
                        pd.show();

                    }
                }
        );
    }



    private void validateInput() { // Validate text input

        if (feedbackInputField.getText().toString().trim().length() == 0 && emailInputField.getText().toString().trim().length() == 0) {

            feedbackInputField.setError("Enter your Feedback!");

            emailInputField.setError("Enter your email!");
            Toast.makeText(MainActivity.this, "Please fill in the required fields!", Toast.LENGTH_LONG).show();
        } else {
            validateEmail();
        }
    }

    private void validateEmail() {
        if (Patterns.EMAIL_ADDRESS.matcher(emailInputField.getText()).matches())
            sendData();
        else {
            emailInputField.setError("Enter a valid email!");
            Toast.makeText(MainActivity.this, "Please fill in a Valid Email Address!", Toast.LENGTH_LONG).show();
        }
    }

    private void sendData() { // Send feedback to Google Spreadsheet if text input is valid

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://docs.google.com/forms/d/e/") // Your spreadsheet URL
                .build();
        final SpreadsheetWebService spreadsheetWebService = retrofit.create(SpreadsheetWebService.class);


        String feedbackInput = feedbackInputField.getText().toString();

        String emailInput = emailInputField.getText().toString();

        Call<Void> feedbackCall = spreadsheetWebService.feedbackSend(feedbackInput,emailInput);
        feedbackCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("XXX", "Submitted. " + response);
                Toast.makeText(MainActivity.this,"Your Details was submitted! We'll inform you shortly",Toast.LENGTH_LONG).show();
                pd.hide();
                // Clear all fields after submitting

                feedbackInputField.setText("");

                emailInputField.setText("");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("XXX", "Failed", t);
                Toast.makeText(MainActivity.this,"There was an error!",Toast.LENGTH_LONG).show();
            }
        });
    }

}
