package fr.trepia.deezersample.utils;

public class DurationUtils {

    public static String durationToString(long duration) {

        long hours = duration / 3600;
        long minutes = (duration % 3600) / 60;
        long seconds = ((duration % 3600) % 60);

        if (hours > 0)
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        else
            return String.format("%d:%02d", minutes, seconds);
    }
}
