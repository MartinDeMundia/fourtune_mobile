package com.coreictconsultancy.fourtune.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;


import com.coreictconsultancy.fourtune.R;

//import com.braintreepayments.api.dropin.*;

//import com.braintreepayments.api.models.PaymentMethodNonce;
import com.loopj.android.http.TextHttpResponseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

public class PaymentActivity extends AppCompatActivity {

    private static final String SERVER_BASE = "http://api.fourtune.uk/api/v1";
    private static final int REQUEST_CODE = Menu.FIRST;
    private AsyncHttpClient client = new AsyncHttpClient();
    private String clientToken;
    private static final int DROP_IN_REQUEST = 100;
    private static final String KEY_NONCE = "nonce";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getToken();
    }

    private void getToken(){
        client.get(SERVER_BASE + "/token", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                findViewById(R.id.btn_start).setEnabled(false);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                clientToken = responseString;
                findViewById(R.id.btn_start).setEnabled(true);
            }
        });
    }
    public void onStartClick(View view) {
       /* DropInRequest dropInRequest = new DropInRequest()
                .clientToken(this.clientToken).amount("15.00");
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);*/
        //DropInRequest dropInRequest = new DropInRequest()
                //.tokenizationKey("sandbox_bnpspx5n_6m8mg96fbd3skmvb").amount("15.00");
        //startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }


    //Amount: 100.00
    //Number: 4111 1111 1111 1111
    //CVV: 123
    //Expiration date: 22/2022  or for client 2 (01/2021)


    //(PayPal) Fill in the following credentials:
    //Email: us-customer@commercefactory.org
    //Password: test1234



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server

                Toast.makeText(this, "Success, send details to server!", Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                //Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }





}