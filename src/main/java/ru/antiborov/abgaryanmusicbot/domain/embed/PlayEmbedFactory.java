package ru.antiborov.abgaryanmusicbot.domain.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class PlayEmbedFactory implements EmbedFactory {
    @Override
    public MessageEmbed makeEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(String.format("Play: %v"));
        return null;
    }
}
