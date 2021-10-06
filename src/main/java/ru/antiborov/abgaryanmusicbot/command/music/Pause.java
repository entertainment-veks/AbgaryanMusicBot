package ru.antiborov.abgaryanmusicbot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.SlashCommand;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

@Component("pause")
public class Pause implements SlashCommand {
    private final GuildMusicManagerFactory guildMusicManagerFactory;

    @Autowired
    public Pause(GuildMusicManagerFactory guildMusicManagerFactory) {
        this.guildMusicManagerFactory = guildMusicManagerFactory;
    }

    @Override
    public void process(SlashCommandEvent event) {
        GuildMusicManager guildMusicManager = guildMusicManagerFactory.getInstance(event);
        AudioPlayer player = guildMusicManager.getAudioPlayer();
        if (player.getPlayingTrack() == null) {
            player.setPaused(false);
            event.reply("Nothing to pause since player is not playing").queue();
            return;
        }

        player.setPaused(!player.isPaused());
        event.reply((player.isPaused() ? "paused" : "unpaused") + " the player").queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("pause", getDescription());
    }

    @Override
    public String getDescription() {
        return "Pauses the player";
    }

    @Override
    public boolean isGuildOnly() {
        return true;
    }
}
