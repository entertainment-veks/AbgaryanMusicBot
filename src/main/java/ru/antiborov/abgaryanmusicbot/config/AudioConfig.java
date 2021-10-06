package ru.antiborov.abgaryanmusicbot.config;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;

@Configuration
public class AudioConfig {

    @Bean
    public AudioPlayerManager audioPlayerManager() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        //        Methods for custom configuration
        //        playerManager.setFrameBufferDuration();
        //        playerManager.setHttpBuilderConfigurator();
        //        playerManager.setHttpRequestConfigurator();
        //        playerManager.setPlayerCleanupThreshold();
        //        playerManager.setItemLoaderThreadPoolSize();
        //        playerManager.setTrackStuckThreshold();
        //        playerManager.setUseSeekGhosting();
        // Register Built-In sources for tracks
        AudioSourceManagers.registerRemoteSources(playerManager);
        return playerManager;
    }

    @Bean
    @Scope("prototype")
    public GuildMusicManager guildMusicManager(AudioPlayerManager audioPlayerManager,
                                               @Value("${abgaryan.audio.byte_buffer_size:1024}") int sendHandlerByteBufferSize) {
        return new GuildMusicManager(audioPlayerManager, sendHandlerByteBufferSize);
    }
}
