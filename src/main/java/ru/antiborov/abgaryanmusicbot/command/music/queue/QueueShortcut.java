package ru.antiborov.abgaryanmusicbot.command.music.queue;

import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.Shortcut;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import static ru.antiborov.abgaryanmusicbot.command.Commands.QUEUE;

@Component
public class QueueShortcut extends Queue implements Shortcut {
    public QueueShortcut(GuildMusicManagerFactory guildMusicManagerFactory) {
        super(guildMusicManagerFactory);
    }

    @Override
    public String getName() {
        return QUEUE.shortCut;
    }
}
