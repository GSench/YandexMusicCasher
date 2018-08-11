package ru.yandexmusiccasher.domain.services;

import java.io.IOException;
import java.net.URL;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.utils.Pair;

/**
 * Created by grish on 11.08.2018.
 */

public class Network {

    private HttpHeadersManager httpHeadersManager;
    private SystemInterface system;

    public Network(SystemInterface system){
        this.system=system;
        httpHeadersManager = new HttpHeadersManager(system);
    }

    public String yRequest(String url) throws YandexCaptchaException, IOException {
        Pair<byte[], HttpParams> response = system.httpGet(new URL(url), httpHeadersManager.getHttpParams());
        httpHeadersManager.updateHttpParams(response.s);
        String txt = new String(response.f, "UTF-8");
        if(yandexCheck(txt)){
            throw new YandexCaptchaException();
        }
        return txt;
    }

    public byte[] yRawRequest(String url) throws YandexCaptchaException, IOException {
        Pair<byte[], HttpParams> response = system.httpGet(new URL(url), httpHeadersManager.getHttpParams());
        httpHeadersManager.updateHttpParams(response.s);
        String txt = new String(response.f, "UTF-8");
        if(yandexCheck(txt)){
            throw new YandexCaptchaException();
        }
        return response.f;
    }

    private boolean yandexCheck(String response){
        return response.contains("https://music.yandex.ru/captcha/");
    }

    public HttpParams getCurrentHttpParams(){
        return httpHeadersManager.getHttpParams();
    }

}
