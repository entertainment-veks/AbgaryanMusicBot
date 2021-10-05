package ru.antiborov.abgaryanmusicbot.command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface SlashCommand {
    void process(SlashCommandEvent event);

    CommandData getCommandData();

    String getDescription();

    boolean isGuildOnly();
}
