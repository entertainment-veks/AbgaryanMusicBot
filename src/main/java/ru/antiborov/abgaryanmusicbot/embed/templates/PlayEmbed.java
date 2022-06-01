package ru.antiborov.abgaryanmusicbot.embed.templates;

import net.dv8tion.jda.api.EmbedBuilder;

public class PlayEmbed extends EmbedBuilder {
    public PlayEmbed(String title, String author, int color, String thumbnailUrl) {
        super.setTitle(String.format("Playing %s", title))
                .setAuthor(author)
                .setColor(color);
        if (thumbnailUrl != null)
            super.setImage(thumbnailUrl);
    }
}
