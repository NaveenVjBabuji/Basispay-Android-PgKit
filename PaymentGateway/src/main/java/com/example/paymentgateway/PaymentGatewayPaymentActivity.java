package com.example.paymentgateway;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

public class PaymentGatewayPaymentActivity extends AppCompatActivity {
    ProgressBar pb;
    WebView webview;

    public PaymentGatewayPaymentActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_payment);
        this.webview = (WebView)this.findViewById(R.id.pgPaymentGatewayWebview);
        this.pb = (ProgressBar)this.findViewById(R.id.progressBar);
        this.pb.setVisibility(0);
        String postPaymentRequestParams = this.getIntent().getStringExtra(PGConstants.POST_PARAMS);
        HashMap<String,String> hashParamsMap = (HashMap<String,String>)this.getIntent().getSerializableExtra(PGConstants.HASH_MAP);
        String hashDict = "69ecafcf78912a3f57a00f0e78ea4194efcd7d24";
        TreeMap<String, String> sorted = new TreeMap<>();

        // Copy all data from hashMap into TreeMap
        sorted.putAll(hashParamsMap);

        // Display the TreeMap which is naturally sorted
        for (Map.Entry<String, String> entry : sorted.entrySet())
//            System.out.println("Key = " + entry.getKey() +
//                    ", Value = " + entry.getValue());

            hashDict += "|" + entry.getValue();

//        Iterator it = hashParamsMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//
//                hashDict += pair.getKey() + "|" + pair.getValue() + "|";
//            it.remove(); // avoids a ConcurrentModificationException
//        }
       // System.out.println(hashDict);
        Log.d("hashMap",hashDict);
      //  Log.d("hashMap",hashParamsMap.toString());
        String hashValue = get_SHA_512_SecurePassword(hashDict);

        try {
            this.webview.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    pb.setVisibility(8);
                    Log.i("log", "onPageFinished : " + url);
                }

                public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                    super.onPageStarted(view, url, facIcon);
                    pb.setVisibility(0);
                    Log.i("log", "onPageFinished : " + url);
                }
            });
            WebSettings webSettings = this.webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            this.webview.addJavascriptInterface(new PaymentGatewayPaymentActivity.MyJavaScriptInterface(this), "Android");
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setDomStorageEnabled(true);
            this.webview.setWebChromeClient(new WebChromeClient() {
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    return super.onJsAlert(view, url, message, result);
                }
            });
             String postUrl = "https://biz.traknpay.in/v2/paymentrequest?" + postPaymentRequestParams + "&hash=" + hashValue.toUpperCase();
             String postParamValues =  postPaymentRequestParams + "&hash=" + hashValue.toUpperCase();
            Log.d("postParamValues",postParamValues);

            this.webview.postUrl(postUrl, (postParamValues).getBytes());
        } catch (Exception var7) {
            StringWriter sw = new StringWriter();
            var7.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Toast.makeText(this.getBaseContext(), exceptionAsString, 0).show();
        }

    }


    public String get_SHA_512_SecurePassword(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == 0) {
            switch(keyCode) {
                case 4:
                    if (this.webview.canGoBack()) {
                        this.webview.goBack();
                    } else {
                        this.finish();
                    }

                    return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        if (this.webview.getVisibility() == 0) {
            if (this.webview.canGoBack()) {
                this.webview.goBack();
            }

        } else {
            super.onBackPressed();
        }
    }

    public native String getParamA();

    public native String getParamB();

    public native String getParamC();

    public native String getLink(String var1, String var2, String var3);

    static {
        System.loadLibrary("native-lib");
    }

    public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            this.mContext = c;
        }

        @JavascriptInterface
        public void showHTML(String html, String url) {
            Log.i("log", "showHTML : " + url + " : " + html);
        }

        @JavascriptInterface
        public void paymentResponse(String jsonStringResponse) {
            try {
                Log.d("", "ResponseJson: " + jsonStringResponse);
                if (!jsonStringResponse.equals("null") && !jsonStringResponse.isEmpty() && jsonStringResponse.contains("transaction_id")) {
                    Intent data = new Intent();
                    data.putExtra(PGConstants.PAYMENT_RESPONSE, jsonStringResponse);
                    PaymentGatewayPaymentActivity.this.setResult(-1, data);
                    PaymentGatewayPaymentActivity.this.finish();
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        }
    }
}
