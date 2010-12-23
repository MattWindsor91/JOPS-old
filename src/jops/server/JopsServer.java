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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

import jops.protocol.JopsProtocol;

/**
 * The JOPS server unit.
 * 
 * The JOPS server handles the actual playback of sound, and is 
 * controlled by a detached frontend (the JOPS client) which provides 
 * users with a GUI to control the server with.
 * 
 * @author Matt Windsor
 *
 */

public class JopsServer
{
  /**
   * Main method for running the JopsClient as an application.
   * 
   * @param args  Command-line arguments (presently ignored).
   */
  
  public static void
  main (String[] args)
  {
    // TODO Auto-generated method stub
    JopsServer js = new JopsServer ();
    js.run ();
  }
  
  private List<Mixer.Info> mixers;
  private Map<String, Player> players;
  private int currentPlayerIndex;
  private Socket clientSocket;

  
  /**
   * Get the available input and output lines from the audio system. 
   */
  
  public void
  acquireMixers ()
  {
    Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo ();
    
    for (Mixer.Info i : mixerInfo)
      {
        mixers.add (i);
      }
  }

  
  /**
   * Add a player engine to the player rack.
   *
   * @param player  The player object to add.
   * 
   * @return  The name of the new player.
   */
  
  public String
  addPlayer (Player player)
  {
    if (player != null)  
      {
        String nameString = player.getTypeName () + currentPlayerIndex;
        currentPlayerIndex++;
        
        player.setName (nameString);
        players.put (nameString, player);
        
        return nameString;
      }
    
    return null;
  }
  
  public boolean
  changeFile (String playerName, String fileName)
  {
    if (players.get (playerName) instanceof FilePlayer)
      {
        ((FilePlayer) players.get (playerName)).changeFile (fileName);
        return true;
      }
    
    return false;
  }
  
  public void
  dropPlayer (String name)
  {
    // TODO Auto-generated method stub
    players.remove (name);    
  }


  public Mixer.Info
  getMixer (int i)
  {
    return mixers.get (i);
  }


  public List<String>
  getMixerNames ()
  {
     List<String> names = new ArrayList<String> ();
     
     for (Mixer.Info i : mixers)
       {
         names.add (i.toString ());
       }
     
     return names;
  }
  
  public String
  getPlayerOutput (String name)
  {
    return players.get (name).getMixerName ();
  }


  public String
  getPlayerType (String name)
  {
    return players.get (name).getTypeName ();
  }


  public void
  run ()
  {
    mixers = new ArrayList<Mixer.Info> ();
    players = new HashMap<String, Player> ();
    
    ServerSocket serverSocket = null;
    
    try
      {
        serverSocket = new ServerSocket (JopsProtocol.DEFAULT_PORT);
      }
    catch (IOException e)
      {
        System.out.println ("Could not listen on port " + JopsProtocol.DEFAULT_PORT);
        System.exit (-1);
      }
    
    acquireMixers ();
    
    boolean running = true;
    
    while (running)
      {
        System.out.println ("JOPS waiting for client");
    
        try
        {
          clientSocket = serverSocket.accept ();
        }
        catch (IOException e)
        {
          clientSocket = null;
          System.out.println ("Could not accept on port 4444");
          System.exit (-1);
        }    
    
        System.out.println ("JOPS ready");
    
        PrintWriter out = null;
        BufferedReader in = null;
    
        try
        {
          out = new PrintWriter (clientSocket.getOutputStream(), true);
          in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ()));
        }
        catch (IOException e1)
        {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
    
        String inputLine, outputLine;

        //initiate conversation with client
        JopsProtocol jp = new JopsProtocol ();
    
        try
        {
          inputLine = in.readLine ();
        }
        catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
          inputLine = null;
        }
    
        while (inputLine != null)
          { 
            System.out.println ("<<< " + inputLine);
            outputLine = jp.processInput (inputLine, this);
            System.out.println (">>> " + outputLine);
            out.println(outputLine);
            if (outputLine.equals("Bye."))
              inputLine = null;
            else
              {
                try
                {
                  inputLine = in.readLine ();
                }
                catch (IOException e)
                {
                  inputLine = null;
                }
              }
          }
        
        try
          {
            in.close ();
            out.close ();
            clientSocket.close ();
          }
        catch (IOException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        
        in = null;
        out = null;
        clientSocket = null;
      }
  }
  
  public boolean
  setPaused (String playerName, boolean isPaused)
  {
    if (players.containsKey (playerName))
      {
        players.get (playerName).setPaused (isPaused);
        return true;
      }
    return false;
  }

  
  /**
   * Start a player in the player rack.
   * 
   * @param nameString  The name of the player object to start.
   */
  
  public boolean
  start (String nameString)
  {
    if (players.containsKey (nameString))
      {
        players.get (nameString).start ();
        return true;
      }
    return false;
  }


  /**
   * Stop a player in the player rack.
   * 
   * @param nameString  The name of the player object to stop.
   */
  
  public boolean
  stop (String nameString)
  {
    if (players.containsKey (nameString))
      {
        players.get (nameString).stop ();
        return true;
      }
    return false;
  }
}
