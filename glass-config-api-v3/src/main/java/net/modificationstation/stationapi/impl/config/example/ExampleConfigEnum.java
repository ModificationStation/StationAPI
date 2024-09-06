package net.modificationstation.stationapi.impl.config.example;

public enum ExampleConfigEnum {
    YAY("Yay!"),
    NAY("Nay."),
    WHY("WHYY?!?!");

    final String stringValue;

    ExampleConfigEnum(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
