package ru.antiborov.abgaryanmusicbot.domain.music.factory;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;
import ru.antiborov.abgaryanmusicbot.annotation.Internal;
import ru.antiborov.abgaryanmusicbot.domain.music.GuildMusicManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/*
 This class is basically a way to simplify a lot of hustle we'll get if we'll try to move fully to Spring IoC
 Instead of creating custom scope with a container and working our way to create GuildMusicManager beans via the
 BeanDefinitions we use a simpler approach and don't overwhelm ourselves
 There is not a big need in that and this is justified by the KISS Principle
 */
@Component
public class GuildMusicManagerFactory {
    private final Map<String, GuildMusicManager> cache = new ConcurrentHashMap<>();

    @Lookup
    @Internal
    public GuildMusicManager getGuildMusicManager() {
        return null;
    }

    public GuildMusicManager getInstance(String guildId) {
        return ((Supplier<GuildMusicManager>) () -> {
            if (cache.containsKey(guildId))
                return cache.get(guildId);

            GuildMusicManager manager = getGuildMusicManager();
            cache.putIfAbsent(guildId, manager);
            return manager;
        }).get();
    }
}