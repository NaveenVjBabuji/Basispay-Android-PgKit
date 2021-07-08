package com.example.paymentgateway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.test.pg.secure.pgsdkv4.PGConstants;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PaymentParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

       public PaymentListener paymentListener;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_payment);
            Intent intent = getIntent();
            paymentListener = (PaymentListener) intent.getSerializableExtra("interface");
            Map<String,String> inputParameters = (Map<String,String>) intent.getSerializableExtra("inputParameters");
            initiatePaymentGateway(inputParameters);
                }

       public void initiatePaymentGateway(Map<String,String> paymentObject){
        PaymentParams pgPaymentParams = new PaymentParams();
        pgPaymentParams.setAPiKey(paymentObject.get(PaymentDefaults.apiKey));
        pgPaymentParams.setAmount(paymentObject.get(PaymentDefaults.amount));
        pgPaymentParams.setEmail(paymentObject.get(PaymentDefaults.email));
        pgPaymentParams.setName(paymentObject.get(PaymentDefaults.name));
        pgPaymentParams.setPhone(paymentObject.get(PaymentDefaults.phone));
        pgPaymentParams.setOrderId(paymentObject.get(PaymentDefaults.orderId));
        pgPaymentParams.setCurrency(paymentObject.get(PaymentDefaults.currency));
        pgPaymentParams.setDescription(paymentObject.get(PaymentDefaults.description));
        pgPaymentParams.setCity(paymentObject.get(PaymentDefaults.city));
        pgPaymentParams.setState(paymentObject.get(PaymentDefaults.state));
        pgPaymentParams.setAddressLine1(paymentObject.get(PaymentDefaults.address1));
        pgPaymentParams.setAddressLine2(paymentObject.get(PaymentDefaults.address2));
        pgPaymentParams.setZipCode(paymentObject.get(PaymentDefaults.zipCode));
        pgPaymentParams.setCountry(paymentObject.get(PaymentDefaults.country));
        pgPaymentParams.setReturnUrl(paymentObject.get(PaymentDefaults.returnUrl));
        pgPaymentParams.setMode(paymentObject.get(PaymentDefaults.mode));
        pgPaymentParams.setUdf1(paymentObject.get(PaymentDefaults.udf1));
        pgPaymentParams.setUdf2(paymentObject.get(PaymentDefaults.udf2));
        pgPaymentParams.setUdf3(paymentObject.get(PaymentDefaults.udf3));
        pgPaymentParams.setUdf4(paymentObject.get(PaymentDefaults.udf4));
        pgPaymentParams.setUdf5(paymentObject.get(PaymentDefaults.udf5));
        pgPaymentParams.setEnableAutoRefund(paymentObject.get(PaymentDefaults.enableAutoRefund));
        pgPaymentParams.setOfferCode(paymentObject.get(PaymentDefaults.offerCode));
        pgPaymentParams.setSplitInfo(paymentObject.get(PaymentDefaults.splitInfo));

        PaymentGatewayPaymentInitializer pgPaymentInitialzer = new PaymentGatewayPaymentInitializer(pgPaymentParams,PaymentActivity.this);
        pgPaymentInitialzer.initiatePaymentProcess();
        }
        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == PGConstants.REQUEST_CODE) {
                if(resultCode == Activity.RESULT_OK){
                    try{
                        String paymentResponse=data.getStringExtra(PGConstants.PAYMENT_RESPONSE);
                        System.out.println("paymentResponse: "+paymentResponse);
                        if(paymentResponse.equals("null")){
                            System.out.println("Transaction Error!");
                        }else{

                            JSONObject response = new JSONObject(paymentResponse);
                            if (response.get("statusCode") == "0") {
                                paymentListener.onPaymentSuccess(response);
                            }else {
                                paymentListener.onPaymentFailure(response);
                            }

                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    paymentListener.OnPaymentCancelled("Transaction failed");
                }

            }
        }

    }
