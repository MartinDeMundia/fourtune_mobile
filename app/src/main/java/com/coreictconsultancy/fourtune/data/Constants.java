package com.coreictconsultancy.fourtune.data;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

public class Constants {
    public static final int ADV_FULLSCRENN_COUNT = 15;

    public static String ActionBarColor;

    public static String AdsBanner;

    public static String AdsInter;

    public static final String CONFIG_APP_16LABEL = "CONFIG_APP_16LABEL";

    public static final String CONFIG_APP_ADV = "CONFIG_APP_ADV";

    public static final String CONFIG_APP_ADVID = "CONFIG_APP_ADVID";

    public static final String CONFIG_APP_ADV_ENABLE = "CONFIG_APP_ADVERTISEMENT";

    public static final String CONFIG_APP_ADV_TYPE = "CONFIG_APP_ADV_TYPE";

    public static final String CONFIG_APP_CHANEL = "CONFIG_APP_CHANEL";

    public static final String CONFIG_APP_ID = "CONFIG_APP_ID";

    public static final String CONFIG_APP_LINK = "CONFIG_APP_LINK";

    public static final String CONFIG_APP_MORELINK = "CONFIG_APP_MORELINK";

    public static final String CONFIG_APP_NODID = "CONFIG_APP_NODID";

    public static final String CONFIG_APP_NODMESSAGE = "CONFIG_APP_NODMESSAGE";

    public static final String CONFIG_APP_QUALITY = "CONFIG_APP_QUALITY_DVD_HD";

    public static final String CONFIG_APP_VERSION = "CONFIG_APP_VERSION";

    public static final String CONFIG_APP_ZING_KEY = "CONFIG_APP_ZING_KEY";

    public static String DEVICE_INFOR = "";

    public static String DEVICE_NAME;

    public static final int PER_PAGE = 32;

    public static final int PER_PAGE_VIDEO = -1;

    public static String PRIVATE_KEY = "bnhit22@1234bhhdrasem@7884351234";

    public static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

    public static String StartAppAppID;

    public static String StartAppDevID;

    public static final int TASK_BAD_AUTHENTICATION = 2;

    public static final int TASK_FAIL = 1;

    public static final int TASK_SUCCESSFUL = 0;

    public static final long TIME_CACHE = 18000L;

    public static String TOKEN_ACTIVE;

    public static String TOKEN_GOOGLE;

    public static String TOKEN_KEY = "ggteam@android@123167";

    public static final int kAdvTime = 1;

    public static final int kAdvertisement_TypeGoogleAdmod = 0;

    public static final int kAdvertisement_TypeService = 1;

    public static final String kEmail = "homecinema.mobi@gmail.com";

    public static final String kGetAdv = "action=getadv";

    public static final String kGetCateHot = "movies?type=update";

    public static final String kGetCateMovie = "movies?type=";

    public static final String kGetCateNew = "movies?type=new";

    public static final String kGetCatePopular = "movies?type=popular";

    public static final String kGetCateRating = "movies?type=rating";

    public static final String kGetCateTvShows = "tvshow?type=";

    public static final String kGetChanel = "listcategory?";

    public static final String kGetCheckPlayer = "checkplayer";

    public static final String kGetLink = "film?action=getlink";

    public static final String kGetListCategory = "category?";

    public static final String kGetListStream = "stream";

    public static final String kGetListVideo = "detail";

    public static final String kGetMoreApps = "action=getmoreapps";

    public static final String kGetReport = "getreport?";

    public static final String kGetSearch = "movies?type=search";

    public static final String kGetServer = "listserver?";

    public static final String kGetStreamType = "streamtype?";

    public static final String kGetVersion = "getversion?";

    public static final String kLinkApp = "http://homecinema.mobi/";

    public static final String kLinkMoreApp = "http://homecinema.mobi/";

    public static final String kLinkWebsite = "http://homecinema.mobi/";

    public static final String kOS = "android";

    public static final String kServiceURL = "https://appmoviehd.info/admin/index.php/apiandroid/";

    public static String kVersion = "";

    public static final String kYoutubeThumbnailURL = "http://i1.ytimg.com/vi/";

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Build.MANUFACTURER);
        stringBuilder.append(Build.MODEL);
        DEVICE_NAME = stringBuilder.toString();
        AdsBanner = "";
        AdsInter = "";
        StartAppDevID = "";
        StartAppAppID = "";
        ActionBarColor = "#FF333333";
        TOKEN_GOOGLE = "google";
        TOKEN_ACTIVE = "active";
    }

    public static int getColumeEpisodeList(Configuration paramConfiguration, Activity paramActivity) {
        boolean bool = Utils.isTablet((Context)paramActivity);
        int i = 1;
        if (bool) {
            i = paramConfiguration.orientation;
            return 1;
        }
        if (paramConfiguration.orientation == 2)
            i = 2;
        return i;
    }

    public static int getColumeMovieList(Configuration paramConfiguration, Activity paramActivity) {
        return Utils.isTablet((Context)paramActivity) ? ((paramConfiguration.orientation == 2) ? 6 : 4) : ((paramConfiguration.orientation == 2) ? 5 : 3);
    }

    public static int getGridPading(Configuration paramConfiguration, Activity paramActivity) {
        return Utils.isTablet((Context)paramActivity) ? 20 : 10;
    }
}