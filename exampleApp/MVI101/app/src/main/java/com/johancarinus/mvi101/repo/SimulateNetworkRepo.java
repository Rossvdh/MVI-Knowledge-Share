package com.johancarinus.mvi101.repo;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class SimulateNetworkRepo {
    private final String TAG = this.getClass().getCanonicalName();

    public Observable<String> performNetworkCall(String echo) {
        if (Math.random() < 0.4) {
            Log.i(TAG, "network error occurred");
//            return Observable.error(new RuntimeException("network error")).delay(1, TimeUnit.SECONDS);
            throw new RuntimeException("network error");
        } else {
            Log.i(TAG, "successful network call");
            return Observable.just(echo).delay(1, TimeUnit.SECONDS);
        }
    }
}
