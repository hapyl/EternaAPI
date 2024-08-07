package me.hapyl.eterna.builtin.updater;

public enum UpdateResult {

    INVALID("&cCould not check for update! Try again later."),
    UP_TO_DATE("&aYou are using the latest version!"),
    DEVELOPMENT("&aYou are using a development version!"),
    OUTDATED("&eThere is a new version available!");

    private final String message;

    UpdateResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
