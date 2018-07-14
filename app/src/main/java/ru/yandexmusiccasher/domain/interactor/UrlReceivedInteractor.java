package ru.yandexmusiccasher.domain.interactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.YandexCaptchaException;
import ru.yandexmusiccasher.domain.usecase.UrlReceivedUseCase;
import ru.yandexmusiccasher.domain.utils.HttpParams;
import ru.yandexmusiccasher.domain.utils.Pair;
import ru.yandexmusiccasher.presentation.presenter.UrlReceiverPresenter;

/**
 * Created by grish on 08.07.2018.
 */

public class UrlReceivedInteractor implements UrlReceivedUseCase {

    private static final String PATH = "path";
    private static final String COOKIES = "cookies";

    private SystemInterface system;
    private HttpParams httpParams;
    private String cashPath;

    public UrlReceivedInteractor(SystemInterface system, String cashPath){
        this.system=system;
        this.cashPath=cashPath;
        initHttpParams();

    }

    private void initHttpParams(){
        String cookie = system.getSavedString(COOKIES, "");
        ArrayList<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<String, String>("X-Retpath-Y", "https://music.yandex.ru/"));
        headers.add(new Pair<String, String>("Cookie", cookie));
        headers.add(new Pair<String, String>("http.useragent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1"));
        httpParams = new HttpParams();
        httpParams.setHeaders(headers);
    }

    @Override
    public void downloadTrackByUrl(String url, UrlReceiverPresenter presenter) {

        String path = system.getSavedString(PATH, null);
        if(path==null){
            presenter.onUndefinedPath();
            return;
        }

        String id = url.substring(url.lastIndexOf("/") + 1);
        String album = url.substring(url.indexOf("album/") + "album/".length());
        album = album.substring(0, album.indexOf("/"));

        String trackUrl = null, trackTitle = null;
        try {
            trackTitle = getTrack(id, album);
            trackUrl = getTrackUrl(id);
        } catch (YandexCaptchaException e) {
            presenter.onYandexCaptcha();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            presenter.onInternetError();
            return;
        } catch (JSONException e) {
            e.printStackTrace();
            presenter.onParseError();
            return;
        }
        long downloadId = system.startDownloadingFile(trackUrl, cashPath, trackTitle+".mp3", httpParams);
    }

    private String getTrackUrl(String trackId) throws IOException, JSONException, YandexCaptchaException {
        String trackInfoUrl = "https://music.yandex.ru/api/v2.1/handlers/track/"+trackId+"/track/download/m?hq=1";
        String trackInfo = yRequest(trackInfoUrl);
        JSONObject jObject = new JSONObject(trackInfo);
        String srcUrl = jObject.getString("src")+"&format=json";
        String downloadInfo = yRequest(srcUrl);
        String salt = "XGRlBW9FXlekgbPrRHuSiA";
        JSONObject jDownloadInfo = new JSONObject(downloadInfo);
        String hash = system.md5(salt + jDownloadInfo.getString("path").substring(1) + jDownloadInfo.getString("s"));
        return "https://"+jDownloadInfo.getString("host")+"/get-mp3/"+hash+"/"+jDownloadInfo.getString("ts")+jDownloadInfo.getString("path");
    }

    private String getTrack(String trackId, String albumId) throws IOException, JSONException, YandexCaptchaException {
        String url = "https://music.yandex.ru/handlers/track.jsx?track="+trackId+"%3A"+albumId;
        String response = yRequest(url);
        JSONObject jsonObject = new JSONObject(response);
        JSONObject jsonArtist = jsonObject.getJSONArray("artists").getJSONObject(0);
        String artist = jsonArtist.getString("name");
        JSONObject jsonTrack = jsonObject.getJSONObject("track");
        String title = jsonTrack.getString("title");
        String version = "";
        try {
            version = jsonTrack.getString("version");
        } catch (Exception e){}
        return artist+" - "+title+" "+version;
    }

    private boolean yandexCheck(String response){
        return response.contains("https://music.yandex.ru/captcha/");
    }

    private String yRequest(String url) throws YandexCaptchaException, IOException {
        Pair<byte[], HttpParams> response = system.httpGet(new URL(url), httpParams);
        updateHttpParams(response.s);
        String txt = new String(response.f, "UTF-8");
        if(yandexCheck(txt)){
            throw new YandexCaptchaException();
        }
        return txt;
    }

    private void updateHttpParams(HttpParams received){
        for(Pair<String, String> param: received.getHeaders())
            if(param.f.equals("Set-Cookie")){ //finds "Set-Cookie" header from received ones
                ArrayList<Pair<String, String>> headers = httpParams.getHeaders();
                int i;
                for(i=0; i<headers.size(); i++)
                    if(headers.get(i).f.equals("Cookie")) { //finds "Cookie" header from old ones
                        headers.add(i, new Pair<String, String>("Cookie",
                                headers.get(i).s + (headers.get(i).s.equals("") ? "" : "; ") + param.s)); //adds new cookies to old ones
                        break;
                    }
                system.saveString(COOKIES, headers.get(i).s);
                httpParams.setHeaders(headers);
                break;
            }
    }

}
