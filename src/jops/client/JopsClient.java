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

package jops.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import jops.client.ui.UserInterface;
import jops.protocol.JopsProtocol;

/**
 * Testing class for potential BAPS replacement.
 * 
 * The purpose of this class is to experiment with sound code to interface 
 * with the URY mixing desk and play various forms of media (FLAC, MP3, 
 * line-in).
 * 
 * @author Matt Windsor
 *
 */

public class
JopsClient
{
  /**
   * Main method for running the JopsClient as an application.
   * 
   * @param args  Command-line arguments (presently ignored).
   */
  
  public static void
  main (String args[])
  {
    JopsClient bt = new JopsClient ();
    bt.run ();
  }
  
  private List<String> mixers;

  private UserInterface ui;
  private Socket servSocket;
  private PrintWriter out;
  
  private BufferedReader in;
  
  
  /**
   * Create a client object.
   */
  
  public
  JopsClient ()
  {
    mixers = null;
    ui = new UserInterface (this);
  }
  
  
  /**
   * Get the available input and output lines from the audio system. 
   */
  
  public void
  acquireMixers ()
  {
    communicate (JopsProtocol.COMMAND_ACQUIRE_MIXERS);
  }

  
  public void
  addPlayer (String type, int selectedIndex)
  {
    communicate (JopsProtocol.COMMAND_CREATE_PLAYER + JopsProtocol.DELIMITER
                 + type + JopsProtocol.DELIMITER + selectedIndex);
  }
  
  
  /**
   * Communicate with the JopsServer.
   * 
   * @param cin  The string to send to the server, in protocol format.
   * 
   * @return  The output from the protocol.  Usually this will be the 
   *          returned result from the server; for certain operations 
   *          this will be different.  Some operations incur 
   *          side-effects not evident from the return value.
   */
  
  public String
  communicate (String cin)
  {
    // No server
    if (in == null || out == null)
      return "NAK";
    
    JopsProtocol jp = new JopsProtocol ();
    out.println (cin);
    
    String fromServer = null;
    
    while (fromServer == null)
      {
        try
          {
            fromServer = in.readLine ();
          }
        catch (IOException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace ();
            return null;
          }
      }
    
    return jp.processOutput (fromServer, this);
  }
  
  
  /**
   * Connect to a JOPS server on the given address and port.
   * 
   * @param serverHost  The address (hostname or IP) of the JOPS server to connect to.
   * @param port  The port on which to connect to.
   * @return  true if the connection was successful, false otherwise.
   */
  
  public boolean
  connectToServer (String serverHost, int port)
  {
    try
      {
        servSocket = new Socket (serverHost, port);
        out = new PrintWriter (servSocket.getOutputStream (), true);
        in = new BufferedReader (new InputStreamReader (servSocket.getInputStream ()));
      }
    catch (UnknownHostException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
        return false;
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
        System.out.println ("Couldn't connect to JOPS server.");
        return false;
      }
    
    acquireMixers ();
    return true;
  }
 

  public String[] getMixerList ()
  {
    String str[] = new String[mixers.size ()];
    return mixers.toArray (str);
  }
  

  /**
   * Replace the player of a given name with another player.
   * 
   * @param name    The name of the player to remove.
   * @param player  The name of the player to insert.
   *
  
  public void
  replacePlayer (String name, Player player)
  {
    dropPlayer (name);
    addPlayer (player);
  }
*/


  /**
   * Return the type of the given player.
   * @param playerName  The name of the player.
   * @return  A string representing the class of the player.
   */

  
  /**
   * Return the output of the given player.
   * @param playerName  The name of the player.
   * @return  A string representing the output (mixer) of the player.
   */


  /**
   * Relay an action to a Player.
   * 
   * TODO: Replace this with the actual BAPS protocol.
   * 
   * @param name    The name of the Player.
   * @param opcode  The action to send to the Player
   * @param param   The parameter to send to the Player
   */


  /**
   * Initialise the user interface.
   */
  
  public void
  initUI ()
  {
    ui.initialise ();
  }


  /**
   * Register the player with the given name with the client, creating 
   * a hook and controller pane.
   * 
   * @param name  the name of the player to register.
   */
  
  public void
  registerPlayer (String name)
  {
    ui.addPlayerPane (new PlayerHook (name, this));
  }


  public void
  run ()
  {
    initUI ();
  }


  /**
   * Set the mixers list.
   * @param out  The new mixers list.
   */
  
  public void
  setMixers (List<String> out)
  {
    mixers = out;
  }
}
