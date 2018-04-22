package audio;

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound
{
    public static Sound pickup = new Sound("/sfx/pickup.wav");
    public static Sound hurt = new Sound("/sfx/hurt.wav");
    public static Sound spawn = new Sound("/sfx/spawn.wav");
    public static Sound hit = new Sound("/sfx/hit.wav");
    public static Sound monsterhurt = new Sound("/sfx/zombiehurt.wav");
    
    public String path;
    public int volume;
    
    /**
     * 
     * @param path 
     */
    private Sound(String path)
    {
        this.path = path;
        this.volume = 70;
    }
    
    /**
     * 
     */
    public void play()
    {
        this.volume = 70;
        try
        {
            InputStream inputStream = this.getClass().getResourceAsStream(this.path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            Clip clip = AudioSystem.getClip();
            
            AudioFormat audioFormat = audioInputStream.getFormat();
            
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            
            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
            
            if(sourceLine.isControlSupported(FloatControl.Type.MASTER_GAIN))
            {
                FloatControl gainControl = (FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN);
                float attenuation = -80 + (80 * this.volume / 100);
                gainControl.setValue(attenuation);
            }
                
            sourceLine.start();
            
            int nBytesRead = 0;
            byte[] abData = new byte[128000];
            while (nBytesRead != -1)
            {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                {
                    @SuppressWarnings("unused")
                    int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                }
            }
            sourceLine.drain();
            sourceLine.close();
        }
        catch(IOException|UnsupportedAudioFileException|LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }
}
