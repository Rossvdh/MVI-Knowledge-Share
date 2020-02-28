package com.johancarinus.mvi101.root;

import com.johancarinus.mvi101.collection.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class})
public interface AppComponent {

    void inject(MainActivity target);
}
