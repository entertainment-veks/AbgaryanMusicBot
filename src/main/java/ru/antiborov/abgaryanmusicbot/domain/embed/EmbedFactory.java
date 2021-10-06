package ru.antiborov.abgaryanmusicbot.domain.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface EmbedFactory {
    MessageEmbed makeEmbed();
}
