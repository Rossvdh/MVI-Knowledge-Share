package com.johancarinus.mvi101.collection;

import androidx.annotation.Nullable;

import com.johancarinus.mvi101.models.ListItemData;

import java.util.List;

public interface MainActivityState {

    @Nullable
    boolean isLoading();

    @Nullable
    List<ListItemData> getListItems();

    @Nullable
    Throwable getError();

    abstract class MainActivityStateBase implements MainActivityState {

        @Override
        public boolean isLoading() {
            return false;
        }

        @Override
        public List<ListItemData> getListItems() {
            return null;
        }

        @Override
        public Throwable getError() {
            return null;
        }
    }

    class InitialState extends MainActivityStateBase {
    }

    class LoadingState extends MainActivityStateBase {
        @Override
        public boolean isLoading() {
            return true;
        }
    }

    class HasResultState extends MainActivityStateBase {

        private List<ListItemData> results;

        public HasResultState(List<ListItemData> results) {
            this.results = results;
        }

        @Override
        public List<ListItemData> getListItems() {
            return results;
        }
    }

    class ErrorState extends MainActivityStateBase {

        private Throwable error;

        public ErrorState(Throwable error) {
            this.error = error;
        }

        @Override
        public Throwable getError() {
            return error;
        }
    }
}
