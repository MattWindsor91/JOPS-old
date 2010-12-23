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

import jops.protocol.JopsProtocol;

/**
 *  A class that represents a Player to the JOPS frontend, handling requests
 *  for information about that Player from the frontend.
 * 
 *  @author Matt Windsor
 */

public class PlayerHook
{
  private String playerName;
  private JopsClient controller;
  
  public
  PlayerHook (String playerName, JopsClient controller)
  {
    this.setPlayerName (playerName);
    this.controller = controller;
  }

  
  /**
   * Attempt to change the file that the Player is to play.
   * 
   * @param filename  The new filename to play.
   */
  
  public void
  changeFile (String filename)
  {
    controller.communicate (JopsProtocol.COMMAND_CHANGE_FILE + JopsProtocol.DELIMITER 
                            + getPlayerName () + JopsProtocol.DELIMITER + filename);
  }

  
  /**
   * Attempt to drop the player.
   */
  
  public void
  dropPlayer ()
  {
    // TODO Auto-generated method stub
    controller.communicate (JopsProtocol.COMMAND_DELETE_PLAYER + JopsProtocol.DELIMITER
                            + getPlayerName ());
  }

  
  /**
   * @return the playerName
   */
  public String
  getPlayerName ()
  {
      return playerName;
  }


  /**
   * Return the string identifying the output of the player.
   * @return  the string identifying the output of the player.
   */

  public String
  getPlayerOutput ()
  {
    return controller.communicate (JopsProtocol.COMMAND_GET_PLAYER_OUTPUT + JopsProtocol.DELIMITER
                                   + getPlayerName ());
  }

  
  /**
   * @return  the type string of the Player.
   */
  
  
  public String
  getPlayerType ()
  {
    return controller.communicate (JopsProtocol.COMMAND_GET_PLAYER_TYPE + JopsProtocol.DELIMITER
                                   + getPlayerName ());
  }

  
  /**
   * Attempt to set the player's paused attribute.
   * @param isPaused  Whether or not the player should be paused.
   */
  
  public void
  setPaused (boolean isPaused)
  {
    controller.communicate (JopsProtocol.COMMAND_SETPAUSED + JopsProtocol.DELIMITER 
                            + getPlayerName () + JopsProtocol.DELIMITER + (isPaused ? "T" : "F"));
    
  }

  
  /**
   * @param playerName the playerName to set
   */
  public void
  setPlayerName (String playerName)
  {
      this.playerName = playerName;
  }

  
  /**
   * Attempt to start the player.
   */
  
  public void
  start ()
  {
    controller.communicate (JopsProtocol.COMMAND_START + JopsProtocol.DELIMITER + getPlayerName ());
  }

  
  /**
   * Attempt to stop the player.
   */
  
  public void
  stop ()
  {
    controller.communicate (JopsProtocol.COMMAND_STOP + JopsProtocol.DELIMITER + getPlayerName ());
  }

}
