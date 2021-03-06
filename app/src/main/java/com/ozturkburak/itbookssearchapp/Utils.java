package com.ozturkburak.itbookssearchapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by burak on 28.06.2016.
 */
public class Utils
{
    public static String getJsonDatafromUrl(String urlStr) throws Exception
    {
        String result;
        HttpURLConnection connection = null;
        try
        {
            connection = openHttpConnection(urlStr);

            result = readInputStream(connection.getInputStream());
            connection.disconnect();

        }
        catch (Exception ex)
        {
            Log.d("Exception" , ex.getMessage());
            throw ex;
        }finally
        {
            connection.disconnect();
        }

        return result;
    }


    public static HttpURLConnection openHttpConnection(String urlStr) throws IOException
    {
        if (!HttpUrlBuilder.urlIsValid(urlStr))
            throw  new IllegalArgumentException("Url is invalid");

        URL url = null;
        HttpURLConnection conn = null;

        try
        {
            url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
        }
        catch (Exception ex)
        {
            Log.d("Exception" , ex.getMessage());
            throw ex;
        }

        return conn;
    }


    private static String readInputStream(InputStream is)throws Exception
    {
        StringBuilder result = new StringBuilder();
        try
        {
            InputStreamReader isr = new InputStreamReader(is);

            char[] buffer = new char[1024];
            int readCount;

            while ( (readCount = isr.read(buffer)) >0 )
            {
                result.append(buffer , 0 , readCount);
                buffer = new char[1024];
            }

        }
        catch (Exception ex)
        {
            Log.d("Exception" , ex.getMessage());
            throw ex;
        }
        finally
        {
            try
            {is.close();}
            catch (IOException e){   e.printStackTrace();}
        }

        return result.toString();
    }


    public static Bitmap saveImagefromUrl(Context context , String urlStr , String imageName) throws IOException
    {
        Bitmap result = null;
        HttpURLConnection conn = openHttpConnection(urlStr);

        InputStream bis = new BufferedInputStream(conn.getInputStream());
        result = BitmapFactory.decodeStream(bis);

        OutputStream os = context.openFileOutput( imageName , Context.BIND_AUTO_CREATE);
        result.compress(Bitmap.CompressFormat.JPEG , 100 , os);


        return  result;
    }

    public static Bitmap downloadImagefromUrl(String urlStr) throws IOException
    {
        Bitmap result = null;
        HttpURLConnection conn = openHttpConnection(urlStr);

        InputStream bis = new BufferedInputStream(conn.getInputStream());
        result = BitmapFactory.decodeStream(bis);

        return  result;
    }


    public static Intent openWebBrowser(String url)
    {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        return new Intent(Intent.ACTION_VIEW , Uri.parse(url));
    }

    public static void openChromeCustomTab(Activity activity , String url)
    {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        CustomTabsIntent customTabsIntent = builder
                .setToolbarColor(Color.parseColor("#303f9f"))
                .setShowTitle(true)
                .build();

        customTabsIntent.launchUrl(activity , Uri.parse(url));

    }




    public static void hideKeyboard(Activity activity)
    {
        View view = activity.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}











