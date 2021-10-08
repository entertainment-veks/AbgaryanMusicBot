package ru.antiborov.abgaryanmusicbot.command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface SlashCommand {
    void process(SlashCommandEvent event);

    default CommandData getCommandData() {
        return new CommandData(getName(), getDescription());
    }

    String getName();

    String getDescription();

    boolean isGuildOnly();
}
