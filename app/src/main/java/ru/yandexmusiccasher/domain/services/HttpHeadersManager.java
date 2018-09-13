package ru.yandexmusiccasher.domain.services;

import java.util.ArrayList;
import java.util.Arrays;

import ru.yandexmusiccasher.domain.SystemInterface;
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
            if(param.f!=null&&"set-cookie".equals(param.f.toLowerCase().trim())){ //finds "Set-Cookie" header from received ones
                ArrayList<Pair<String, String>> headers = httpParams.getHeaders();
                int i;
                for(i=0; i<headers.size(); i++)
                    if(headers.get(i).f.equals("Cookie")) { //finds "Cookie" header from old ones
                        String old = headers.get(i).s;
                        headers.remove(i);
                        headers.add(i, new Pair<String, String>("Cookie", updateCookies(old,param.s))); //adds new cookies to old ones
                        break;
                    }
                system.saveString(COOKIES, headers.get(i).s);
                httpParams.setHeaders(headers);
                break;
            }
    }

    public static String updateCookies(String oldOnes, String newOnes){
        String[] newCookies = newOnes.split(";");
        ArrayList<String> oldCookies = new ArrayList<>(Arrays.asList(oldOnes.split(";")));
        if(oldCookies.size()==1&&oldCookies.get(0).trim().equals("")) oldCookies = new ArrayList<String>();
        String keyOld, keyNew;
        int i,j;
        for(i=0; i<newCookies.length; i++){
            keyNew = newCookies[i].contains("=") ? newCookies[i].split("=")[0] : newCookies[i];
            for(j=0; j<oldCookies.size(); j++){
                keyOld = oldCookies.get(j).contains("=") ? oldCookies.get(j).split("=")[0] : oldCookies.get(j);
                if(keyNew.equals(keyOld)){
                    oldCookies.remove(i);
                    oldCookies.add(j, newCookies[i]);
                    break;
                }
            }
            if(j==oldCookies.size()) oldCookies.add(newCookies[i]);
        }
        StringBuilder ret = new StringBuilder();
        for(String cookie: oldCookies) ret.append(cookie).append("; ");
        ret.delete(ret.length()-2, ret.length());
        return ret.toString();
    }


}
