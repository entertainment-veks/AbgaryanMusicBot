package ru.antiborov.abgaryanmusicbot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.SlashCommand;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

@Component("skip")
public class Skip implements SlashCommand {
    private final GuildMusicManagerFactory guildMusicManagerFactory;

    @Autowired
    public Skip(GuildMusicManagerFactory guildMusicManagerFactory) {
        this.guildMusicManagerFactory = guildMusicManagerFactory;
    }

    @SuppressWarnings("ConstantConditions") // Command is Guild Only
    @Override
    public void process(SlashCommandEvent event) {
        GuildMusicManager manager = guildMusicManagerFactory.getInstance(event);
        AudioPlayer audioPlayer = manager.getAudioPlayer();

        AudioTrack track = manager.getTrackScheduler().nextTrack();
        if (track == null) {
            event.reply("queue has ended").queue();
            return;
        }

        event.reply("playing " + (audioPlayer.isPaused() ? " and unpaused " : "") + track.getInfo().title).queue();
        audioPlayer.setPaused(false);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("skip", getDescription());
    }

    @Override
    public String getDescription() {
        return "Skips current playing track";
    }

    @Override
    public boolean isGuildOnly() {
        return true;
    }
}
