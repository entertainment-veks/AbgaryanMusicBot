package ru.antiborov.abgaryanmusicbot.domain.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;

@Getter
public class GuildMusicManager {
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;
    private final int sendHandlerByteBufferSize;

    public GuildMusicManager(AudioPlayerManager audioPlayerManager, int sendHandlerByteBufferSize) {
        this.audioPlayer = audioPlayerManager.createPlayer();
        this.trackScheduler = new TrackScheduler(audioPlayer);
        this.sendHandlerByteBufferSize = sendHandlerByteBufferSize;
        this.audioPlayer.addListener(this.trackScheduler);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(audioPlayer, sendHandlerByteBufferSize);
    }
}
