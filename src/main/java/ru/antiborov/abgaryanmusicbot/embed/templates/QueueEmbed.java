package ru.antiborov.abgaryanmusicbot.embed.templates;


import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import ru.antiborov.abgaryanmusicbot.util.TimeConverter;

import java.util.List;

import static ru.antiborov.abgaryanmusicbot.util.TimeConverter.formatMs;


public class QueueEmbed extends EmbedBuilder {
    public QueueEmbed(AudioTrack current, List<AudioTrack> tracks, int color, boolean paused) {
        if (current == null) {
            super.setTitle("Tracks in queue 0")
                    .setDescription("No tracks in queue!")
                    .setColor(0x000FF);
            return;
        }

        super.setTitle(String.format("Tracks in queue %s", tracks.size()));
        super.addField("",
                String.format("Currently %s: **%s** %s/%s",
                        paused ? "paused" : "playing",
                        current.getInfo().title,
                        formatMs(current.getPosition()),
                        formatMs(current.getInfo().length)),
                false);
        for (int i = 0; i < tracks.size(); i++) {
            var info = tracks.get(i).getInfo();
            super.addField("", String.format("**%s** *<|>* **%s** %s", i + 1, info.title, formatMs(info.length)), false);
        }
        super.setColor(color);
    }
}
