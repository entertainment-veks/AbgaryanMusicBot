package ru.antiborov.abgaryanmusicbot.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Locale;

public enum SourceColors {
    YOUTUBE(0xFF0000),
    SOUNDCLOUD(0xF26F23),
    TWITCH(0x6441a5),
    OTHER(0x18233d);

    private final int color;

    SourceColors(int color) {
        this.color = color;
    }

    public static int determineColor(AudioTrack track) {
        if (track == null) {
            return 0x000000;
        }
        return determineColor(track.getSourceManager().getSourceName());
    }

    public static int determineColor(String sourceName) {
        return valueOf(sourceName.toUpperCase(Locale.ROOT)).color;
    }

    public int getColor() {
        return color;
    }
}
