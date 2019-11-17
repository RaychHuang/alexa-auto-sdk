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

        public FanSpeed create() {
            return new FanSpeed(this);
        }

        public Builder setOn(boolean on) {
            isOn = on;
            return this;
        }

        public Builder setFront(int intensity) {
            this.driver = intensity;
            this.passenger = intensity;
            return this;
        }

        public Builder setDriver(int intensity) {
            this.driver = intensity;
            return this;
        }

        public Builder setPassenger(int intensity) {
            this.passenger = intensity;
            return this;
        }
    }
}
