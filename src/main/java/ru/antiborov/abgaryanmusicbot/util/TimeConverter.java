package ru.antiborov.abgaryanmusicbot.util;

import java.util.concurrent.TimeUnit;

public class TimeConverter {
    public static String formatMs(long ms) {
        var builder = new StringBuilder();
        var hours = TimeUnit.MILLISECONDS.toHours(ms);
        var minutes = TimeUnit.MILLISECONDS.toMinutes(ms) - hours * 60;
        var seconds = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MILLISECONDS.toMinutes(ms) * 60;
        if (hours >= 1)
            builder.append(hours < 10 ? "0" + hours : hours).append(":");
        return builder.append(minutes < 10 ? "0" + minutes : minutes).append(":")
                .append(seconds < 10 ? "0" + seconds : seconds)
                .toString();
    }
}
