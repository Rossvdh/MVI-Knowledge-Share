package com.johancarinus.mvi101.repo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class SimulateNetworkRepo {

    private static final List<String> names = Arrays.asList(
            "Rogelio Flowers",
            "Timmy Stone",
            "Tricia Hodges",
            "Winifred Manning",
            "Harry Kelly",
            "Andrew Mckenzie",
            "Billie Ortega",
            "Charlene Stevens",
            "Robert Mendoza",
            "Veronica Bradley",
            "Rolando Ross",
            "Dallas Robbins",
            "Kathleen Walker",
            "Leslie Lee",
            "Jana Peters",
            "Max Wolfe",
            "Lucy Reese",
            "Lila Gordon",
            "Holly Hall",
            "Josh Luna",
            "Sonja Mccoy",
            "Helen Holmes",
            "Ruth Chandler",
            "Jay Gonzales",
            "Stewart Wright");


    public Observable<String> performNetworkCall(String echo) {
        return Observable.just(echo).delay(1, TimeUnit.SECONDS);
    }

    /**
     * Simulates a network call to get a list of random names.
     * @return List of 10 names
     */
    public static Observable<List<String>> getNamesList() {
        Collections.shuffle(names);

        return Observable.just(names.subList(0, 10)).delay(1, TimeUnit.SECONDS);
    }
}
