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

package jops.client.ui;

import jops.client.PlayerHook;

/**
 * Factory for player panes.
 * 
 * @author Matt Windsor
 *
 */

public class PlayerPaneFactory
{
  /**
   * Make a PlayerPane suitable for the given player.
   * 
   * @param hook  The PlayerHook representing the player to connect to.
   * 
   * @return a PlayerPane.
   */
  
  static public GenericPlayerPane
  makePlayerPane (PlayerHook hook)
  {
    System.out.println (hook.getPlayerType ());
    if (hook.getPlayerType ().equals ("SPI-based file player"))
      return new FilePlayerPane (hook);
    else
      return new GenericPlayerPane (hook);
  }
}
