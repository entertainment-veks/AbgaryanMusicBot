package ru.antiborov.abgaryanmusicbot.command.music.repeat;

import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.Shortcut;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import static ru.antiborov.abgaryanmusicbot.command.Commands.REPEAT;

@Component
public class RepeatShortcut extends Repeat implements Shortcut {
    public RepeatShortcut(GuildMusicManagerFactory guildMusicManagerFactory) {
        super(guildMusicManagerFactory);
    }

    @Override
    public String getName() {
        return REPEAT.shortCut;
    }
}
