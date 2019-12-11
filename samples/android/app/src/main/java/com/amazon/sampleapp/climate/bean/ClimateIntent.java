package com.amazon.sampleapp.climate.bean;

public class ClimateIntent {
    private final String endpoint;
    private final String controller;

    public ClimateIntent(String endpoint, String controller) {
        this.endpoint = endpoint;
        this.controller = controller;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getController() {
        return controller;
    }

    public static Power createPowerIntent(String endpoint, boolean value) {
        return new Power(endpoint, value);
    }

    public static Toggle createToggleIntent(String endpoint, String controller, boolean value) {
        return new Toggle(endpoint, controller, value);
    }

    public static Range createSetRangeIntent(String endpoint, String controller, int value) {
        return new Range(endpoint, controller, value, false);
    }

    public static Range createAdjustRangeIntent(String endpoint, String controller, int value) {
        return new Range(endpoint, controller, value, true);
    }

    public static Mode createModeIntent(String endpoint, String controller, String value) {
        return createModeIntent(endpoint, controller, value, true);
    }

    public static Mode createModeIntent(String endpoint, String controller, String value, boolean isOn) {
        return new Mode(endpoint, controller, value, isOn);
    }

    public static class Power extends ClimateIntent {
        private final boolean value;

        public Power(String endpoint, boolean value) {
            super(endpoint, "");
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }
    }

    public static class Toggle extends ClimateIntent {
        private final boolean value;

        public Toggle(String endpoint, String controller, boolean value) {
            super(endpoint, controller);
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }
    }

    public static class Range extends ClimateIntent {
        private final int value;
        private final boolean isDelta;

        public Range(String endpoint, String controller, int value, boolean isDelta) {
            super(endpoint, controller);
            this.value = value;
            this.isDelta = isDelta;
        }

        public int getValue() {
            return value;
        }

        public boolean isDelta() {
            return isDelta;
        }
    }

    public static class Mode extends ClimateIntent {
        private final String value;
        private final boolean isOn;

        public Mode(String endpoint, String controller, String value, boolean isOn) {
            super(endpoint, controller);
            this.value = value;
            this.isOn = isOn;
        }

        public String getValue() {
            return value;
        }

        public boolean isOn() {
            return isOn;
        }
    }
}
