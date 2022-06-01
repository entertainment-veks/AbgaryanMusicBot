package ru.antiborov.abgaryanmusicbot.domain.music;

/**
 * Remote Dependant prefixes for search in {@link com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers}
 */
public enum RemotePrefixes {
    YOUTUBE("ytsearch"),
    SOUNDCLOUD("scsearch");

    public String prefix;

    RemotePrefixes(String searchPrefix) {
        this.prefix = searchPrefix + ":";
    }
}
