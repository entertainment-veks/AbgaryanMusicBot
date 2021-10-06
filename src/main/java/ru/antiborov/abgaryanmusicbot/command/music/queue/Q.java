package ru.antiborov.abgaryanmusicbot.command.music.queue;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import static ru.antiborov.abgaryanmusicbot.command.music.Commands.QUEUE;

@Component("q")
public class Q extends Queue {
    public Q(GuildMusicManagerFactory guildMusicManagerFactory) {
        super(guildMusicManagerFactory);
    }

    @Override
    public CommandData getCommandData() {
        return super.getCommandData().setName(QUEUE.shorCut);
    }
}
