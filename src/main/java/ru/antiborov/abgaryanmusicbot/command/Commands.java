package ru.antiborov.abgaryanmusicbot.command;

public enum Commands {
    PLAY("play", "p"),
    QUEUE("queue", "q"),
    REPEAT("repeat", "r"),
    SKIP("skip", "s"),
    PAUSE("pause", null),
    STOP("stop", null);

    public final String fullName;
    public final String shortCut;

    Commands(String fullName, String shortCut) {
        this.fullName = fullName;
        this.shortCut = shortCut;
    }
}
