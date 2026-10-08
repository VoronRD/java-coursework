package ru.chsu.computer_devices.model;

public enum Rating {
    ONE(1, "★☆☆☆☆"),
    TWO(2, "★★☆☆☆"),
    THREE(3, "★★★☆☆"),
    FOUR(4, "★★★★☆"),
    FIVE(5, "★★★★★");

    private final int value;
    private final String stars;

    Rating(int value, String stars) {
        this.value = value;
        this.stars = stars;
    }

    public int getValue() {
        return value;
    }

    public String getStars() {
        return stars;
    }

    public static Rating fromValue(int value) {
        for (Rating r : values()) {
            if (r.value == value) {
                return r;
            }
        }
        return FIVE;
    }

    public int toValue() {
        return value;
    }
}