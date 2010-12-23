/** 
 * JOPS - Java Originated Presentation System
 * (A proof-of-concept for future BAPS replacement)
 * 
 * This file is part of JOPS.
 *
 * JOPS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JOPS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JOPS.  If not, see <http://www.gnu.org/licenses/>.
 **/

package jops.server;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 * A player node to play any music file supported by the Java Sound System - 
 * that is, any music file with an SPI available.
 * 
 * The system, by default, packs a FLAC SPI (jflac).
 * 
 * @author Matt Windsor
 */
public class SpiPlayer implements Runnable, FilePlayer
{
  private String name;
  private String fileName;
  private JopsServer controller;
  private Mixer.Info mixerInfo;
  private volatile Thread thread;
  private volatile boolean paused;
  
  
  public
  SpiPlayer (String inFileName, Mixer.Info inMixer, JopsServer inController)
  {
    name = "untitled";
    mixerInfo = inMixer;
    fileName = inFileName;
    paused = false;
    controller = inController;
    
    thread = null;
  }
  
  
  /**
   * Change the file being played.
   *
   * @param inFileName  The filename of the new file to be played.
   */
  
  public void
  changeFile (String inFileName)
  {
    boolean wasRunning = false;
    
    if (isRunning () == true)
      {
        stop ();
        wasRunning = true; 
      }
    
    fileName = inFileName;
    
    // Only start playing if the previous song was running and unpaused.
    
    if (wasRunning == true && paused == false)
      start ();
    
    paused = false;
  }
  
  
  /**
   * Get a data line from the mixer on which to play the stream.
   * 
   * @param audioFormat  The desired audio format.
   * @return  A line on which to play the stream.
   * @throws LineUnavailableException
   */

  private SourceDataLine
  getLine (AudioFormat audioFormat) throws LineUnavailableException
  {
    SourceDataLine res = null;
    DataLine.Info info = new DataLine.Info (SourceDataLine.class, audioFormat);
    Mixer mixer = AudioSystem.getMixer (mixerInfo);
    res = (SourceDataLine) mixer.getLine (info);
    res.open (audioFormat);
    return res;
  }
  
  
  /**
   * Get the mixer the player is feeding into.
   * 
   * @return  the Mixer.Info node corresponding to the player.
   */
  
  public Mixer.Info
  getMixer ()
  {
    return mixerInfo;
  }
  
  
  /**
   * Get the name of the mixer that this player is feeding into.
   * 
   * @return  the name of the mixer.
   */
  
  public String
  getMixerName ()
  {
    return mixerInfo.getName ();
  }
  
  
  /**
   * Get the name of this player.
   * 
   * @return  The name of this player.
   */
  
  public String
  getName ()
  {
    return name;
  }
  
  
  /**
   * Get the type name of this player.
   * 
   * @return  the type name of the player.
   */
  
  public String
  getTypeName ()
  {
    return "SPI-based file player";
  }


  /**
   * Get whether or not the player is currently paused.
   * 
   * @return  true if paused; false otherwise.
   */

  private boolean
  isPaused ()
  {
    return paused;
  }

  
  /**
   * Get whether or not the PlayerCore should be running.
   * @return  true if the player is expected to be running, false otherwise.
   */
  
  public boolean 
  isRunning ()
  {
    return (thread != null);
  }

  /**
   * Play the decoded file-stream until the stream ends or the SpiPlayer is 
   * instructed to stop.
   * 
   * @param targetFormat  The target format of the stream.
   *  @param din           The input stream.
   * @throws IOException
   * @throws LineUnavailableException
   */

  private void
  rawPlay (AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException
  {
    Thread thisThread = Thread.currentThread ();
    
    byte[] data = new byte[4096];

    SourceDataLine line = getLine (targetFormat);

    if (line != null)
      {
        line.start ();
        int nBytesRead = 0;
    
        // nBytesRead == -1 implies end of file, so terminate when this occurs.
    
        while (nBytesRead != -1 && thread == thisThread)
          {
            if (isPaused () == false)
              {
                nBytesRead = din.read (data, 0, data.length);
        
                if (nBytesRead != -1)
                  line.write (data, 0, nBytesRead);
              }
            else
              {
                // Paused, so sleep a bit.
                try
                  {
                    Thread.sleep (10);
                  }
                catch (InterruptedException e)
                  {
                    /* This is perfectly normal.
                       (It occurs if the user changes song while the current
                        song is paused.) */
                  }
              }
          }

        // Stop
    
        //line.drain (); <-- This causes freezes, for some reason.
        line.stop ();
        line.close ();
        din.close ();
      }
  }
  
  
  /**
   * Thread execution body.
   */
  
  public void
  run ()
  {    
    File file = new File (fileName);
  
    AudioInputStream in = null;
    try
      {
        in = AudioSystem.getAudioInputStream (file);
      }
    catch (UnsupportedAudioFileException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
  
    AudioInputStream din = null;
    AudioFormat baseFormat = in.getFormat ();
    AudioFormat decodedFormat = new AudioFormat (AudioFormat.Encoding.PCM_SIGNED,
                                                 baseFormat.getSampleRate(),
                                                 16,
                                                 baseFormat.getChannels(),
                                                 baseFormat.getChannels() * 2,
                                                 baseFormat.getSampleRate(),
                                                 false);
    din = AudioSystem.getAudioInputStream (decodedFormat, in);
  
    // Play now.
  
    try
      {
        rawPlay (decodedFormat, din);
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    catch (LineUnavailableException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
  
    // Now try to close the stream.
  
    try
      {
        in.close();
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
  }
  
  
  /**
   * Set the name of the player, which should correspond to the name of the 
   * player in the controller player map.
   * 
   * @param inName  The new name of the player.
   */
  
  public void
  setName (String inName)
  {
    name = inName;
  }
  
  
  /**
   * Set the paused status of this player.
   * 
   * This method will only have an effect if the player is running.
   * 
   * @param isPaused  Whether or not the player should be paused.
   */

  public void
  setPaused (boolean isPaused)
  {
    if (isRunning ())
      paused = isPaused;
  }
  
  /**
   * Start the player.
   */
  
  public void
  start ()
  {
    thread = new Thread (this);
    paused = false;
    thread.start ();
  }
   
  /**
   * Attempt to stop playback, if this player is currently playing.
   */
  
  public void
  stop ()
  {
    if (thread != null)
      { 
        Thread temp = thread;
        thread = null;
        temp.interrupt ();
        
        try
          {
            temp.join ();
          }
        catch (InterruptedException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace ();
          }
      }
  }
  
  /**
   * Stop the player and then request that the controller
   * unload the player.
   */
  public void
  unload ()
  {
    stop ();
    
    controller.dropPlayer (name);
  }
}
