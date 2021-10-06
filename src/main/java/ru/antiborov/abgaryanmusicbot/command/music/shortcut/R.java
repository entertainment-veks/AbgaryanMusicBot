package ru.antiborov.abgaryanmusicbot.command.music.shortcut;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import ru.antiborov.abgaryanmusicbot.command.music.Repeat;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import static ru.antiborov.abgaryanmusicbot.command.music.Commands.REPEAT;

public class R extends Repeat {
    public R(GuildMusicManagerFactory guildMusicManagerFactory) {
        super(guildMusicManagerFactory);
    }

    @Override
    public CommandData getCommandData() {
        return super.getCommandData().setName(REPEAT.shorCut);
    }
}
