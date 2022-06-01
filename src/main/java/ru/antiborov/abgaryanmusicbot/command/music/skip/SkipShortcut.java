package ru.antiborov.abgaryanmusicbot.command.music.skip;

import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.Shortcut;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import static ru.antiborov.abgaryanmusicbot.command.Commands.SKIP;

@Component
public class SkipShortcut extends Skip implements Shortcut {
    public SkipShortcut(GuildMusicManagerFactory guildMusicManagerFactory) {
        super(guildMusicManagerFactory);
    }

    @Override
    public String getName() {
        return SKIP.shortCut;
    }
}
