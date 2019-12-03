package com.amazon.sampleapp.climate;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.amazon.sampleapp.GsonUtils;
import com.amazon.sampleapp.climate.bean.ClimateData;
import com.amazon.sampleapp.climate.bean.ClimateIntent;
import com.amazon.sampleapp.climate.bean.Modification;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ClimateDataCell {
    private final ClimateDataLocalSource mLocalSource;
    private final ClimateIntentHandler mIntentHandler;
    private final MutableLiveData<ClimateData> mClimateData = new MutableLiveData<>();
    private final Subject<ClimateData> mStatePublisher;
    private final Subject<ClimateIntent> mIntentPublisher;
    private final CompositeDisposable mDisposables;

    public ClimateDataCell(ClimateDataLocalSource localSource) {
        this.mLocalSource = localSource;
        this.mIntentHandler = new ClimateIntentHandler();
        this.mStatePublisher = BehaviorSubject.create();
        this.mIntentPublisher = PublishSubject.create();
        this.mDisposables = new CompositeDisposable();
    }

    public LiveData<ClimateData> getClimateData() {
        return mClimateData;
    }

    public void sendIntent(ClimateIntent intent) {
        mIntentPublisher.onNext(intent);
    }

    public void start() {
        Scheduler scheduler = createSingleScheduler();
        Callable<ClimateData> initialState = mLocalSource::readClimateData;

        ConnectableObservable<Modification<ClimateData>> connectable = mIntentPublisher
                .doOnSubscribe(mDisposables::add)
                .observeOn(scheduler)
                .concatMapEager(this::handleIntent)
                .observeOn(scheduler)
                .publish();

        connectable
                .doOnSubscribe(mDisposables::add)
                .scanWith(initialState, (oldState, reducer) -> reducer.update(oldState))
                .distinctUntilChanged()
                .subscribe(mStatePublisher);

        connectable.connect();

        mDisposables.add(mStatePublisher.subscribe(mClimateData::postValue));
        mDisposables.add(mStatePublisher.subscribe(data -> Log.d("Raych", GsonUtils.toJsonString(data))));
        mDisposables.add(mStatePublisher.observeOn(Schedulers.io())
                .flatMapCompletable(mLocalSource::writeClimateDataRx, true).subscribe());
    }

    public void stop() {
        mDisposables.clear();
    }

    private ObservableSource<Modification<ClimateData>> handleIntent(ClimateIntent intent) {
        return mIntentHandler.handleIntent(intent);
    }

    private Scheduler createSingleScheduler() {
        return Schedulers.from(Executors.newSingleThreadExecutor(
                runnable -> new Thread(runnable, "ClimateDataCell")));
    }
}
