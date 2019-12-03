package com.amazon.sampleapp.climate;

import com.amazon.sampleapp.climate.bean.ClimateData;
import com.amazon.sampleapp.climate.bean.ClimateIntent;
import com.amazon.sampleapp.climate.bean.Modification;

import io.reactivex.Observable;

class ClimateIntentHandler {

    private final Modification<ClimateData> SKIP = data -> data;

    public Observable<Modification<ClimateData>> handleIntent(ClimateIntent intent) {
        Modification<ClimateData> mod = SKIP;
        String endpoint = intent.getEndpoint();
        String controller = intent.getController();
        if (intent instanceof ClimateIntent.Power) {
            ClimateIntent.Power powerIntent = (ClimateIntent.Power) intent;
            boolean value = powerIntent.getValue();
            mod = handlePowerController(endpoint, value);
        } else if (intent instanceof ClimateIntent.Toggle) {
            ClimateIntent.Toggle toggleIntent = (ClimateIntent.Toggle) intent;
            boolean value = toggleIntent.getValue();
            mod = handleToggleController(endpoint, controller, value);
        } else if (intent instanceof ClimateIntent.Range) {
            ClimateIntent.Range rangeIntent = (ClimateIntent.Range) intent;
            int value = rangeIntent.getValue();
            mod = handleRangeController(endpoint, controller, value);
        } else if (intent instanceof ClimateIntent.Mode) {
            ClimateIntent.Mode modeIntent = (ClimateIntent.Mode) intent;
            String value = modeIntent.getValue();
            boolean isOn = modeIntent.isOn();
            mod = handleModeController(endpoint, controller, value, isOn);
        }
        return Observable.just(mod);
    }

    private Modification<ClimateData> handlePowerController(String endpoint, boolean value) {
        Modification<ClimateData> mod = SKIP;
        if ("ac".equals(endpoint)) {
            mod = value
                    ? data -> data.cloneToBuilder()
                    .modifyMode(builder -> builder.setAcOn(true))
                    .create()
                    : data -> data.cloneToBuilder()
                    .modifyMode(builder -> builder.setAcOn(false).setAuto(false).setDual(false).setAcMax(false))
                    .create();
        }
        return mod;
    }

    private Modification<ClimateData> handleToggleController(String endpoint, String controller, boolean value) {
        Modification<ClimateData> mod = SKIP;
        if ("recirculate".equals(controller)) {
            if ("car".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyToggle(builder -> builder.setRecirculation(value))
                        .create();
            }
        } else if ("defroster".equals(controller)) {
            if ("front.windshield".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyToggle(builder -> builder.setDefrostFront(value))
                        .create();
            } else if ("rear.windshield".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyToggle(builder -> builder.setDefrostRear(value))
                        .create();
            }
        }
        return mod;
    }

    private Modification<ClimateData> handleRangeController(String endpoint, String controller, int value) {
        Modification<ClimateData> mod = SKIP;
        if ("temperature".equals(controller)) {
            if (value < 60 || value > 90) {
                return mod;
            }
            if ("all.heater".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcMax(false))
                        .modifyTemperature(builder -> builder.setFirstRow(value))
                        .create();
            } else if ("driver.heater".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setDual(true).setAcMax(false))
                        .modifyTemperature(builder -> builder.setDriver(value))
                        .create();
            } else if ("passenger.heater".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setDual(true).setAcMax(false))
                        .modifyTemperature(builder -> builder.setPassenger(value))
                        .create();
            }
        } else if ("speed".equals(controller)) {
            if ("front.fan".equals(endpoint) || "all.fan".equals(endpoint)) {
                if (value < 0 || value > 10) {
                    return mod;
                }
                mod = data -> data.cloneToBuilder()
                        .modifyFanSpeed(builder -> builder.setFront(value))
                        .create();
            }
        }
        return mod;
    }

    private Modification<ClimateData> handleModeController(String endpoint, String controller, String value, boolean isOn) {
        Modification<ClimateData> mod = SKIP;
        if ("ac".equals(endpoint)) {
            if ("AUTOMATIC".equals(value)) {
                mod = isOn
                        ? data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setAuto(true).setAcMax(false))
                        .create()
                        : data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setAuto(false))
                        .create();
            } else if ("ECONOMY".equals(value)) {

            } else if ("MANUAL".equals(value)) {
                mod = data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setAuto(false))
                        .create();
            } else if ("HIGH".equals(value) || "MAXIMUM".equals(value)) {
                mod = isOn
                        ? data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setAuto(false).setDual(false).setAcMax(true))
                        .modifyFanSpeed(builder -> builder.setFront(10))
                        .modifyTemperature(builder -> builder.setFirstRow(60))
                        .create()
                        : data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setAcMax(false))
                        .create();
            } else if ("DUAL".equals(value)) {
                mod = isOn
                        ? data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setDual(true))
                        .create()
                        : data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setDual(false))
                        .modifyTemperature(builder -> builder.setFirstRow(data.temperature.driver))
                        .create();
            }
        }
        return mod;
    }
}
