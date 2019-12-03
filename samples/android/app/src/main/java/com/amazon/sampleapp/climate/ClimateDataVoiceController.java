package com.amazon.sampleapp.climate;

import com.amazon.sampleapp.climate.bean.ClimateIntent;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ClimateDataVoiceController {
    private static ClimateDataVoiceController INSTANCE;
    private final Subject<ClimateIntent> mIntent = PublishSubject.create();

    public static ClimateDataVoiceController getInstance() {
        if (INSTANCE == null) {
            synchronized (ClimateDataVoiceController.class) {
                ClimateDataVoiceController instance = INSTANCE;
                if (instance == null) {
                    instance = new ClimateDataVoiceController();
                }
                INSTANCE = instance;
            }
        }
        return INSTANCE;
    }

    ClimateDataVoiceController() {
    }

    public Subject<ClimateIntent> getClimateIntent() {
        return mIntent;
    }

    public void handlePowerController(String endpoint, final boolean isOn) {
        mIntent.onNext(ClimateIntent.createPowerIntent(endpoint, isOn));
    }

    public void handleToggleController(String endpoint, String controller, final boolean isOn) {
        mIntent.onNext(ClimateIntent.createToggleIntent(endpoint, controller, isOn));
    }

    public void handleRangeController(String endpoint, String controller, final double value) {
        final int intValue = (int) Math.round(value);
        mIntent.onNext(ClimateIntent.createRangeIntent(endpoint, controller, intValue));
    }

    public void handleModeController(String endpoint, String controller, final String value) {
        mIntent.onNext(ClimateIntent.createModeIntent(endpoint, controller, value));
    }
}
