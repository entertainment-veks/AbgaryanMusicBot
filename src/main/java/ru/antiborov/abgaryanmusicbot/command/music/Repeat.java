package ru.antiborov.abgaryanmusicbot.command.music;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.SlashCommand;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;
import ru.antiborov.abgaryanmusicbot.domain.music.TrackScheduler;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;
import static ru.antiborov.abgaryanmusicbot.command.music.Commands.REPEAT;

@Component("repeat")
public class Repeat implements SlashCommand {
    private final GuildMusicManagerFactory guildMusicManagerFactory;

    @Autowired
    public Repeat(GuildMusicManagerFactory guildMusicManagerFactory) {
        this.guildMusicManagerFactory = guildMusicManagerFactory;
    }

    @Override
    public void process(SlashCommandEvent event) {
        GuildMusicManager guildMusicManager = guildMusicManagerFactory.getInstance(event);
        TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
        trackScheduler.setRepeat(!trackScheduler.isRepeat());
        event.reply(trackScheduler.isRepeat()
                ? "Repeating the queue"
                : "Stopped repeating the queue").queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(REPEAT.fullName, getDescription());
    }

    @Override
    public String getDescription() {
        return "Repeat the queue";
    }

    @Override
    public boolean isGuildOnly() {
        return true;
    }
}
