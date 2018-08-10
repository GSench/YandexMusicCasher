package ru.yandexmusiccasher.presentation;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.provider.DocumentFile;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.yandexmusiccasher.R;
import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.utils.HttpParams;
import ru.yandexmusiccasher.domain.utils.Pair;
import ru.yandexmusiccasher.domain.utils.function;
import ru.yandexmusiccasher.presentation.utils.FileUtil;
import ru.yandexmusiccasher.presentation.utils.ToastService;


/**
 * Created by grish on 01.05.2017.
 */

public class AndroidInterface implements SystemInterface {

    private Context act;
    public static final String SPREF = "preferences";

    public AndroidInterface(Context act){
        this.act = act;
    }

    @Override
    public void doOnBackground(final function<Void> background) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                background.run();
            }
        }).start();

    }

    @Override
    public Pair<byte[], HttpParams> httpGet(URL url, HttpParams params) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        if(params!=null){
            for(Pair<String, String> header: params.getHeaders()){
                urlConnection.setRequestProperty(header.f, header.s);
            }
        }
        urlConnection.setRequestMethod("GET");
        try {
            //Opening input stream
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            //getting response parameters
            int responseCode = urlConnection.getResponseCode();
            Iterator<Map.Entry<String, List<String>>> headers = urlConnection.getHeaderFields().entrySet().iterator();

            //proceeding response parameters
            HttpParams response = new HttpParams();
            ArrayList<Pair<String, String>> headersParam = new ArrayList<>();
            while (headers.hasNext()){
                Map.Entry<String, List<String>> entry = headers.next();
                headersParam.add(new Pair<>(entry.getKey(), entry.getValue().get(0)));
            }
            response.setHeaders(headersParam);
            response.setResultCode(responseCode);

            //reading input stream
            int available = in.available();
            if(available==0)
                return new Pair<>(readByByte(in), response);
            else
                return new Pair<>(IOUtils.readFully(in, in.available()), response);
        } finally {
            urlConnection.disconnect();
        }
    }

    @Override
    public String[] getSavedStringArray(String title, String[] def) {
        Set<String> defaultArr;
        if(def==null) defaultArr = null;
        else defaultArr = new HashSet<>(Arrays.asList(def));
        Set<String> stringSet = act.getSharedPreferences(SPREF, Context.MODE_PRIVATE).getStringSet(title, defaultArr);
        return stringSet!=null ? stringSet.toArray(new String[stringSet.size()]) : null;
    }

    @Override
    public void saveStringArray(String title, String[] array) {
        act.getSharedPreferences(SPREF, Context.MODE_PRIVATE)
                .edit()
                .putStringSet(title, new HashSet<>(Arrays.asList(array)))
                .commit();
    }

    private byte[] readByByte(InputStream in) throws IOException {
        ArrayList<Byte> byteChain = new ArrayList<>();
        while (true){
            try {
                byteChain.add(IOUtils.readFully(in, 1)[0]);
            } catch (EOFException e){
                break;
            }
        }
        byte[] ret = new byte[byteChain.size()];
        for(int i=0; i<byteChain.size(); i++) ret[i]=byteChain.get(i);
        return ret;
    }

    @Override
    public String getSavedString(String title, String def) {
        return act.getSharedPreferences(SPREF, Context.MODE_PRIVATE).getString(title, def);
    }

    @Override
    public void saveString(String title, String string) {
        act.getSharedPreferences(SPREF, Context.MODE_PRIVATE)
                .edit()
                .putString(title, string)
                .commit();
    }

    /**
    @Override
    public int getSavedInt(String title, int def) {
        return act.getSharedPreferences(SPREF, Context.MODE_PRIVATE).getInt(title, def);
    }

    @Override
    public void saveInt(String title, int i) {
        act.getSharedPreferences(SPREF, Context.MODE_PRIVATE)
                .edit()
                .putInt(title, i)
                .commit();
    }*/

    @Override
    public void removeSaved(String str) {
        act.getSharedPreferences(SPREF, Context.MODE_PRIVATE)
                .edit()
                .remove(str)
                .commit();
    }

    @Override
    public String md5(String s) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(s.getBytes());
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

    @Override
    public void startDownloadingFile(String url, String path, String filename) {
        Uri source = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(source);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(filename.substring(0, filename.lastIndexOf(".")));
        File dir = new File(path);
        dir.mkdirs();
        File file = new File(dir, filename);
        request.setDestinationUri(Uri.fromFile(file));
        DownloadManager manager = (DownloadManager) act.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    public boolean checkUPathIsAvailable(String uPath) {
        if(!uPath.startsWith("content://com.android.externalstorage.documents/tree/")) return false;
        List<UriPermission> uriPermissions = act.getContentResolver().getPersistedUriPermissions();
        for(UriPermission permission: uriPermissions){
            if(permission.getUri().toString().equals(uPath)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String copyFile(String file, String toDir) throws IOException {
        Uri fileUri = Uri.parse(file);
        ContentResolver cR = act.getContentResolver();
        DocumentFile docDir = DocumentFile.fromTreeUri(act, Uri.parse(toDir));
        DocumentFile newFile = docDir.createFile(cR.getType(fileUri), fileUri.getLastPathSegment());
        InputStream in = null;
        OutputStream out = null;
        try {
            in = cR.openInputStream(fileUri);
            out = cR.openOutputStream(newFile.getUri());
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        return newFile.getUri().toString();
    }

    @Override
    public void deleteMusicFileFromExtStorageDirByUri(String uri) throws Exception {
        File dir = act.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        String filename = Uri.parse(uri).getLastPathSegment();
        File file = new File(dir, filename);
        if(!file.delete()) throw new Exception();
    }

    @Override
    public String getCashPath() {
        return act.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    }

    @Override
    public String isMusicDownloaded(String trackID, String path) {
        DocumentFile docDir = DocumentFile.fromTreeUri(act, Uri.parse(path));
        for (DocumentFile file: docDir.listFiles())
            if(file.getName().endsWith(trackID)) return file.getUri().toString();
        return null;
    }

    public static void playMusic(String uri, Context context){
        System.out.println("Music uri: "+uri);
        Uri docUri = Uri.parse(uri);
        String filePath = FileUtil.getFullPathFromDocumentUri(docUri, context);
        if(filePath==null) return;
        File file = new File(filePath);
        System.out.println("Music path: "+filePath);
        Intent player = new Intent(Intent.ACTION_VIEW);
        player.setDataAndType(Uri.fromFile(file), "audio/mp3");
        player.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (player.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(player);
        } else ToastService.show(context.getString(R.string.no_music_app), context);
    }

}
