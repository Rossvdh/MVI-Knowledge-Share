package com.johancarinus.mvi101.collection;

import com.johancarinus.mvi101.models.ListItemData;
import com.johancarinus.mvi101.repo.SimulateNetworkRepo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class MainActivityModel {

    private BehaviorSubject<MainActivityState> currentState;
    private MainActivityState currentStateValue;

    private CompositeDisposable compositeDisposable;

    private SimulateNetworkRepo simulateNetworkRepo;
    private List<ListItemData> listItemData;

    public MainActivityModel() {
        currentState = BehaviorSubject.create();
        listItemData = new ArrayList<>();
        currentStateValue = new MainActivityState.HasResultState(listItemData);
        currentState.onNext(currentStateValue);
        compositeDisposable = new CompositeDisposable();
        simulateNetworkRepo = new SimulateNetworkRepo();
    }

    public Observable<MainActivityState> bindState(MainActivity mainActivity) {
        bindIntents(mainActivity);
        return currentState;
    }

    public void unbindState() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }

    private void bindIntents(MainActivity mainActivity) {
        compositeDisposable.add(
                mainActivity.addItemIntent()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                reduceStateWithAdd(s);
                            }
                        }).subscribe()
        );
        compositeDisposable.add(
                mainActivity.removeItemIntent()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            reduceStateWithRemove(integer.intValue());
                        }
                    }).subscribe()
        );
    }

    private void reduceStateWithAdd(String s) {
        if (!(currentStateValue instanceof MainActivityState.LoadingState)) {
            currentStateValue = new MainActivityState.LoadingState();
            currentState.onNext(currentStateValue);
            compositeDisposable.add(
                    simulateNetworkRepo.performNetworkCall(s)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                reduceStateWithLoadingComplete(s);
                            }
                        }).subscribe()
            );
        }
    }

    private void reduceStateWithRemove(int index) {
        if(!(currentStateValue instanceof MainActivityState.LoadingState)) {
            listItemData.remove(index);
            currentStateValue = new MainActivityState.HasResultState(listItemData);
            currentState.onNext(currentStateValue);
        }
    }

    private void reduceStateWithLoadingComplete(String s) {
        if (currentStateValue instanceof MainActivityState.LoadingState) {
            listItemData.add(new ListItemData(s));
            currentStateValue = new MainActivityState.HasResultState(listItemData);
            currentState.onNext(currentStateValue);
        }
    }
}
