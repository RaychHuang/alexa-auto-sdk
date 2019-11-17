package com.amazon.sampleapp.climate;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public class ClimateViewModel extends ViewModel implements LifecycleObserver {
    private final ClimateDataCell mCell;
    private final ClimateDataManager mDataManager;
    private final ClimateDataTouchController mTouchController;

    private CompositeDisposable mDisposable;

    public ClimateViewModel() {
        this(ClimateDataManager.getInstance());
    }

    ClimateViewModel(ClimateDataManager manager) {
        this.mCell = new ClimateDataCell();
        this.mDataManager = manager;
        this.mTouchController = manager.getTouchController();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        mDisposable = new CompositeDisposable();
        mDisposable.add(mCell.observeClimateData(mDataManager.getClimateData()));
        mDisposable.add(mTouchController.handleTemperatureDriverClick(mCell.getTemperatureDriver()));
        mDisposable.add(mTouchController.handleTemperaturePassengerClick(mCell.getTemperaturePassenger()));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    public ClimateDataCell getClimateDataCell() {
        return mCell;
    }
}
