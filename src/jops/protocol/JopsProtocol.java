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
 * @author Matt Windsor
 *
 */

public class JopsProtocol
{
  public static final String DELIMITER = ";";
  
  public static final String COMMAND_ACQUIRE_MIXERS = "mx";
  public static final String RESULT_ACQUIRE_MIXERS = "MX";
  
  public static final String COMMAND_CREATE_PLAYER = "cp";
  public static final String RESULT_CREATE_PLAYER = "CP";
  
  public static final String COMMAND_DELETE_PLAYER = "dp";
  public static final String RESULT_DELETE_PLAYER = "DP";
  
  public static final String COMMAND_CHANGE_FILE = "cf";
  public static final String RESULT_CHANGE_FILE = "CF";
  
  public static final String COMMAND_START = "st";
  public static final String RESULT_START = "ST";
  
  public static final String COMMAND_STOP = "sp";
  public static final String RESULT_STOP = "SP";
  
  public static final String COMMAND_SETPAUSED = "pa";
  public static final String RESULT_SETPAUSED = "PA";

  public static final String COMMAND_GET_PLAYER_TYPE = "gt";
  public static final String RESULT_GET_PLAYER_TYPE = "GT";
  
  public static final String COMMAND_GET_PLAYER_OUTPUT = "go";
  public static final String RESULT_GET_PLAYER_OUTPUT = "GO";

  public static final int DEFAULT_PORT = 4444;

  
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
            
            if (type.equals ("SPI"))
              return (RESULT_CREATE_PLAYER 
                      + DELIMITER
                      + master.addPlayer (new SpiPlayer ("", master.getMixer (mixerIndex), master)));
          }
        else if (command.equals (COMMAND_ACQUIRE_MIXERS))
          {
            List<String> mixList = master.getMixerNames ();
            
            String out = RESULT_ACQUIRE_MIXERS + DELIMITER;
            
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
            boolean isPaused = (st.nextToken ().equals ("T") ? true : false);
            
            if (master.setPaused (playerName, isPaused) == true)
              return RESULT_SETPAUSED;
          }
        }
    
    return "NAK";
  }

  public String
  processOutput (String input, JopsClient jopsClient)
  {
    if (input != null)
      {
        StringTokenizer st = new StringTokenizer (input, DELIMITER);
    
        String command = st.nextToken ();

        if (command.equals (RESULT_ACQUIRE_MIXERS))
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
}
