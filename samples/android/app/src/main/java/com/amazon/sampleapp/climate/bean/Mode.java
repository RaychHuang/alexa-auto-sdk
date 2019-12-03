package com.amazon.sampleapp.climate.bean;

public class Mode {
    public final boolean isAcOn;
    public final boolean isAuto;
    public final boolean isDual;
    public final boolean isAcMax;
    public final boolean isEconomic;

    public static Builder builder() {
        return new Builder();
    }

    private Mode(Builder builder) {
        this.isAcOn = builder.isAcOn;
        this.isAuto = builder.isAuto;
        this.isDual = builder.isDual;
        this.isAcMax = builder.isAcMax;
        this.isEconomic = builder.isEconomic;
    }

    public Builder cloneToBuilder() {
        return builder()
                .setAcOn(this.isAcOn)
                .setAuto(this.isAuto)
                .setDual(this.isDual)
                .setAcMax(this.isAcMax)
                .setEconomic(this.isEconomic);
    }

    public static final class Builder {
        private boolean isAcOn = false;
        private boolean isAuto = false;
        private boolean isDual = false;
        private boolean isAcMax = false;
        private boolean isEconomic = false;

        private Builder() {
        }

        public Mode create() {
            return new Mode(this);
        }

        public Builder setAcOn(boolean isAcOn) {
            this.isAcOn = isAcOn;
            return this;
        }

        public Builder setAuto(boolean isAuto) {
            this.isAuto = isAuto;
            return this;
        }

        public Builder setDual(boolean isDual) {
            this.isDual = isDual;
            return this;
        }

        public Builder setAcMax(boolean isAcMax) {
            this.isAcMax = isAcMax;
            return this;
        }

        public Builder setEconomic(boolean economic) {
            isEconomic = economic;
            return this;
        }
    }
}
