package ru.antiborov.abgaryanmusicbot.command.music.skip;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.SlashCommand;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import static ru.antiborov.abgaryanmusicbot.command.Commands.SKIP;

@Component
public class Skip implements SlashCommand {
    private final GuildMusicManagerFactory guildMusicManagerFactory;

    @Autowired
    public Skip(GuildMusicManagerFactory guildMusicManagerFactory) {
        this.guildMusicManagerFactory = guildMusicManagerFactory;
    }

    @Override
    public void process(SlashCommandEvent event) {
        GuildMusicManager manager = guildMusicManagerFactory.getInstance(event);
        AudioPlayer audioPlayer = manager.getAudioPlayer();

        manager.getTrackScheduler().skip();
        AudioTrack track = audioPlayer.getPlayingTrack();
        if (track == null) {
            event.reply("queue has ended").queue();
            return;
        }

        event.reply("playing " + (audioPlayer.isPaused() ? " and unpaused " : "") + track.getInfo().title).queue();
        audioPlayer.setPaused(false);
    }

    @Override
    public String getName() {
        return SKIP.fullName;
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
