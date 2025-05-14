package henu.javaweb.collapstime.utils;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;

public class MusicUtils {
    public static String getMusicDuration(String path){
        File source =new File(path);
        Encoder encoder = new Encoder();
        MultimediaInfo m = null;
        try {
            m = encoder.getInfo(source);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
        long ls;
        try {
            ls = m.getDuration();
        }catch (Exception e){
            return "未知";
        }
        long seconds = ls/1000-ls/60000*60;
        String s = String.format("%02d", seconds);
        return ls/60000+":"+s;
    }
    public static String getDuration(String position) {
        int length = 0;
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(new File(position));
            MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();
            // 单位为秒
            length = audioHeader.getTrackLength();
            int second = length - length/60 *60;
            String s = String.format("%02d", second);
            return length/60 +":"+s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "未知";
    }

}
