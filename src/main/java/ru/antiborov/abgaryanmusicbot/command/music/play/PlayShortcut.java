package ru.antiborov.abgaryanmusicbot.command.music.play;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.Shortcut;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import static ru.antiborov.abgaryanmusicbot.command.Commands.PLAY;


@Component
public class PlayShortcut extends Play implements Shortcut {

    @Autowired
    public PlayShortcut(AudioPlayerManager audioManager, GuildMusicManagerFactory guildMusicManagerFactory) {
        super(audioManager, guildMusicManagerFactory);
    }

    @Override
    public String getName() {
        return PLAY.shortCut;
    }
}
