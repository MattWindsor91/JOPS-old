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

package jops.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jops.client.JopsClient;
import jops.server.JopsServer;
import jops.server.SpiPlayer;

/**
 * Implementor of the JOPS client-server protocol.
 * 
 * Note that the JOPS protocol is completely different from the BAPS 
 * protocol - one of the reasons why this project is more of a proof 
 * of concept than an actual replacement prototype.
 * 
 * The JOPS protocol as it is is a text-based protocol, consisting of 
 * several commands in lowercase (command to server) and uppercase
 * (response to client) forms.  The commands are followed by 
 * command-specific arguments, delimited by a delimiter character.
 * 
 * @author Matt Windsor
 *
 */

public class JopsProtocol
{ 
  private static final String DELIMITER = ";";
  
  // Protocol Commands
  
  /**
   * mx
   * 
   * Ask the server to report its possible outputs.
   */
  
  public static final String COMMAND_LIST_OUTPUTS = "mx";
 
  /**
   * MX output1 output2... outputN
   * 
   * Return to the client a list of possible outputs.
   */
  
  public static final String RESULT_LIST_OUTPUTS = "MX";
  
  
  /**
   * cp type mixer-index
   * 
   * Create a player of type type (types documented below) 
   * outputting to the output at index mixer-index in the server
   * mixer list (retrieved through mx).
   */
  
  public static final String COMMAND_CREATE_PLAYER = "cp";
  
  /**
   * CP player-name
   * 
   * Return to the client the name of the player that was just 
   * created.  This name will be used in future to refer to the 
   * player.
   */
  public static final String RESULT_CREATE_PLAYER = "CP";
  
  
  /**
   * @TODO IMPLEMENT
   * 
   * dp player-name
   * 
   * Delete the player with name player-name.
   */
  
  public static final String COMMAND_DELETE_PLAYER = "dp";
  
  /**
   * @TODO IMPLEMENT
   * 
   * DP
   * 
   * Confirm that the player has been deleted.
   */
  
  public static final String RESULT_DELETE_PLAYER = "DP";
  
  
  /**
   * @TODO decouple so client does not need to know filenames
   * 
   * cf player-name filename
   * 
   * Change the file loaded on the file-based player player-name to 
   * the file filename.
   */
  
  public static final String COMMAND_CHANGE_FILE = "cf";
  
  /**
   * CF
   * 
   * Confirm that the player's file has been changed.
   */
  
  public static final String RESULT_CHANGE_FILE = "CF";
  
  
  /**
   * st player-name
   * 
   * Start the player named player-name.
   */
  
  public static final String COMMAND_START = "st";
  
  /**
   * ST
   * 
   * Confirm that the player has been started.
   */
  
  public static final String RESULT_START = "ST";
  
  
  /**
   * sp player-name
   * 
   * Stop the player named player-name.
   */
  
  public static final String COMMAND_STOP = "sp";
  
  /**
   * SP
   * 
   * Confirm that the player has been stopped.
   */
  
  public static final String RESULT_STOP = "SP";
  
  
  /**
   * pa player-name is-paused
   * 
   * Set whether or not the player named player-name is paused.
   * is-paused should be either the constant JopsProtocol.TRUE
   * or the constant JopsProtocol.FALSE.
   */
  
  public static final String COMMAND_SETPAUSED = "pa";
  
  /**
   * PA
   * 
   * Confirm that the player is now (un)paused.
   */
  
  public static final String RESULT_SETPAUSED = "PA";

  
  /**
   * gt player-name
   * 
   * Get the type name of the player named player-name.
   */
  
  public static final String COMMAND_GET_PLAYER_TYPE = "gt";
  
  /**
   * GT player-type
   * 
   * Return to the client the type name of the player.
   */
  
  public static final String RESULT_GET_PLAYER_TYPE = "GT";
  
  
  /**
   * go player-name
   * 
   * Get the output name of the player named player-name.
   */
  
  public static final String COMMAND_GET_PLAYER_OUTPUT = "go";
  
  /**
   * GO player-output
   * 
   * Return to the client the output name of the player.
   */
  
  public static final String RESULT_GET_PLAYER_OUTPUT = "GO";

  
  // Player types
  
  /**
   * A SPI player.
   * 
   * SPI players output directly using the Java Sound API.
   */
  
  public static final String PLAYERTYPE_SPI = "SPI";
  
  
  // Boolean representation
  
