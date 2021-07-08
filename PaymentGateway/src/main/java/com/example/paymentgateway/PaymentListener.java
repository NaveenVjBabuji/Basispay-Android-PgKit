package com.example.paymentgateway;

import org.json.JSONObject;

import java.io.Serializable;

public interface PaymentListener extends Serializable {
    public void onPaymentSuccess(JSONObject paymentResponse);
    public void onPaymentFailure(JSONObject PaymentReponse);
    public void OnPaymentCancelled(String reason);
}
