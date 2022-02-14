package com.coreictconsultancy.fourtune.data;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.client.entity.UrlEncodedFormEntity;

public class URLProvider extends AppCompatActivity {
    private static final String TAG = "URLProvider";

    //public static String LiveURL = "http://fourtune-api.coreict.co.ke";
    //public static String LiveURL = "http://192.168.1.125/fourtune/api/public";
    //public static String LiveURL = "http://192.168.100.16/fourtune/api/public";
    public static String LiveURL = "http://api.fourtune.uk";

    public static UrlEncodedFormEntity getCoinsEarned(UrlEncodedFormEntity urlEncodedForm) {
        return urlEncodedForm;
    }

    public static UrlEncodedFormEntity createUserAccount(UrlEncodedFormEntity urlEncodedForm) {
        return urlEncodedForm;
    }
    public static UrlEncodedFormEntity authenticateUserAccount(UrlEncodedFormEntity urlEncodedForm) {
        return urlEncodedForm;
    }

    public static UrlEncodedFormEntity purchaseToken(UrlEncodedFormEntity urlEncodedForm) {
        return urlEncodedForm;
    }

    public static UrlEncodedFormEntity getApi(UrlEncodedFormEntity urlEncodedForm) {
        return urlEncodedForm;
    }

}
