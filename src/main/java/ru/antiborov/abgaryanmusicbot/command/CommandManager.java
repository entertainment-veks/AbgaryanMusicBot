package ru.antiborov.abgaryanmusicbot.command;

import lombok.extern.java.Log;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log
@Service
public class CommandManager extends ListenerAdapter {
    private final Map<String, SlashCommand> commands;

    @Autowired
    public CommandManager(Map<String, SlashCommand> commands) {
        this.commands = commands;
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        commands.entrySet()
                .stream()
                .dropWhile(entry -> !event.getName().equals(entry.getKey()))
                .findFirst()
                .ifPresent(entry -> {
                    SlashCommand command = entry.getValue();
                    log.fine(String.format("Processing slash command %s with %s",
                            entry.getClass().getSimpleName(), event.getOptions()));
                    command.process(event);
                });
    }
}
