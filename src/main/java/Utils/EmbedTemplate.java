package Utils;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.cdimascio.dotenv.Dotenv;
import lavaplayer.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class EmbedTemplate {

    // #######################
    // Error Embeds
    // #######################

    public static MessageEmbed ErrorEmbed (String err, String msg, String hint){
        final EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.red);
        builder.setTitle(err);
        builder.setDescription(msg);
        builder.setFooter(hint);

        return builder.build();
    }

    public static MessageEmbed ErrorEmbed (String err, String msg){
        return ErrorEmbed(err, msg, "");
    }

    // #######################
    // Play Command
    // #######################

    public static MessageEmbed fromAudioTrack(AudioTrack audioTrack, GuildMusicManager musicManager){
        final EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode(Dotenv.load().get("QUOTE_COLOR")));

        if (musicManager.scheduler.getQueue().size() == 0) {
            builder.setTitle("<a:rythmicalparrot:896052037324771348> Reproduint:");
        } else {
            builder.setTitle("<a:rythmicalparrot:896052037324771348> Posant a la cua:");
        }

        builder.setDescription(String.format("[`%s`](%s)",
                audioTrack.getInfo().title,
                audioTrack.getInfo().uri));

        builder.setThumbnail(ThumbnailExtractor.getThumbnailUrl(audioTrack.getInfo().uri));

        return builder.build();
    }

    public static MessageEmbed fromPlayList(AudioPlaylist audioPlaylist, String thumbnailUrl){
        final EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.decode(Dotenv.load().get("QUOTE_COLOR")));

        builder.setTitle("<a:rythmicalparrot:896052037324771348> Posant a la cua la playlist:");


        builder.setDescription(String.format("[`%s`](%s)",
                audioPlaylist.getName(),
                thumbnailUrl));

        builder.setThumbnail(ThumbnailExtractor.getThumbnailUrl(thumbnailUrl));

        return builder.build();
    }

    // #######################
    // General purpose
    // #######################

    public static MessageEmbed generalEmbed(String title, String msg){
        final EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode(Dotenv.load().get("QUOTE_COLOR")));
        builder.setTitle(title);
        builder.setDescription(msg);

        return builder.build();
    }

    public static MessageEmbed levelUpEmbed(User user, int level, int messageCount, int rank){
        final EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode(Dotenv.load().get("QUOTE_COLOR")));
        builder.setTitle(String.format("Enhorabona **%s** ara ets un maricon de categoria %d", user.getName(), level));
        builder.setDescription(String.format("<@%d> ja has enviat **%d** missatges, i estas en la posicio **#%d**", user.getIdLong(), messageCount, rank));

        return builder.build();
    }
}
