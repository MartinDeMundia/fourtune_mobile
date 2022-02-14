package com.coreictconsultancy.fourtune.data;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataLoaderPost extends AsyncTask<String, Void, String> {
    private String TAG = "DataLoaderPost";

    private DownloadedListener mDownloadedListener;

    private LoaderToUIListener mLoaderToUIListener;

    UrlEncodedFormEntity urlEntities;


    private String mUrl;

    public DataLoaderPost(UrlEncodedFormEntity urlEntities, LoaderToUIListener paramLoaderToUIListener, DownloadedListener paramDownloadedListener) {
        this.mLoaderToUIListener = paramLoaderToUIListener;
        this.mDownloadedListener = paramDownloadedListener;
        this.urlEntities = urlEntities;
    }

    public static String convertinputStreamToString(InputStream paramInputStream) throws IOException {
        StringBuilder stringNull = null;
        String str = "";
        if (paramInputStream != null) {
            stringNull = new StringBuilder();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "UTF-8"));
                while (true) {
                    str = bufferedReader.readLine();
                    if (str != null) {
                        stringNull.append(str);
                        stringNull.append("\n");
                        continue;
                    }
                    return stringNull.toString();
                }
            } finally {
                paramInputStream.close();
            }
        }
        return str;
    }


    protected String doInBackground(String... paramVarArgs) {
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        String result = null;
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(paramVarArgs[0]);
            httpPost.setEntity(this.urlEntities);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            String line = null;
            while ((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    protected void onPostExecute(String paramString) {
        if (this.mDownloadedListener != null) {
            /*String str1 = this.TAG; i can decrypt data here
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("AESresult=");
            stringBuilder1.append(paramString);
            DebugLog.log(str1, stringBuilder1.toString());
            try {
                str1 = AES256Cipher.AES_Decode(paramString, Constants.PRIVATE_KEY);
            } catch (Exception exception) {
                str1 = "";
            }
            String str2 = this.TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("AESresult=");
            stringBuilder2.append(str1);
            DebugLog.log(str2, stringBuilder2.toString());*/
            this.mDownloadedListener.onFinish(this.mLoaderToUIListener,this.mUrl,paramString);
        }
        super.onPostExecute(paramString);
    }



/*    protected void onPostExecute(String paramString) {
        if (this.mDownloadedListener != null) {
            String str1 = this.TAG;
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("result=");
            stringBuilder1.append(paramString);
           // DebugLog.log(str1, stringBuilder1.toString());
            try {
                str1 = AES256Cipher.AES_Decode(paramString, Constants.PRIVATE_KEY);
            } catch (Exception exception) {
                str1 = "";
            }
            String str2 = this.TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("AESresult=");
            stringBuilder2.append(str1);
           // DebugLog.log(str2, stringBuilder2.toString());
            this.mDownloadedListener.onFinish(this.mLoaderToUIListener, this.mUrl, str1);
        }
        super.onPostExecute(paramString);
    }*/


}
