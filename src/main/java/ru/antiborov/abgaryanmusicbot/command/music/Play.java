package ru.antiborov.abgaryanmusicbot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.java.Log;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.command.SlashCommand;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;
import ru.antiborov.abgaryanmusicbot.domain.music.RemotePrefixes;
import ru.antiborov.abgaryanmusicbot.domain.music.factory.GuildMusicManagerFactory;

import java.util.stream.Collectors;

@Log
@Component("play")
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
        String audioID;

        assert event.getSubcommandName() != null;

        // We ignore these warnings since parameters are required and semantics of null check is obsolete
        switch (event.getSubcommandName()) {
            case "soundcloud":
                audioID = RemotePrefixes.SOUNDCLOUD.prefix + event.getOption("title").getAsString();
                break;
            case "url":
                audioID = event.getOption("url").getAsString();
                break;
            default:
                audioID = RemotePrefixes.YOUTUBE.prefix + event.getOption("title").getAsString();
                break;
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

    // DRY?
    public CommandData getCommandData() {
        return new CommandData("play", getDescription())
                .addSubcommands(
                        new SubcommandData("", "search via youtube")
                                .addOption(OptionType.STRING,
                                        "title",
                                        "le youtube video title",
                                        true),
                        new SubcommandData("youtube", "search via youtube")
                                .addOption(OptionType.STRING,
                                        "title",
                                        "le youtube video title",
                                        true),
                        new SubcommandData("soundcloud", "search via soundcloud")
                                .addOption(OptionType.STRING,
                                        "title",
                                        "le soundcloud song title",
                                        true),
                        new SubcommandData("url", "enter url to the desired track")
                                .addOption(OptionType.STRING,
                                        "url",
                                        "le url",
                                        true));
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
            log.fine("Loaded track: " + track.toString());
            AudioManager audioManager = event.getGuild().getAudioManager();
            audioManager.openAudioConnection(voiceChannel);
            audioManager.setSendingHandler(manager.getSendHandler());
            manager.getTrackScheduler().queue(track);
            event.reply("less goo " + track.getInfo().title).queue();
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            // TODO: Playlist handling and different subcommand for processing these kinds of requests
            log.fine("Playlist loaded");
            log.finest(playlist
                    .getTracks()
                    .stream()
                    .map(t -> t.getInfo().title)
                    .collect(Collectors.joining("\n")
                    ));
            trackLoaded(playlist.getTracks().get(0));
        }

        @Override
        public void noMatches() {
            log.fine("No matches");
            event.reply("no matches").queue();
        }

        @Override
        public void loadFailed(FriendlyException exception) {
            log.warning("Encountered exception: " + exception.toString());
            event.reply("Encountered exception " + exception).queue();
        }
    }
}
