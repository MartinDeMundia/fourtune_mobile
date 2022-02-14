package com.coreictconsultancy.fourtune.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

//import com.coreictconsultancy.tazama.main.MyFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Utils {
    private static final String FILE_NAME_RESERVED = "|\\?*#<\":>+[]/'";

    private static final String TAG = "Utils";

    public static boolean checkFileExistent(String paramString) {
        return (new File(paramString)).exists();
    }

    public static String convertPath(String paramString) {
        File file = Environment.getExternalStorageDirectory();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(file.getAbsolutePath());
        stringBuilder.append(File.separator);
        stringBuilder.append("MovieHD/.cache");
        stringBuilder.append(File.separator);
        stringBuilder.append(paramString);
        return stringBuilder.toString();
    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    public static String convertStreamToString(InputStream paramInputStream) {
        // Byte code:
        //   0: new java/io/BufferedReader
        //   3: dup
        //   4: new java/io/InputStreamReader
        //   7: dup
        //   8: aload_0
        //   9: invokespecial <init> : (Ljava/io/InputStream;)V
        //   12: invokespecial <init> : (Ljava/io/Reader;)V
        //   15: astore_2
        //   16: new java/lang/StringBuilder
        //   19: dup
        //   20: invokespecial <init> : ()V
        //   23: astore_1
        //   24: aload_2
        //   25: invokevirtual readLine : ()Ljava/lang/String;
        //   28: astore_3
        //   29: aload_3
        //   30: ifnull -> 70
        //   33: new java/lang/StringBuilder
        //   36: dup
        //   37: invokespecial <init> : ()V
        //   40: astore #4
        //   42: aload #4
        //   44: aload_3
        //   45: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   48: pop
        //   49: aload #4
        //   51: ldc '\\n'
        //   53: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   56: pop
        //   57: aload_1
        //   58: aload #4
        //   60: invokevirtual toString : ()Ljava/lang/String;
        //   63: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   66: pop
        //   67: goto -> 24
        //   70: aload_0
        //   71: invokevirtual close : ()V
        //   74: goto -> 98
        //   77: astore_0
        //   78: aload_0
        //   79: invokevirtual printStackTrace : ()V
        //   82: goto -> 98
        //   85: astore_1
        //   86: goto -> 103
        //   89: astore_2
        //   90: aload_2
        //   91: invokevirtual printStackTrace : ()V
        //   94: aload_0
        //   95: invokevirtual close : ()V
        //   98: aload_1
        //   99: invokevirtual toString : ()Ljava/lang/String;
        //   102: areturn
        //   103: aload_0
        //   104: invokevirtual close : ()V
        //   107: goto -> 115
        //   110: astore_0
        //   111: aload_0
        //   112: invokevirtual printStackTrace : ()V
        //   115: goto -> 120
        //   118: aload_1
        //   119: athrow
        //   120: goto -> 118
        // Exception table:
        //   from	to	target	type
        //   24	29	89	java/io/IOException
        //   24	29	85	finally
        //   33	67	89	java/io/IOException
        //   33	67	85	finally
        //   70	74	77	java/io/IOException
        //   90	94	85	finally
        //   94	98	77	java/io/IOException
        //   103	107	110	java/io/IOException
        return null;
    }

    public static void deleteFile(Context paramContext, String paramString) {
/*        try {
            (new File(paramString)).delete();
            return;
        } catch (Exception exception) {
            Toast.makeText(paramContext, "Can't not delete this file", 1).show();
            return;
        }*/
    }

    @SuppressLint({"NewApi"})
    public static void downloading(Context paramContext, String paramString1, String paramString2, String paramString3) {
/*        if (paramString3 != null || paramString3.length() > 10)
            try {
                paramString2 = getUniqueFileName(paramString2);
                String str1 = getUniqueFileName(paramString1);
                DownloadManager downloadManager = (DownloadManager)paramContext.getSystemService("download");
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(paramString3));
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(getExternalDownloadDir(paramContext).getAbsolutePath());
                stringBuilder2.append(File.separator);
                stringBuilder2.append(str1);
                (new File(stringBuilder2.toString())).mkdirs();
                if (Build.VERSION.SDK_INT >= 11) {
                    request.setDescription(paramString2);
                    request.setTitle(str1);
                    request.setNotificationVisibility(1);
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("/MovieHD/Download/");
                stringBuilder2.append(str1);
                stringBuilder2.append("/");
                stringBuilder2.append(paramString2);
                stringBuilder2.append(".mp4");
                String str2 = stringBuilder2.toString();
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Download to: ");
                stringBuilder3.append(str2);
                Toast.makeText(paramContext, stringBuilder3.toString(), 1).show();
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("/MovieHD/Download/");
                stringBuilder1.append(str1);
                str1 = stringBuilder1.toString();
                stringBuilder1 = new StringBuilder();
                stringBuilder1.append(paramString2);
                stringBuilder1.append(".mp4");
                request.setDestinationInExternalPublicDir(str1, stringBuilder1.toString());
                downloadManager.enqueue(request);
                return;
            } catch (Exception exception) {
                Toast.makeText(paramContext, "Can't not download this video", 1).show();
            }*/
    }

    public static Drawable drawableFromUrl(String paramString) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection)(new URL(paramString)).openConnection();
        httpURLConnection.connect();
        return (Drawable)new BitmapDrawable(BitmapFactory.decodeStream(httpURLConnection.getInputStream()));
    }

    @SuppressLint({"NewApi"})
    public static File getExternalAppDir(Context paramContext) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getPath());
        stringBuilder.append("/MovieHD");
        return new File(stringBuilder.toString());
    }

    @SuppressLint({"NewApi"})
    public static File getExternalCacheDir(Context paramContext) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("/MovieHD");
        stringBuilder1.append(File.separator);
        stringBuilder1.append("Cache");
        String str = stringBuilder1.toString();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(Environment.getExternalStorageDirectory().getPath());
        stringBuilder2.append(str);
        File file = new File(stringBuilder2.toString());
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    @SuppressLint({"NewApi"})
    public static File getExternalDownloadDir(Context paramContext) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("/MovieHD");
        stringBuilder1.append(File.separator);
        stringBuilder1.append("Download");
        String str = stringBuilder1.toString();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(Environment.getExternalStorageDirectory().getPath());
        stringBuilder2.append(str);
        File file = new File(stringBuilder2.toString());
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    @SuppressLint({"NewApi"})
    public static File getExternalFavoritesDir(Context paramContext) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("/MovieHD");
        stringBuilder1.append(File.separator);
        stringBuilder1.append("Favorites");
        String str = stringBuilder1.toString();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(Environment.getExternalStorageDirectory().getPath());
        stringBuilder2.append(str);
        File file = new File(stringBuilder2.toString());
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    @SuppressLint({"NewApi"})
    public static File getExternalVPlayerDownloadDir(Context paramContext) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("/VPlayer");
        stringBuilder1.append(File.separator);
        stringBuilder1.append("Download");
        String str = stringBuilder1.toString();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(Environment.getExternalStorageDirectory().getPath());
        stringBuilder2.append(str);
        File file = new File(stringBuilder2.toString());
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    public static JSONObject getJSONObject(String paramString) {
        try {
            if (!TextUtils.isEmpty(paramString))
                return new JSONObject(paramString);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }
        return null;
    }

  /*  public static ArrayList<MyFile> getListMyFile(String paramString) {
        ArrayList<MyFile> arrayList = new ArrayList();
        File[] arrayOfFile = (new File(paramString)).listFiles();
        Arrays.sort(arrayOfFile, new Comparator<File>() {
            public int compare(File param1File1, File param1File2) {
                long l1 = param1File1.lastModified();
                long l2 = param1File2.lastModified();
                return (l1 > l2) ? 1 : ((l1 < l2) ? -1 : 0);
            }
        });
        int j = arrayOfFile.length;
        for (int i = 0; i < j; i++) {
            File file = arrayOfFile[i];
            String str = file.getName();
            File[] arrayOfFile1 = getListVideoFile(file.getPath());
            for (int k = 0; k < arrayOfFile1.length; k++) {
                MyFile myFile = new MyFile();
                myFile.Folder = str;
                myFile.Name = arrayOfFile1[k].getName();
                myFile.Path = arrayOfFile1[k].getPath();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("myFile.Path ");
                stringBuilder.append(myFile.Path);
                DebugLog.log("Utils", stringBuilder.toString());
                arrayList.add(myFile);
            }
        }
        return arrayList;
    }
*/
    public static File[] getListVideoFile(String paramString) {
        return (new File(paramString)).listFiles(new FileFilter() {
            public boolean accept(File param1File) {
                return (param1File.getPath().endsWith(".mp4") || param1File.getPath().endsWith(".avi"));
            }
        });
    }

    public static String getMd5Digest(String paramString) {
        try {
            return (new BigInteger(1, MessageDigest.getInstance("MD5").digest(paramString.getBytes()))).toString(16);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
        }
    }

    public static String getStringHtml(String paramString) throws Exception {
        URLConnection uRLConnection = (new URL(paramString)).openConnection();
        uRLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
        InputStreamReader inputStreamReader = new InputStreamReader(uRLConnection.getInputStream(), "UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            int i = inputStreamReader.read();
            if (i != -1) {
                stringBuilder.append((char)i);
                continue;
            }
            inputStreamReader.close();
            return stringBuilder.toString();
        }
    }

    public static String getSubString(String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
        int j = paramString1.lastIndexOf(paramString2);
        int i = j;
        if (paramBoolean)
            i = j + paramString2.length();
        return paramString1.substring(i, paramString1.indexOf(paramString3, i));
    }

    public static String getUniqueFileName(String paramString) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] arrayOfChar = paramString.toCharArray();
        int j = arrayOfChar.length;
        for (int i = 0; i < j; i++) {
            Character character = Character.valueOf(arrayOfChar[i]);
            if ("|\\?*#<\":>+[]/'".indexOf(character.charValue()) == -1)
                stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }

    public static boolean isSDCardPresent() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean isTablet(Context paramContext) {
        return (((paramContext.getResources().getConfiguration()).screenLayout & 0xF) >= 3);
    }

    public static String md5(String paramString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(paramString.getBytes());
            byte[] arrayOfByte = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < arrayOfByte.length; i++) {
                String str = Integer.toHexString(arrayOfByte[i] & 0xFF);
                if (str.length() == 1)
                    stringBuffer.append('0');
                stringBuffer.append(str);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
            return "";
        }
    }

    public static int randInt(int paramInt1, int paramInt2) {
        return (new Random()).nextInt(paramInt2 - paramInt1 + 1) + paramInt1;
    }

    private String readFromFile(Context paramContext) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getExternalFavoritesDir(paramContext));
            stringBuilder.append(File.separator);
            stringBuilder.append("/data.txt");
            FileInputStream fileInputStream = paramContext.openFileInput((new File(stringBuilder.toString())).getAbsolutePath());
            if (fileInputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                StringBuilder stringBuilder1 = new StringBuilder();
                while (true) {
                    String str = bufferedReader.readLine();
                    if (str != null) {
                        stringBuilder1.append(str);
                        continue;
                    }
                    fileInputStream.close();
                    return stringBuilder1.toString();
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("File not found: ");
            stringBuilder.append(fileNotFoundException.toString());
            Log.e("login activity", stringBuilder.toString());
        } catch (IOException iOException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can not read file: ");
            stringBuilder.append(iOException.toString());
            Log.e("login activity", stringBuilder.toString());
            return "";
        }
        return "";
    }

   /* public static void showMessage(Context paramContext, String paramString) {
        Toast.makeText(paramContext, paramString, 1).show();
    }*/

    public static void writeBitmapToFile(String paramString, Bitmap paramBitmap) throws IOException {
        if (paramString != null) {
            if (paramBitmap != null) {
                StringBuilder stringBuilder3;
                File file = new File(paramString);
                boolean bool = file.exists();
                StringBuilder stringBuilder4 = null;
                String str = null;
                if (bool) {
                    String str1;
                    if (file.isDirectory()) {
                        str1 = "directory";
                    } else if (!file.canWrite()) {
                        str1 = "not writable";
                    } else {
                        str1 = null;
                    }
                    if (str1 != null) {
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("FileUtils.WriteToFile");
                        stringBuilder3.append(": file '");
                        stringBuilder3.append(paramString);
                        stringBuilder3.append("' is ");
                        stringBuilder3.append(str1);
                        throw new IOException(stringBuilder3.toString());
                    }
                }
                if (file.getParentFile() != null)
                    file.getParentFile().mkdirs();
                paramString = str;
                try {
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                    try {
                        ///stringBuilder3.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
                        bufferedOutputStream.flush();
                        return;
                    } catch (Exception exception1) {
                        BufferedOutputStream bufferedOutputStream1 = bufferedOutputStream;
                    } finally {
                        Exception exception1;
                        stringBuilder3 = null;
                    }
                } catch (Exception exception) {
                    stringBuilder3 = stringBuilder4;
                } finally {}
                StringBuilder stringBuilder2 = stringBuilder3;
                stringBuilder4 = new StringBuilder();
                stringBuilder2 = stringBuilder3;
                stringBuilder4.append("FileUtils.WriteToFile");
                stringBuilder2 = stringBuilder3;
                stringBuilder4.append(" failed, got: ");
                stringBuilder2 = stringBuilder3;
               // stringBuilder4.append(exception.toString());
                stringBuilder2 = stringBuilder3;
                throw new IOException(stringBuilder4.toString());
            }
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("FileUtils.WriteToFile");
            stringBuilder1.append(": bitmap is null");
            throw new IOException(stringBuilder1.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FileUtils.WriteToFile");
        stringBuilder.append(": filename is null");
        throw new IOException(stringBuilder.toString());
    }

    private void writeFromFile(Context paramContext, String paramString) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getExternalFavoritesDir(paramContext));
            stringBuilder.append(File.separator);
            stringBuilder.append("/data.txt");
            File file = new File(stringBuilder.toString());
            file.mkdir();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(file, "url.txt")));
            outputStreamWriter.write(paramString);
            outputStreamWriter.close();
            return;
        } catch (IOException iOException) {
            iOException.printStackTrace();
            return;
        }
    }

    public static void writeStringToFile(String paramString, File paramFile) {}

    public Bitmap centerCropImage(Bitmap paramBitmap) {
        return (paramBitmap.getWidth() >= paramBitmap.getHeight()) ? Bitmap.createBitmap(paramBitmap, paramBitmap.getWidth() / 2 - paramBitmap.getHeight() / 2, 0, paramBitmap.getHeight(), paramBitmap.getHeight()) : Bitmap.createBitmap(paramBitmap, 0, paramBitmap.getHeight() / 2 - paramBitmap.getWidth() / 2, paramBitmap.getWidth(), paramBitmap.getWidth());
    }
}
