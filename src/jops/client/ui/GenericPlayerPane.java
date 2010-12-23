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

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;

import jops.client.PlayerHook;


/**
 * A user interface pane to display controls and information for a player unit.
 * 
 * @author Matt Windsor
 *
 */

public class GenericPlayerPane extends JPanel implements PlayerPane
{
  private static final long serialVersionUID = 2397881702131121435L;  
  private PlayerHook hook;
  private String playerName;
  private StartStopDeck startStopPane;
  
  
  /**
   * Create a PlayerPane to expose controls for the given player hook.
   *
   * @param hook  The hook that the PlayerPane should control.
   */
  
  public
  GenericPlayerPane (PlayerHook hook)
  {
    super ();
    
    setPlayerName (hook.getPlayerName ());
    setHook (hook);
    
    setStartStopPane (new StartStopDeck (this));
  }
  
  public void
  editPlayer ()
  {
    // TODO: do something!
  }
  
  
  /**
   * @return the hook
   */
  
  public PlayerHook
  getHook ()
  {
    return hook;
  }

  
  /**
   * Retrieve the player that the PlayerPane is controlling.
   * 
   * @return the name of the player.
   */
  
  public String
  getPlayerName ()
  {
    return playerName;
  }


  /**
   * Initialise the generic pane footer - the part of the pane that appears 
   * before player type-specific controls - and perform final user interface setup. 
   */
  
  public void
  initFooter ()
  {
    JSeparator typeSpecSep2 = new JSeparator ();  
    add (typeSpecSep2);
    add (getStartStopPane());
  }

  /** 
   * Initialise the generic pane header - the part of the pane that appears 
   * before player type-specific controls - and perform initial user interface setup.
   */
  
  public void
  initHeader ()
  {  
    setLayout (new BoxLayout (this, BoxLayout.PAGE_AXIS));

    // Composition
    
    Border outerBorder = BorderFactory.createEtchedBorder ();
    Border innerBorder = BorderFactory.createEmptyBorder (5, 5, 5, 5);    
    
    setBorder (BorderFactory.createCompoundBorder (outerBorder, 
                                                   innerBorder));  
    
    final JPanel toFromPanel = new JPanel (new GridLayout (2, 1));
    
    JLabel typeName = new JLabel ("To: " + getHook().getPlayerOutput ());
    JLabel mixerName = new JLabel ("From: " + playerName);
    
    toFromPanel.add (typeName);
    toFromPanel.add (mixerName);

    add (toFromPanel);
    
    JSeparator typeSpecSep = new JSeparator ();
    
    add (typeSpecSep);
  }

  
  /**
   * @param hook the hook to set
   */
  
  public void
  setHook (PlayerHook hook)
  {
    this.hook = hook;
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
   * Unload the player connected to this player pane, deleting the pane.
   */
  
  public void 
  unloadPlayer ()
  {
    getHook().dropPlayer ();
  }

  public void setStartStopPane (StartStopDeck startStopPane)
  {
      this.startStopPane = startStopPane;
  }

  public StartStopDeck getStartStopPane ()
  {
      return startStopPane;
  }
}