package com.coreictconsultancy.fourtune.data;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.coreictconsultancy.fourtune.data.Constants;
import com.coreictconsultancy.fourtune.data.Utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataLoader extends AsyncTask<String, Void, String> {
    private String TAG = "DataLoader";

    private DownloadedListener mDownloadedListener;

    private LoaderToUIListener mLoaderToUIListener;

    private String mUrl;

    public DataLoader(LoaderToUIListener paramLoaderToUIListener, DownloadedListener paramDownloadedListener) {
        this.mLoaderToUIListener = paramLoaderToUIListener;
        this.mDownloadedListener = paramDownloadedListener;
    }

    public static String convertinputStreamToString(InputStream paramInputStream) throws IOException {
/*        if (paramInputStream != null) {
            null = new StringBuilder();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "UTF-8"));
                while (true) {
                    String str = bufferedReader.readLine();
                    if (str != null) {
                        null.append(str);
                        null.append("\n");
                        continue;
                    }
                    return null.toString();
                }
            } finally {
                paramInputStream.close();
            }
        }*/
        return "";
    }

    protected String doInBackground(String... paramVarArgs) {
        String str = this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("params[0] == ");
        stringBuilder.append(paramVarArgs[0]);

        if (TextUtils.isEmpty(paramVarArgs[0]))
            return null;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection)(new URL(paramVarArgs[0])).openConnection();
            httpURLConnection.setConnectTimeout(60000);
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                str = Utils.convertStreamToString(bufferedInputStream);
                bufferedInputStream.close();
                String str1 = this.TAG;
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("params[0] response == ");
                stringBuilder1.append(str);

                return str;
            } finally {
                httpURLConnection.disconnect();
            }
        } catch (Exception exception) {
            str = this.TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Error in http connection ");
            stringBuilder.append(exception.toString());
            Log.e(str, stringBuilder.toString());
            return null;
        }
    }

    protected void onPostExecute(String paramString) {
        if (this.mDownloadedListener != null) {
            String str;
            try {
                str = AES256Cipher.AES_Decode(paramString, Constants.PRIVATE_KEY);
            } catch (Exception exception) {
                str = "";
            }
            this.mDownloadedListener.onFinish(this.mLoaderToUIListener, this.mUrl, str);
        }
        super.onPostExecute(paramString);
    }
}