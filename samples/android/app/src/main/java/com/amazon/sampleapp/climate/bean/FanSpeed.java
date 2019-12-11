package com.amazon.sampleapp.climate.bean;

public class FanSpeed {
    // Power
    public final boolean isOn;
    // Driver fan intensity
    public final int driver;
    // Passenger fan intensity
    public final int passenger;

    public static Builder builder() {
        return new Builder();
    }

    private FanSpeed(Builder builder) {
        this.isOn = builder.isOn;
        this.driver = builder.driver;
        this.passenger = builder.passenger;
    }

    public Builder cloneToBuilder() {
        return builder()
                .setOn(this.isOn)
                .setDriver(this.driver)
                .setPassenger(this.passenger);
    }

    public static final class Builder {
        private boolean isOn = false;
        private int driver = 1;
        private int passenger = 1;

        private Builder() {
        }

        public FanSpeed build() {
            return new FanSpeed(this);
        }

        public Builder setOn(boolean on) {
            isOn = on;
            return this;
        }

        public Builder setFront(int intensity) {
            int value = range(intensity);
            this.driver = value;
            this.passenger = value;
            return this;
        }

        public Builder adjustFront(int delta) {
            return setFront(this.driver + delta);
        }

        public Builder setDriver(int intensity) {
            this.driver = intensity;
            return this;
        }

        public Builder setPassenger(int intensity) {
            this.passenger = intensity;
            return this;
        }

        private int range(int value) {
            value = Math.max(value, 0);
            value = Math.min(value, 10);
            return value;
        }
    }
}
