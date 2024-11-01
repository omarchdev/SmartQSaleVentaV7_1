package com.omarchdev.smartqsale.smartqsaleventas.Model;

/**
 * Created by OMAR CHH on 26/02/2018.
 */


import android.app.Activity;
import android.os.Handler;

//import mbanje.kurt.fabbutton.FabButton;

/**
 * Created by kurt on 08 06 2015 .
 */
public class ProgressHelper {


    private int currentProgress = 0;
    private Handler handle=new Handler();
//    private final FabButton button;
    private final Activity activity;

    public ProgressHelper( Activity activity) {
     //   this.button = button;
        this.activity = activity;

    }

    private Runnable getRunnable(final Activity activity){
        return new Runnable() {
            @Override
            public void run() {
                currentProgress += 1;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         if(currentProgress <= 100){
                            handle.postDelayed(getRunnable(activity),50);
                        }
                    }
                });
            }
        };
    }

    public void startIndeterminate(){
    }

    public void complete(){

    }

    public void startDeterminate() {

         currentProgress = 0;
         getRunnable(activity).run();

    }

    public void Reset(){

    }


}