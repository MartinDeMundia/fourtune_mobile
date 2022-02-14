package com.coreictconsultancy.fourtune.data;

import android.content.Context;


import org.apache.http.client.entity.UrlEncodedFormEntity;

public class DataCache {
    private static final String TAG = "DataCache";

    private static com.coreictconsultancy.fourtune.data.DataCache mDataCache;

    private Context mContext;

    private DataLoader mDataLoader;

    private DataLoaderPost mDataLoaderPost;

    public DataCache(Context paramContext) {
        this.mContext = paramContext;
    }

    public static com.coreictconsultancy.fourtune.data.DataCache getInstance(Context paramContext) {
        if (mDataCache == null)
            mDataCache = new com.coreictconsultancy.fourtune.data.DataCache(paramContext);
        return mDataCache;
    }

    public void cancelLoading() {
        DataLoader dataLoader = this.mDataLoader;
        if (dataLoader != null)
            dataLoader.cancel(true);
    }

    public void loadData(String Url,UrlEncodedFormEntity UrlEntities, LoaderToUIListener paramLoaderToUIListener) {

      this.mDataLoaderPost = new DataLoaderPost(UrlEntities,paramLoaderToUIListener, new DownloadedListener() {

            public void onFinish(LoaderToUIListener param1LoaderToUIListener, String param1String1, String param1String2) {
                if (param1LoaderToUIListener != null)
                    param1LoaderToUIListener.loadedData(param1String2);
            }
        });
        this.mDataLoaderPost.execute(Url);
    }

}
