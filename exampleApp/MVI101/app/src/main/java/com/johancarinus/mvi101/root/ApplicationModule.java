package com.johancarinus.mvi101.root;

import android.app.Application;

import com.johancarinus.mvi101.collection.MainActivityModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    // Provide things here
    @Provides
    @Singleton
    public MainActivityModel getMainActivityModel() {
        return new MainActivityModel();
    }
}
