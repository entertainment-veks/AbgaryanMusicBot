package ru.antiborov.abgaryanmusicbot.command.music.repeat;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.SlashCommand;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;
import ru.antiborov.abgaryanmusicbot.domain.music.RepeatStatus;
import ru.antiborov.abgaryanmusicbot.domain.music.TrackScheduler;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import static ru.antiborov.abgaryanmusicbot.command.Commands.REPEAT;

@Component
public class Repeat implements SlashCommand {
    private final GuildMusicManagerFactory guildMusicManagerFactory;

    @Autowired
    public Repeat(GuildMusicManagerFactory guildMusicManagerFactory) {
        this.guildMusicManagerFactory = guildMusicManagerFactory;
    }

    @Override
    public void process(SlashCommandEvent event) {
        GuildMusicManager guildMusicManager = guildMusicManagerFactory.getInstance(event);
        OptionMapping typeMapping = event.getOption("type");
        TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();

        if (typeMapping == null) {
            // If not repeating - repeat a queue;
            // If repeating queue or a track - don't repeat;
            if (trackScheduler.getRepeat().equals(RepeatStatus.NONE)) {
                trackScheduler.setRepeat(RepeatStatus.QUEUE);
            } else {
                trackScheduler.setRepeat(RepeatStatus.NONE);
            }
        } else {
            // Setting repeat based on provided option
            trackScheduler.setRepeat(RepeatStatus.valueOf(typeMapping.getAsString()));
        }

        // TODO: Move to Embed Factory
        event.reply(trackScheduler.getRepeat().equals(RepeatStatus.QUEUE)
                ? "Repeating the queue"
                : trackScheduler.getRepeat().equals(RepeatStatus.SINGLE)
                ? "Repeating single track"
                : "Stopped repeating the queue").queue();
    }

    @Override
    public CommandData getCommandData() {
        return SlashCommand.super.getCommandData()
                .addOptions(
                        new OptionData(OptionType.STRING, "type", "repeat type", false)
                                .addChoice("None", RepeatStatus.NONE.name())
                                .addChoice("Single", RepeatStatus.SINGLE.name())
                                .addChoice("Queue", RepeatStatus.QUEUE.name())
                );
    }

    @Override
    public String getName() {
        return REPEAT.fullName;
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
