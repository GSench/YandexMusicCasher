package ru.yandexmusiccasher.domain.yamusic_model;

import java.util.ArrayList;

import ru.yandexmusiccasher.domain.SystemInterface;
import ru.yandexmusiccasher.domain.utils.HttpParams;
import ru.yandexmusiccasher.domain.utils.Pair;

/**
 * Created by grish on 20.07.2018.
 */

public class HttpHeadersManager {

    private static final String COOKIES = "cookies";

    private SystemInterface system;

    private HttpParams httpParams;

    public HttpHeadersManager(SystemInterface system){
        this.system=system;
        initHttpParams();
    }

    public HttpParams getHttpParams() {
        return httpParams;
    }

    public void initHttpParams(){
        String cookie = system.getSavedString(COOKIES, "");
        ArrayList<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<String, String>("X-Retpath-Y", "https://music.yandex.ru/"));
        headers.add(new Pair<String, String>("Cookie", cookie));
        headers.add(new Pair<String, String>("http.useragent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1"));
        httpParams = new HttpParams();
        httpParams.setHeaders(headers);
    }

    public void updateHttpParams(HttpParams received){
        for(Pair<String, String> param: received.getHeaders())
            if("set-cookie".equals(param.f)){ //finds "Set-Cookie" header from received ones
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
