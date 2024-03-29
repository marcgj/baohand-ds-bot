package Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThumbnailExtractor {

    public static String getThumbnailUrl(String videoUrl) {
        String pattern = "(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#&?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(videoUrl);
        if (matcher.find()) {
            return "http://img.youtube.com/vi/" + matcher.group() + "/0.jpg";
        }
        return "";
    }
}
