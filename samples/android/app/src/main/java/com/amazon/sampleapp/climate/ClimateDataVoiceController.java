package com.amazon.sampleapp.climate;

import com.amazon.sampleapp.climate.bean.ClimateData;
import com.amazon.sampleapp.climate.bean.Modification;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ClimateDataVoiceController implements ClimateDataController {
    private final Subject<Modification<ClimateData>> mRequest = PublishSubject.create();

    ClimateDataVoiceController() {
    }

    @Override
    public Observable<Modification<ClimateData>> getModificationRequest() {
        return mRequest;
    }

    public void handlePowerController(String endpoint, final boolean isOn) {

    }

    public void handleToggleController(String endpoint, String controller, final boolean isOn) {

    }

    public void handleRangeController(String endpoint, String controller, final double value) {
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
                        .modifyTemperature(builder -> builder.setDriver(intValue))
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

    public void handleModeController(String endpoint, String controller, final String value) {
        if ("ac".equals(endpoint)) {
            if ("AUTOMATIC".equals(value)) {
                mRequest.onNext(data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAuto(true).setAcMax(false))
                        .create());
            } else if ("ECONOMY".equals(value)) {

            } else if ("MANUAL".equals(value)) {
                mRequest.onNext(data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAuto(false).setAcMax(false))
                        .create());
            } else if ("HIGH".equals(value) || "MAXIMUM".equals(value)) {
                mRequest.onNext(data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAuto(false).setDual(false).setAcMax(true))
                        .modifyFanSpeed(builder -> builder.setFront(10))
                        .modifyTemperature(builder -> builder.setFirstRow(60))
                        .create());
            }
        }
    }
}
