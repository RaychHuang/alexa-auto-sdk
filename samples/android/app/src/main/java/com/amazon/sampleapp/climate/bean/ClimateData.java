package com.amazon.sampleapp.climate.bean;

public class ClimateData {
    public final Mode mode;
    public final Temperature temperature;
    public final FanSpeed fanSpeed;

    private ClimateData(Builder builder) {
        this.mode = builder.mode.create();
        this.fanSpeed = builder.fanSpeed.create();
        this.temperature = builder.temperature.create();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ClimateData createDefault() {
        return builder().create();
    }

    public Builder cloneToBuilder() {
        return builder()
                .setMode(this.mode.cloneToBuilder())
                .setFanSpeed(this.fanSpeed.cloneToBuilder())
                .setTemperature(this.temperature.cloneToBuilder());
    }

    public static final class Builder {
        private Mode.Builder mode;
        private FanSpeed.Builder fanSpeed;
        private Temperature.Builder temperature;

        public ClimateData create() {
            if (mode == null) {
                mode = Mode.builder();
            }
            if (fanSpeed == null) {
                fanSpeed = FanSpeed.builder();
            }
            if (temperature == null) {
                temperature = Temperature.builder();
            }
            return new ClimateData(this);
        }

        public Builder setMode(Mode.Builder mode) {
            this.mode = mode;
            return this;
        }

        public Builder modifyMode(Modification<Mode.Builder> modification) {
            this.mode = modification.update(this.mode);
            return this;
        }

        public Builder setFanSpeed(FanSpeed.Builder fanSpeed) {
            this.fanSpeed = fanSpeed;
            return this;
        }

        public Builder modifyFanSpeed(Modification<FanSpeed.Builder> modification) {
            this.fanSpeed = modification.update(this.fanSpeed);
            return this;
        }

        public Builder setTemperature(Temperature.Builder temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder modifyTemperature(Modification<Temperature.Builder> modification) {
            this.temperature = modification.update(this.temperature);
            return this;
        }
    }
}
