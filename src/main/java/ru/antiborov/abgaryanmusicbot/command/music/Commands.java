package ru.antiborov.abgaryanmusicbot.command.music;

public enum Commands {
    PLAY("play", "p"),
    QUEUE("queue", "q"),
    SKIP("skip", "s"),
    PAUSE("pause", ""),
    STOP("stop", "");

    public final String fullName;
    public final String shorCut;

    Commands(String fullName, String shortCut) {
        this.fullName = fullName;
        this.shorCut = shortCut;
    }
}
