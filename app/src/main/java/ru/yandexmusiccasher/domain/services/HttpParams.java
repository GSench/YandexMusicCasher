package ru.yandexmusiccasher.domain.services;

import java.util.ArrayList;

import ru.yandexmusiccasher.domain.utils.Pair;

/**
 * Created by Григорий Сенченок on 05.05.2017.
 */

public class HttpParams {

    private ArrayList<Pair<String, String>> headers;
    private int resultCode=-1;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public ArrayList<Pair<String, String>> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<Pair<String, String>> headers) {
        this.headers = headers;
    }

}
