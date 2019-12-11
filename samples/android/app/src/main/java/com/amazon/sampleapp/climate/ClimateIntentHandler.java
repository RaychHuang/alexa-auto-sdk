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
            boolean isDelta = rangeIntent.isDelta();
            mod = handleRangeController(endpoint, controller, value, isDelta);
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
                    .build()
                    : data -> data.cloneToBuilder()
                    .modifyMode(builder -> builder.setAcOn(false).setAuto(false).setDual(false).setAcMax(false))
                    .build();
        }
        return mod;
    }

    private Modification<ClimateData> handleToggleController(String endpoint, String controller, boolean value) {
        Modification<ClimateData> mod = SKIP;
        if ("recirculate".equals(controller)) {
            if ("car".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyToggle(builder -> builder.setRecirculation(value))
                        .build();
            }
        } else if ("defroster".equals(controller)) {
            if ("front.windshield".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyToggle(builder -> builder.setDefrostFront(value))
                        .build();
            } else if ("rear.windshield".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyToggle(builder -> builder.setDefrostRear(value))
                        .build();
            }
        }
        return mod;
    }

    private Modification<ClimateData> handleRangeController(String endpoint, String controller, int value, boolean isDelta) {
        Modification<ClimateData> mod = SKIP;
        if ("temperature".equals(controller)) {
            if ("all.heater".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setDual(false).setAcMax(false))
                        .modifyTemperature(builder -> isDelta ? builder.adjustFirstRow(value)
                                : builder.setFirstRow(value))
                        .build();
            } else if ("driver.heater".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setDual(true).setAcMax(false))
                        .modifyTemperature(builder -> isDelta ? builder.adjustDriver(value)
                                : builder.setDriver(value))
                        .build();
            } else if ("passenger.heater".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setDual(true).setAcMax(false))
                        .modifyTemperature(builder -> isDelta ? builder.adjustPassenger(value)
                                : builder.setPassenger(value))
                        .build();
            }
        } else if ("speed".equals(controller)) {
            if ("front.fan".equals(endpoint) || "all.fan".equals(endpoint)) {
                mod = data -> data.cloneToBuilder()
                        .modifyFanSpeed(builder -> isDelta ? builder.adjustFront(value)
                                : builder.setFront(value))
                        .build();
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
                        .build()
                        : data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setAuto(false))
                        .build();
            } else if ("ECONOMY".equals(value)) {

            } else if ("MANUAL".equals(value)) {
                mod = data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setAuto(false))
                        .build();
            } else if ("HIGH".equals(value) || "MAXIMUM".equals(value)) {
                mod = isOn
                        ? data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setAuto(false).setDual(false).setAcMax(true))
                        .modifyFanSpeed(builder -> builder.setFront(10))
                        .modifyTemperature(builder -> builder.setFirstRow(60))
                        .build()
                        : data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setAcMax(false))
                        .build();
            } else if ("DUAL".equals(value)) {
                mod = isOn
                        ? data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setDual(true))
                        .build()
                        : data -> data.cloneToBuilder()
                        .modifyMode(builder -> builder.setAcOn(true).setDual(false))
                        .modifyTemperature(builder -> builder.setFirstRow(data.temperature.driver))
                        .build();
            }
        }
        return mod;
    }
}
