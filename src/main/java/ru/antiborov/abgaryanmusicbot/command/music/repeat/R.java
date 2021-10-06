package ru.antiborov.abgaryanmusicbot.command.music.repeat;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import ru.antiborov.abgaryanmusicbot.command.music.Commands;
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
