package ru.antiborov.abgaryanmusicbot.command.music;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.SlashCommand;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import static ru.antiborov.abgaryanmusicbot.command.Commands.STOP;

@Component
public class Stop implements SlashCommand {
    private final GuildMusicManagerFactory guildMusicManagerFactory;

    @Autowired
    public Stop(GuildMusicManagerFactory guildMusicManagerFactory) {
        this.guildMusicManagerFactory = guildMusicManagerFactory;
    }

    @SuppressWarnings("ConstantConditions") // Command is Guild Only
    @Override
    public void process(SlashCommandEvent event) {
        GuildMusicManager manager = guildMusicManagerFactory.getInstance(event);
        manager.getAudioPlayer().stopTrack();
        event.getGuild().getAudioManager().closeAudioConnection();
        event.reply("stopped the player and left voice channel").queue();
    }

    @Override
    public String getName() {
        return STOP.fullName;
    }

    @Override
    public String getDescription() {
        return "Stop the player";
    }

    @Override
    public boolean isGuildOnly() {
        return true;
    }
}
