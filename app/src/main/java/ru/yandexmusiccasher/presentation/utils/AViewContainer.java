package ru.yandexmusiccasher.presentation.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import ru.gsench.passwordmanager.domain.utils.function;

/**
 * Created by grish on 25.03.2017.
 */

public class AViewContainer {

    public ViewGroup getContainerView() {
        return container;
    }

    private ViewGroup container;

    private function onClose;
    private function onOpen;

    public AViewContainer(ViewGroup container){
        this.container=container;
    }

    public AViewContainer setOnCloseListener(function onClose){
        this.onClose=onClose;
        return this;
    }

    public AViewContainer setOnOpenListener(function onOpen){
        this.onOpen=onOpen;
        return this;
    }

    protected void openView(AView view){
        closeView();
        view.getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        container.addView(view.getView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.start();
        if(onOpen!=null) onOpen.run();
    }

    public void closeView(){
        container.removeAllViews();
        if(onClose!=null) onClose.run();
    }

    public boolean viewOpened(){
        return container.getChildCount()!=0;
    }

}
