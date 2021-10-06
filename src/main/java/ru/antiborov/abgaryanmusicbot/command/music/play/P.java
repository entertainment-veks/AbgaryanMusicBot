package ru.antiborov.abgaryanmusicbot.command.music.play;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.music.Commands;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import static ru.antiborov.abgaryanmusicbot.command.music.Commands.PLAY;


@Component("p")
public class P extends Play {

    @Autowired
    public P(AudioPlayerManager audioManager, GuildMusicManagerFactory guildMusicManagerFactory) {
        super(audioManager, guildMusicManagerFactory);
    }

    @Override
    public CommandData getCommandData() {
        return super.getCommandData().setName(PLAY.shorCut);
    }
}
