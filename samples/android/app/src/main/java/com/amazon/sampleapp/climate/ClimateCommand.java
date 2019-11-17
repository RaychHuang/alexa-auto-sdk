package com.amazon.sampleapp.climate;

public class ClimateCommand {
    public enum Entity {
        POWER_CONTROLLER, TOGGLE_CONTROLLER, RANGE_CONTROLLER, MODE_CONTROLLER
    }

    public final Entity entity;
    public final String endpoint;
    public final String controller;
    public final String action;
    public final Object value;

    private ClimateCommand(Builder builder) {
        this.entity = builder.entity;
        this.endpoint = builder.endpoint;
        this.controller = builder.controller;
        this.action = builder.action;
        this.value = builder.value;
    }

    public static Builder builder() {
        return new Builder();
    }

    private static class Builder {
        private Entity entity;
        private String endpoint;
        private String controller;
        private String action;
        private Object value;

        public Builder setEntity(Entity entity) {
            this.entity = entity;
            return this;
        }

        public Builder setEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder setController(String controller) {
            this.controller = controller;
            return this;
        }

        public Builder setAction(String action) {
            this.action = action;
            return this;
        }

        public Builder setValue(Object value) {
            this.value = value;
            return this;
        }

        public ClimateCommand create() {
            return new ClimateCommand(this);
        }
    }
}