  /**
   * Boolean true.
   */
  
  public static final String TRUE = "T";
  
  /**
   * Boolean false.
   */
  
  public static final String FALSE = "F";
  
  
  /**
   * The default port that the JOPS client and server communicate on.
   */
  
  public static final int DEFAULT_PORT = 4444;

  
  /**
   * Process the given input string as a command to the server.
   * @param input  The raw input string.
   * @param master  The server.
   * @return  A command-dependent string.
   */
  
  public String
  processInput (String input, JopsServer master)
  {
    if (input != null)
      {
        StringTokenizer st = new StringTokenizer (input, DELIMITER);
    
        String command = st.nextToken ();
    
        if (command.equals (COMMAND_CREATE_PLAYER))
          {
            String type = st.nextToken ();
            int mixerIndex = Integer.parseInt (st.nextToken ());
            
            if (type.equals (PLAYERTYPE_SPI))
              return (RESULT_CREATE_PLAYER 
                      + DELIMITER
                      + master.addPlayer (new SpiPlayer ("", master.getMixer (mixerIndex), master)));
          }
        else if (command.equals (COMMAND_LIST_OUTPUTS))
          {
            List<String> mixList = master.getMixerNames ();
            
            String out = RESULT_LIST_OUTPUTS + DELIMITER;
            
            for (String mixer : mixList)
              {
                out += mixer + DELIMITER;
              }
            
            return out;
          }
        else if (command.equals (COMMAND_GET_PLAYER_TYPE))
          {
            String playerName = st.nextToken ();
            
            String out = RESULT_GET_PLAYER_TYPE + DELIMITER + master.getPlayerType (playerName);
            return out;
          }
        else if (command.equals (COMMAND_GET_PLAYER_OUTPUT))
          {
            String playerName = st.nextToken ();
            
            String out = RESULT_GET_PLAYER_OUTPUT + DELIMITER + master.getPlayerOutput (playerName);
            return out;
          }
        else if (command.equals (COMMAND_CHANGE_FILE))
          {
            String playerName = st.nextToken ();
            String fileName = st.nextToken ();
            
            if (master.changeFile (playerName, fileName) == true)
              return RESULT_CHANGE_FILE;
          }
        else if (command.equals (COMMAND_START))
          {
            String playerName = st.nextToken ();
            
            if (master.start (playerName) == true)
              return RESULT_START;
          }
        else if (command.equals (COMMAND_STOP))
          {
            String playerName = st.nextToken ();
            
            if (master.stop (playerName) == true)
              return RESULT_STOP;
          }
        else if (command.equals (COMMAND_SETPAUSED))
          {
            String playerName = st.nextToken ();
            boolean isPaused = (st.nextToken ().equals (TRUE) ? true : false);
            
            if (master.setPaused (playerName, isPaused) == true)
              return RESULT_SETPAUSED;
          }
        }
    
    return "NAK";
  }

  
  /**
   * Process the given input string as a result from the server.
   * @param input  The raw input string.
   * @param jopsClient  The client.
   * @return  a command-dependent string.
   */
  
  public String
  processOutput (String input, JopsClient jopsClient)
  {
    if (input != null)
      {
        StringTokenizer st = new StringTokenizer (input, DELIMITER);
    
        String command = st.nextToken ();

        if (command.equals (RESULT_LIST_OUTPUTS))
          {
            List<String> out = new ArrayList<String> ();
            
            while (st.hasMoreTokens ())
              {
                out.add (st.nextToken ());
              }
            
            jopsClient.setMixers (out);
          }
        else if (command.equals (RESULT_CREATE_PLAYER))
          jopsClient.registerPlayer (st.nextToken ());
        // Pass-through commands.
        // All the protocol handler should do is pass the next token as output.
        else if (command.equals (RESULT_GET_PLAYER_TYPE)
                 || command.equals (RESULT_GET_PLAYER_OUTPUT))
          return st.nextToken ();
      }
    return input;
  }
  
  
  /**
   * Assemble a JOPS command to be sent over the network.
   * @param commandWord  The command word (one of the constants above)
   * @param arguments  A list of arguments to give to the command.
   * @return  The composed command, ready to send.
   */
  
  public String
  assembleCommand (String commandWord, String... arguments)
  {
    String outString = commandWord;
    
    for (String arg : arguments)
      outString += DELIMITER + arg;
    
    return outString;
  }
}
