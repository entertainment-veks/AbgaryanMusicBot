package ru.antiborov.abgaryanmusicbot.command.music.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.SlashCommand;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;
import ru.antiborov.abgaryanmusicbot.embed.templates.QueueEmbed;
import ru.antiborov.abgaryanmusicbot.util.SourceColors;

import java.util.Locale;
import java.util.stream.Collectors;

import static ru.antiborov.abgaryanmusicbot.command.Commands.QUEUE;

@Component
public class Queue implements SlashCommand {
    private final GuildMusicManagerFactory guildMusicManagerFactory;

    @Autowired
    public Queue(GuildMusicManagerFactory guildMusicManagerFactory) {
        this.guildMusicManagerFactory = guildMusicManagerFactory;
    }

    @Override
    public void process(SlashCommandEvent event) {
        GuildMusicManager manager = guildMusicManagerFactory.getInstance(event);
        AudioTrack currentTrack = manager.getAudioPlayer().getPlayingTrack();

        event.reply("")
                .addEmbeds(new QueueEmbed(
                        currentTrack,
                        manager.getTrackScheduler().getQueueAsStream().collect(Collectors.toList()),
                        SourceColors.determineColor(currentTrack),
                        manager.getAudioPlayer().isPaused())
                        .build())
                .queue();
    }

    @Override
    public String getName() {
        return QUEUE.fullName;
    }

    @Override
    public String getDescription() {
        return "Returns the current queue of Audio Tracks";
    }

    @Override
    public boolean isGuildOnly() {
        return true;
    }
}
