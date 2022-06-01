package ru.antiborov.abgaryanmusicbot.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Locale;

public enum SourceThumbnail {
    YOUTUBE("https://img.youtube.com/vi/%s/0.jpg");

    private final String pattern;

    SourceThumbnail(String pattern) {
        this.pattern = pattern;
    }

    public static String getThumbnail(AudioTrack track) {
        return getThumbnail(track.getSourceManager().getSourceName(), track.getIdentifier());
    }

    public static String getThumbnail(String provider, String identifier) {
        try {
            return String.format(valueOf(provider.toUpperCase(Locale.ROOT)).pattern, identifier);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
