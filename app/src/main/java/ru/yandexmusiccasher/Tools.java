package ru.yandexmusiccasher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * Created by GSench on 18.05.2016.
 */
public class Tools {

    public static String md5(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);
        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }
        return md5Hex;
    }

    public static void playMusic(String uri, Context context){
        Intent player = new Intent(Intent.ACTION_VIEW);
        player.setDataAndType(Uri.parse(uri), "audio/mp3");
        player.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (player.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(player);
        } else ToastService.show(context.getString(R.string.no_music_app), context);
    }

    public static InputStream download(String urlStr, CookieManager msCookieManager) throws IOException {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlStr);
        urlConnection =(HttpURLConnection)url.openConnection();
        if(msCookieManager!=null&&msCookieManager.getCookieStore().getCookies().size() > 0) {
            List<HttpCookie> cookies = msCookieManager.getCookieStore().getCookies();
            urlConnection.setRequestProperty("Cookie", TextUtils.join(";", cookies));
        }

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("http.useragent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
        urlConnection.connect();
        inputStream = urlConnection.getInputStream();

        String COOKIES_HEADER = "Set-Cookie";
        Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
        /**
         if(cookiesHeader != null) {
         for (String cookie : cookiesHeader) {
         msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
         }
         }*/
        return inputStream;
    }

    public static String toString(InputStream inputStream) throws IOException {
        try {
            return IOUtils.toString(inputStream);
        } finally {
            inputStream.close();
        }
    }

}
