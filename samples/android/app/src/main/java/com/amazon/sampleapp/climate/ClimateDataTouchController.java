package com.amazon.sampleapp.climate;

import com.amazon.sampleapp.climate.bean.ClimateData;
import com.amazon.sampleapp.climate.bean.Modification;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ClimateDataTouchController implements ClimateDataController {
    private final Subject<Modification<ClimateData>> mRequest = PublishSubject.create();

    ClimateDataTouchController() {
    }

    public Observable<Modification<ClimateData>> getModificationRequest() {
        return mRequest;
    }

    public Disposable handleTemperatureDriverClick(Observable<Integer> intent) {
        Disposable[] disposables = {null};
        intent.map((Function<Integer, Modification<ClimateData>>) diff -> data -> data.cloneToBuilder()
                .modifyTemperature(builder -> builder.setDriver(data.temperature.driver + diff))
                .create())
                .doOnSubscribe(disposable -> disposables[0] = disposable)
                .subscribe(mRequest);
        return disposables[0];
    }

    public Disposable handleTemperaturePassengerClick(Observable<Integer> intent) {
        Disposable[] disposables = {null};
        intent.map((Function<Integer, Modification<ClimateData>>) diff -> data -> data.cloneToBuilder()
                .modifyTemperature(builder -> builder.setPassenger(data.temperature.passenger + diff))
                .create())
                .doOnSubscribe(disposable -> disposables[0] = disposable)
                .subscribe(mRequest);
        return disposables[0];
    }

    public void handlePowerController(String endpoint, boolean isOn) {

    }

    public void handleToggleController(String endpoint, String controller, boolean isOn) {

    }

    public void handleRangeController(String endpoint, String controller, double value) {
        final int intValue = (int) Math.round(value);
        if ("temperature".equals(controller)) {
            if ("all.heater".equals(endpoint)) {
                mRequest.onNext(data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setDual(false))
                        .modifyTemperature(builder -> builder.setFirstRow(intValue))
                        .create());
            } else if ("driver.heater".equals(endpoint)) {
                mRequest.onNext(data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setDual(true))
                        .modifyTemperature(builder -> builder.setDriver(data.temperature.driver + intValue))
                        .create());
            } else if ("passenger.heater".equals(endpoint)) {
                mRequest.onNext(data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setDual(true))
                        .modifyTemperature(builder -> builder.setPassenger(intValue))
                        .create());
            }
        } else if ("speed".equals(controller)) {
            if ("front.fan".equals(endpoint) || "all.fan".equals(endpoint)) {
                mRequest.onNext(data -> data.cloneToBuilder()
                        .modifyFanSpeed(builder -> builder.setFront(intValue))
                        .create());
            }
        }
    }

    public void handleModeController(String endpoint, String controller, String value) {

    }
}
