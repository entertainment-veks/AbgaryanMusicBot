package ru.antiborov.abgaryanmusicbot.config;


import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.antiborov.abgaryanmusicbot.command.SlashCommand;

import javax.security.auth.login.LoginException;
import java.util.Map;

/**
 * JDA Configuration class
 * <p>
 * This class configures the JDA client for further interaction and processing
 */
@Log4j2
@Configuration
public class JDAConfig {
    // We are declaring eventListeners as an array because .addEventListeners(...) uses varargs and to prevent extra
    // wrapping into Object[] we need to use array
    private final EventListener[] eventListeners;

    //
    @Value("${jda.token}")
    private String token;

    /**
     * @param eventListeners {@link EventListener}'s currently declared as {@link org.springframework.stereotype.Component}
     */
    @Autowired(required = false)
    public JDAConfig(EventListener... eventListeners) {
        this.eventListeners = eventListeners;
    }

    /**
     * JDA Bean Factory method
     *
     * @return configured {@link JDA} instance
     * @throws LoginException if authentication to the Discord has been failed
     */
    @Bean
    public JDA jda(Map<String, SlashCommand> commands) throws LoginException, InterruptedException {
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners((Object[]) eventListeners)
                .build();
        jda.awaitReady();
        // Registering commands for each guild TODO: Move this behaviour to the "dev" profile
        jda.getGuilds().forEach(guild -> {
            log.debug(guild.getName());
            commands.forEach((key, value) -> guild.upsertCommand(value.getCommandData()).queue());
            guild.retrieveCommands().complete().stream().map(Object::toString).forEach(log::debug);
        });
        return jda;
    }
}
