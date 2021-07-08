package com.example.paymentgateway;

import org.json.JSONObject;

public interface PaymentListener {
    public void onPaymentSuccess(JSONObject paymentResponse);
    public void onPaymentFailure(JSONObject PaymentReponse);
    public void OnPaymentCancelled(String reason);
}
