package ru.antiborov.abgaryanmusicbot.command.music.play;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.SlashCommand;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;
import ru.antiborov.abgaryanmusicbot.domain.music.RemotePrefixes;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import static ru.antiborov.abgaryanmusicbot.command.Commands.PLAY;

@Log4j2
@Component
public class Play implements SlashCommand {

    private final AudioPlayerManager audioManager;
    private final GuildMusicManagerFactory guildMusicManagerFactory;

    @Autowired
    public Play(AudioPlayerManager audioManager, GuildMusicManagerFactory guildMusicManagerFactory) {
        this.audioManager = audioManager;
        this.guildMusicManagerFactory = guildMusicManagerFactory;
    }

    // We suppress because we have a nullable on .getGuild and we have a guild presence check in CommandManager
    @SuppressWarnings("ConstantConditions")
    @Override
    public void process(SlashCommandEvent event) {
        GuildMusicManager manager = guildMusicManagerFactory.getInstance(event);
        OptionMapping sourceMapping = event.getOption("source");
        String title = event.getOption("title").getAsString();
        String audioID;

        try {
            // Just doing a trivial URL check. Regexes will be much slower and adding Apache Commons for only this
            // use case is kinda strange imho
            new URL(title);
            audioID = title;
        } catch (MalformedURLException ignored) {
            String source;
            if (sourceMapping == null)
                // If there is no source and not a URL was passed then we will search from YouTube
                source = RemotePrefixes.YOUTUBE.prefix;
            else
                source = sourceMapping.getAsString();

            audioID = source + title;
        }

        // Obtaining Member's Voice Channel and doing quick check
        // Requires CacheFlag.VOICE_STATE to be Enabled
        VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
        if (voiceChannel == null) {
            event.reply("You're not in a voice channel").queue();
            return;
        }

        audioManager.loadItemOrdered(manager, audioID, new AudioLoadResultHandlerImpl(event, voiceChannel, manager));
    }

    public CommandData getCommandData() {
        return SlashCommand.super.getCommandData()
                .addOptions(
                        new OptionData(OptionType.STRING, "title", "URL or Audio Title", true),
                        new OptionData(OptionType.STRING, "source", "Search Engine", false)
                                .addChoice("YouTube", RemotePrefixes.YOUTUBE.prefix)
                                .addChoice("SoundCloud", RemotePrefixes.SOUNDCLOUD.prefix)
                );
    }

    @Override
    public String getName() {
        return PLAY.fullName;
    }

    @Override
    public String getDescription() {
        return "Command for playing an audio";
    }

    @Override
    public boolean isGuildOnly() {
        return true;
    }

    /**
     * Encapsulating logic of AudioLoadResultHandler into Private Implementation class instead of anonymous
     * declaration for the sake of a cleaner code
     */
    private static class AudioLoadResultHandlerImpl implements AudioLoadResultHandler {
        private final SlashCommandEvent event;
        private final VoiceChannel voiceChannel;
        private final GuildMusicManager manager;

        public AudioLoadResultHandlerImpl(SlashCommandEvent event,
                                          VoiceChannel voiceChannel,
                                          GuildMusicManager manager) {
            this.event = event;
            this.voiceChannel = voiceChannel;
            this.manager = manager;
        }

        @SuppressWarnings("ConstantConditions") // Command is Guild Only
        @Override
        public void trackLoaded(AudioTrack track) {
            log.debug("Loaded track: " + track.toString());
            AudioManager audioManager = event.getGuild().getAudioManager();
            audioManager.openAudioConnection(voiceChannel);
            audioManager.setSendingHandler(manager.getSendHandler());
            manager.getTrackScheduler().queue(track);
            event.reply("less goo " + track.getInfo().title).queue();
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            // TODO: Playlist handling and different subcommand for processing these kinds of requests
            log.debug("Playlist loaded");
            log.debug(playlist
                    .getTracks()
                    .stream()
                    .map(t -> t.getInfo().title)
                    .collect(Collectors.joining("\n")
                    ));
            trackLoaded(playlist.getTracks().get(0));
        }

        @Override
        public void noMatches() {
            log.debug("No matches");
            event.reply("no matches").queue();
        }

        @Override
        public void loadFailed(FriendlyException exception) {
            log.warn("Encountered exception: " + exception.toString());
            event.reply("Encountered exception " + exception).queue();
        }
    }
}
