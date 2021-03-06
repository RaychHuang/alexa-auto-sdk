package com.amazon.sampleapp.climate.bean;

public class Temperature {
    // Power
    public final boolean isOn;
    // Driver side temperature
    public final int driver;
    // Passenger side temperature
    public final int passenger;

    public static Builder builder() {
        return new Builder();
    }

    private Temperature(Builder builder) {
        this.isOn = builder.isOn;
        this.driver = builder.driver;
        this.passenger = builder.passenger;
    }

    public Builder cloneToBuilder() {
        return builder()
                .setOn(isOn)
                .setDriver(this.driver)
                .setPassenger(this.passenger);
    }

    public static final class Builder {
        private boolean isOn = false;
        private int driver = 70;
        private int passenger = 70;

        private Builder() {
        }

        public Temperature build() {
            return new Temperature(this);
        }

        public Builder setOn(boolean on) {
            this.isOn = on;
            return this;
        }

        public Builder setFirstRow(int temperature) {
            int value = range(temperature);
            this.driver = value;
            this.passenger = value;
            return this;
        }

        public Builder adjustFirstRow(int delta) {
            return setFirstRow(this.driver + delta);
        }

        public Builder setDriver(int temperature) {
            this.driver = range(temperature);
            return this;
        }

        public Builder adjustDriver(int delta) {
            return setDriver(this.driver + delta);
        }

        public Builder setPassenger(int temperature) {
            this.passenger = range(temperature);
            return this;
        }

        public Builder adjustPassenger(int delta) {
            return setPassenger(this.passenger + delta);
        }

        private int range(int value) {
            value = Math.max(value, 60);
            value = Math.min(value, 90);
            return value;
        }
    }
}
