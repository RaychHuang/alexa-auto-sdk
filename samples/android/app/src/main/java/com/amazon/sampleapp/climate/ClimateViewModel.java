package com.amazon.sampleapp.climate;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import io.reactivex.disposables.CompositeDisposable;

public class ClimateViewModel extends AndroidViewModel implements LifecycleObserver {
    private final ClimateDataCell mCell;
    private final CompositeDisposable mDisposable;
    private final ClimateDataVoiceController mVoiceController;

    public ClimateViewModel(Application application) {
        this(application, ClimateDataVoiceController.getInstance());
    }

    ClimateViewModel(Application application, ClimateDataVoiceController voiceController) {
        super(application);
        this.mCell = new ClimateDataCell(new ClimateDataLocalSource(application.getApplicationContext()));
        this.mDisposable = new CompositeDisposable();
        this.mVoiceController = voiceController;
    }

    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        mCell.start();
        mDisposable.add(mVoiceController.getClimateIntent().subscribe(mCell::sendIntent));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        mCell.stop();
        mDisposable.clear();
    }

    public ClimateDataCell getClimateDataCell() {
        return mCell;
    }
}
