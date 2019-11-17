package com.amazon.sampleapp.climate;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.amazon.sampleapp.climate.bean.ClimateData;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class ClimateDataCell {
    private final MutableLiveData<ClimateData> mClimateData = new MutableLiveData<>();
    private final PublishSubject<Integer> temperatureDriver = PublishSubject.create();
    private final PublishSubject<Integer> temperaturePassenger = PublishSubject.create();

    public LiveData<ClimateData> getClimateData() {
        return mClimateData;
    }

    public Disposable observeClimateData(Observable<ClimateData> upstream) {
        return upstream.subscribe(mClimateData::postValue);
    }

    public void onTemperatureDriverClick(int diff) {
        temperatureDriver.onNext(diff);
    }

    public Observable<Integer> getTemperatureDriver() {
        return temperatureDriver;
    }

    public void onTemperaturePassengerClick(int diff) {
        temperaturePassenger.onNext(diff);
    }

    public Observable<Integer> getTemperaturePassenger() {
        return temperaturePassenger;
    }
}
