package com.amazon.sampleapp.climate.bean;

public class Toggle {
    public final boolean isRecirculation;
    public final boolean isDefrostFront;
    public final boolean isDefrostRear;

    public static Builder builder() {
        return new Builder();
    }

    private Toggle(Builder builder) {
        this.isRecirculation = builder.isRecirculation;
        this.isDefrostFront = builder.isDefrostFront;
        this.isDefrostRear = builder.isDefrostRear;
    }

    public Builder cloneToBuilder() {
        return builder()
                .setRecirculation(this.isRecirculation)
                .setDefrostRear(this.isDefrostRear)
                .setDefrostFront(this.isDefrostFront);
    }

    public static final class Builder {
        private boolean isRecirculation = false;
        private boolean isDefrostFront = false;
        private boolean isDefrostRear = false;

        private Builder() {
        }

        public Toggle create() {
            return new Toggle(this);
        }

        public Builder setRecirculation(boolean isRecirculation) {
            this.isRecirculation = isRecirculation;
            return this;
        }

        public Builder setDefrostFront(boolean isDefrostFront) {
            this.isDefrostFront = isDefrostFront;
            return this;
        }

        public Builder setDefrostRear(boolean isDefrostRear) {
            this.isDefrostRear = isDefrostRear;
            return this;
        }
    }
}
