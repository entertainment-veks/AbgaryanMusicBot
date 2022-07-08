package ru.antiborov.abgaryanmusicbot.domain.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Stream;

@Log4j2
@Getter
@Setter
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer audioPlayer;
    private final BlockingDeque<AudioTrack> queue;
    private RepeatStatus repeat = RepeatStatus.NONE;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingDeque<>();
    }

    public void queue(AudioTrack track) {
        // If something is playing - the track will be added to the queue
        if (!audioPlayer.startTrack(track, true)) {
            this.queue.offerLast(track);
        }
    }

    public void skip() {
        AudioTrack currentTrack = audioPlayer.getPlayingTrack();
        if (currentTrack != null)
            onTrackEnd(audioPlayer, currentTrack, AudioTrackEndReason.FINISHED);
    }

    public void nextTrack() {
        audioPlayer.playTrack(queue.pollFirst());
    }

    // Audio Event Handling

    /**
     * @param player Audio player
     */
    @Override
    public void onPlayerPause(AudioPlayer player) {
        log.debug("AudioPlayer has paused playing");
    }

    /**
     * @param player Audio player
     */
    @Override
    public void onPlayerResume(AudioPlayer player) {
        log.debug("AudioPlayer has resumed playing");
    }

    /**
     * @param player Audio player
     * @param track  Audio track that started
     */
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        log.debug(
                "AudioPlayer has started playing "
                        + track.getInfo().uri);
    }

    /**
     * @param player    Audio player
     * @param track     Audio track that ended
     * @param endReason The reason why the track stopped playing
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        log.debug(
                "AudioPlayer track has ended with code "
                        + endReason
                        + " - "
                        + track.getInfo().uri);

        if (!endReason.mayStartNext)
            return;

        if (repeat == RepeatStatus.SINGLE) {
            queue.offerFirst(track.makeClone());
            log.debug("Repeating single track {}", track.getInfo().title);
        } else if (repeat == RepeatStatus.QUEUE) {
            queue.offerLast(track.makeClone());
            log.debug("Repeating queue, added track to the end of the queue {}", track.getInfo().title);
        }

        nextTrack();
    }

    /**
     * @param player    Audio player
     * @param track     Audio track where the exception occurred
     * @param exception The exception that occurred
     */
    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        switch (exception.severity) {
            case COMMON:
                // fallthrough
            case SUSPICIOUS:
                log.warn(
                        "AudioPlayer has encountered an exception while playing "
                                + exception
                                + " - "
                                + track.getInfo().uri);
                break;
            case FAULT:
                log.warn(
                        "AudioPlayer has encountered a fatal exception while playing "
                                + exception
                                + " - "
                                + track.getInfo().uri);
                break;
        }
    }

    /**
     * @param player      Audio player
     * @param track       Audio track where the exception occurred
     * @param thresholdMs The wait threshold that was exceeded for this event to trigger
     */
    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        log.warn(
                "AudioPlayer has exceeded Threshold of "
                        + thresholdMs
                        + " millis on track "
                        + track.getInfo().uri);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs, StackTraceElement[] stackTrace) {
        log.warn(
                "AudioPlayer has exceeded Threshold of "
                        + thresholdMs
                        + " millis on track "
                        + track.getInfo().uri
                        + " stacktrace: "
                        + Arrays.toString(stackTrace));
    }

    public Stream<AudioTrack> getQueueAsStream() {
        return queue.stream();
    }
}
