package com.amazon.sampleapp.climate.bean;

public class ClimateData {
    public final Mode mode;
    public final Toggle toggle;
    public final FanSpeed fanSpeed;
    public final Temperature temperature;

    private ClimateData(Builder builder) {
        this.mode = builder.mode.build();
        this.toggle = builder.toggle.build();
        this.fanSpeed = builder.fanSpeed.build();
        this.temperature = builder.temperature.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ClimateData createDefault() {
        return builder().build();
    }

    public Builder cloneToBuilder() {
        return builder()
                .setMode(this.mode.cloneToBuilder())
                .setToggle(this.toggle.cloneToBuilder())
                .setFanSpeed(this.fanSpeed.cloneToBuilder())
                .setTemperature(this.temperature.cloneToBuilder());
    }

    public static final class Builder {
        private Mode.Builder mode;
        private Toggle.Builder toggle;
        private FanSpeed.Builder fanSpeed;
        private Temperature.Builder temperature;

        public ClimateData build() {
            if (mode == null) {
                mode = Mode.builder();
            }
            if (toggle == null) {
                toggle = Toggle.builder();
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

        public Builder setToggle(Toggle.Builder toggle) {
            this.toggle = toggle;
            return this;
        }

        public Builder modifyToggle(Modification<Toggle.Builder> modification) {
            this.toggle = modification.update(this.toggle);
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
