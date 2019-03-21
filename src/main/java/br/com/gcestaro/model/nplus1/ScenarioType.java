package br.com.gcestaro.model.nplus1;

public enum ScenarioType {
    EAGER(Scenarios.EAGER), LAZY(Scenarios.LAZY);

    String value;

    ScenarioType(String value) {
        if (!this.name().equals(value)) {
            throw new IllegalArgumentException("Value should be equal to name");
        }
        this.value = value;
    }

    public static class Scenarios {
        public static final String LAZY = "LAZY";
        public static final String EAGER = "EAGER";
    }
}
