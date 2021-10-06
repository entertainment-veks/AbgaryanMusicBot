package ru.antiborov.abgaryanmusicbot.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.SlashCommand;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import java.util.Locale;
import java.util.stream.Collectors;

@Component("queue")
public class Queue implements SlashCommand {
    private final GuildMusicManagerFactory guildMusicManagerFactory;

    @Autowired
    public Queue(GuildMusicManagerFactory guildMusicManagerFactory) {
        this.guildMusicManagerFactory = guildMusicManagerFactory;
    }

    @SuppressWarnings("ConstantConditions") // Command is Guild Only
    @Override
    public void process(SlashCommandEvent event) {
        GuildMusicManager manager = guildMusicManagerFactory.getInstance(event);
        AudioTrack current_track = manager.getAudioPlayer().getPlayingTrack();
        if (current_track == null) {
            event.reply("Queue is empty and nothing is currently playing").queue();
            return;
        }

        event.reply("Currently " + (manager.getAudioPlayer().isPaused() ? "paused" : "playing") + ": "
                + trackFormatter(current_track) + "\n"
                + manager.getTrackScheduler()
                .getQueueAsStream()
                .map(this::trackFormatter)
                .collect(Collectors.joining("\n"))).queue();
    }

    // TODO: Move to QueueEmbedFactory
    @Deprecated
    private String trackFormatter(AudioTrack track) {
        return "Track: " + track.getInfo().title
                + " by " + track.getInfo().author
                + " from " + track.getSourceManager().getSourceName().toUpperCase(Locale.ROOT);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("queue", getDescription());
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
