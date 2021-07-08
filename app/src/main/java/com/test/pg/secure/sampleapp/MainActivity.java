package com.test.pg.secure.sampleapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.paymentgateway.PaymentActivity;
import com.example.paymentgateway.PaymentDefaults;
import com.example.paymentgateway.PaymentListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;






public class MainActivity extends AppCompatActivity implements PaymentListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PaymentActivity paymentActivity = new PaymentActivity();

        Intent myIntent = new Intent(this,PaymentActivity.class);
        Map<String,String> inputParameters = new HashMap<String,String>();
        inputParameters.put(PaymentDefaults.apiKey,"79e111fb-098d-4730-8c3a-17fe0c30738a");
        inputParameters.put(PaymentDefaults.returnUrl,"http://159.65.148.139/rest/mcpayment/pgdetails");
        inputParameters.put(PaymentDefaults.amount,"2300");
        inputParameters.put(PaymentDefaults.email,"nvnkumar398@gmail.com");
        inputParameters.put(PaymentDefaults.name,"Naveen");
        inputParameters.put(PaymentDefaults.phone,"8248350384");
        inputParameters.put(PaymentDefaults.orderId,"CJH2386");
        inputParameters.put(PaymentDefaults.currency,"INR");
        inputParameters.put(PaymentDefaults.description,"Shoe");
        inputParameters.put(PaymentDefaults.city,"Chennai");
        inputParameters.put(PaymentDefaults.state,"TamilNadu");
        inputParameters.put(PaymentDefaults.address1,"No28/39 muthaiah street");
        inputParameters.put(PaymentDefaults.address2,"No28/39 muthaiah street");
        inputParameters.put(PaymentDefaults.zipCode,"600014");
        inputParameters.put(PaymentDefaults.country,"India");
        inputParameters.put(PaymentDefaults.mode,"TEST");
        inputParameters.put(PaymentDefaults.udf1,"udf1");
        inputParameters.put(PaymentDefaults.udf1,"udf1");
        inputParameters.put(PaymentDefaults.udf1,"udf1");
        inputParameters.put(PaymentDefaults.udf4,"udf1");
        inputParameters.put(PaymentDefaults.udf5,"udf1");
        inputParameters.put(PaymentDefaults.mode,"TEST");
        inputParameters.put(PaymentDefaults.enableAutoRefund,"");
        inputParameters.put(PaymentDefaults.offerCode,"");
        inputParameters.put(PaymentDefaults.splitInfo,"");
        myIntent.putExtra("interface", (Serializable) this);
        myIntent.putExtra("inputParameters",(Serializable) inputParameters);
        startActivity(myIntent);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    
    @Override
    public void onPaymentSuccess(JSONObject paymentResponse) {
        Log.d("cancel", paymentResponse.toString());
    }

    @Override
    public void onPaymentFailure(JSONObject PaymentReponse) {
        Log.d("cancel", PaymentReponse.toString());
    }

    @Override
    public void OnPaymentCancelled(String reason) {
        Log.d("cancel", reason);
    }
}
