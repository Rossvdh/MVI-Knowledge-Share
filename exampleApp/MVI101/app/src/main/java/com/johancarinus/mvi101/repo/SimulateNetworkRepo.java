package com.johancarinus.mvi101.repo;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class SimulateNetworkRepo {

    public Observable<String> performNetworkCall(String echo) {
        return Observable.just(echo).delay(1, TimeUnit.SECONDS);
    }
}
