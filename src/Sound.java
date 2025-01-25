//Import the AudioInputSystem, Clip and ability to get things from files
import javax.sound.sampled.*;
import java.io.File;

public class Sound
{
    public boolean isPlaying = false;
    private Clip sfxClip; //sound effect clip
    private AudioInputStream audioStream; //allows us to play the sound

    //Constructor
    public Sound(String pathToSound)
    {
        //loading files is prone to error, so we'll try this code...
        try
        {
            // the input stream object, get file and load it into playback AudioInputStream
            audioStream = AudioSystem.getAudioInputStream
                    (new File(pathToSound).getAbsoluteFile());
            // the reference to the clip we want to play on the AudioInputStream
            sfxClip = AudioSystem.getClip();
            //prepare clip for playback
            sfxClip.open(audioStream);
        }
        //...and catch any errors (i.e Exceptions) that occur
        //the most common being the file is not found, misspelled or incorrect format
        catch (Exception e)
        {
            System.out.println("Error: Confirm file location is accurate, file is named correctly and is in .wav format\n" + e.getStackTrace());
        }
    }

    // Play Sound
    public void play() {
        //start the clip if it isn't already playing
        if (!isPlaying) {
            sfxClip.start();
            isPlaying = true;
        }
        //if the clip is no longer running (finished playing)
        else if (!sfxClip.isRunning())
        {
            stop();
        }
    }

    // Loop Playing Sound forever (-1)
    public void loop() {
        if (!isPlaying)
        {
            sfxClip.loop(-1);
            isPlaying = true;
        }
    }

    // Stop Sound, reset it back to start
    public void stop() {
        //reset the playback position to the start
        isPlaying = false;
        sfxClip.setMicrosecondPosition(0);
        sfxClip.stop();
    }


}