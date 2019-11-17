package com.amazon.sampleapp.climate;

import com.amazon.sampleapp.climate.bean.ClimateData;
import com.amazon.sampleapp.climate.bean.Modification;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ClimateDataManager {
    private static ClimateDataManager INSTANCE;
    private final ClimateDataLocalSource mLocalSource;
    private final ClimateDataTouchController mTouchController;
    private final ClimateDataVoiceController mVoiceController;

    public static ClimateDataManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ClimateDataManager.class) {
                ClimateDataManager instance = INSTANCE;
                if (instance == null) {
                    instance = new ClimateDataManager(new ClimateDataLocalSource(com.amazon.sampleapp.SampleApplication.getInstance())
                            , new ClimateDataTouchController(), new ClimateDataVoiceController());
                }
                INSTANCE = instance;
            }
        }
        return INSTANCE;
    }

    ClimateDataManager(ClimateDataLocalSource localSource,
                       ClimateDataTouchController touchController,
                       ClimateDataVoiceController voiceController) {
        this.mLocalSource = localSource;
        this.mTouchController = touchController;
        this.mVoiceController = voiceController;
    }

    public ClimateDataTouchController getTouchController() {
        return mTouchController;
    }

    public ClimateDataVoiceController getVoiceController() {
        return mVoiceController;
    }

    public Observable<ClimateData> getClimateData() {
        // Load local data as initial value.
        Callable<ClimateData> initialValue = mLocalSource::readClimateData;

        Observable<Modification<ClimateData>> requests = Observable
                .merge(mVoiceController.getModificationRequest(),
                        mTouchController.getModificationRequest())
                .subscribeOn(Schedulers.io());

        // Update climate data by voice commands.
        Observable<ClimateData> climateData = requests
                .scanWith(initialValue, (oldData, modification) -> modification.update(oldData))
                .publish()
                .autoConnect(2);

        // Update Local Settings each time we have new data.
        climateData.observeOn(Schedulers.io())
                .flatMapCompletable(mLocalSource::writeClimateDataRx, true)
                .subscribe();

        return climateData;
    }
}
