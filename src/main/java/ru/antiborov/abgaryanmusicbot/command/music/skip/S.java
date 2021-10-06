package ru.antiborov.abgaryanmusicbot.command.music.skip;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import ru.antiborov.abgaryanmusicbot.command.music.Commands;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

public class S extends Skip {
    public S(GuildMusicManagerFactory guildMusicManagerFactory) {
        super(guildMusicManagerFactory);
    }

    @Override
    public CommandData getCommandData() {
        return super.getCommandData().setName(Commands.SKIP.shorCut);
    }
}
