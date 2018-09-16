package ru.yandexmusiccasher.domain.services;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.utils.Pair;

/**
 * Created by grish on 11.08.2018.
 */

public class Network {

    private SystemInterface system;
    private HttpParams httpParams;

    public Network(SystemInterface system){
        this.system=system;
    }

    public String yRequest(String url) throws YandexCaptchaException, IOException {
        Pair<byte[], HttpParams> response = system.httpGet(new URL(url), getInitialHttpParams());
        String txt = new String(response.f, "UTF-8");
        if(yandexCheck(txt)){
            throw new YandexCaptchaException();
        }
        return txt;
    }

    public byte[] yRawRequest(String url) throws YandexCaptchaException, IOException {
        Pair<byte[], HttpParams> response = system.httpGet(new URL(url), getInitialHttpParams());
        String txt = new String(response.f, "UTF-8");
        if(yandexCheck(txt)){
            throw new YandexCaptchaException();
        }
        return response.f;
    }

    private boolean yandexCheck(String response){
        return response.contains("https://music.yandex.ru/captcha/");
    }

    public HttpParams getInitialHttpParams(){
        if(httpParams!=null) return httpParams;
        ArrayList<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<String, String>("X-Retpath-Y", "https://music.yandex.ru/"));
        headers.add(new Pair<String, String>("http.useragent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1"));
        httpParams = new HttpParams();
        httpParams.setHeaders(headers);
        return httpParams;
    }

}
