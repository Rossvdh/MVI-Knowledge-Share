package com.johancarinus.mvi101.collection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding3.view.RxView;
import com.johancarinus.mvi101.R;
import com.johancarinus.mvi101.models.ListItemData;
import com.johancarinus.mvi101.root.Mvi101Application;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import kotlin.Unit;

public class MainActivity extends AppCompatActivity {

    @Inject
    MainActivityModel mainActivityModel;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.new_button)
    Button newButton;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private PublishSubject<Boolean> reloadIntent;
    private CompositeDisposable compositeDisposable;
    private RecyclerView.LayoutManager layoutManager;
    private ListItemAdapter listItemAdapter;

    public Observable<String> addItemIntent() {
        return RxView.clicks(newButton).flatMap(new Function<Unit, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Unit unit) throws Exception {
                return Observable.just("Some text ID: " + Math.random() * 100);
            }
        });
    }

    public Observable<Integer> removeItemIntent() {
        return listItemAdapter.removeItemIntents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Mvi101Application) this.getApplicationContext()).getAppComponent().inject(this);
        configureUi();
        bindState();
        reloadIntent = PublishSubject.create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityModel.unbindState();
    }

    private void configureUi() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listItemAdapter = new ListItemAdapter(new ArrayList<ListItemData>());
        recyclerView.setAdapter(listItemAdapter);
    }

    private void bindState() {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                mainActivityModel.bindState(this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<MainActivityState>() {
                            @Override
                            public void accept(MainActivityState mainActivityState) {
                                MainActivity.this.render(mainActivityState);
                            }
                        }).subscribe()
        );
    }

    private void render(MainActivityState state) {
        if (state instanceof MainActivityState.LoadingState) {
            showLoading(true);
        } else if (state instanceof MainActivityState.HasResultState) {
            showResults(state.getListItems());
        } else if (state instanceof MainActivityState.ErrorState) {
            // TODO: Render error state
        }
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            newButton.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            newButton.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showResults(List<ListItemData> data) {
        showLoading(false);
        listItemAdapter.update(data);
    }
}
