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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jops.client.JopsClient;
import jops.client.PlayerHook;

/**
 * The parent user interface frame for the graphical JOPS client.
 * 
 * @author Matt Windsor
 */

public class UserInterface extends JFrame
{
  /**
   * 
   */
  private static final long serialVersionUID = -5128214701957219595L;
  
  private JopsClient master;
  private JPanel playerRack;
  private JScrollPane scrollPane;
  private HashMap <String, GenericPlayerPane> playerPanes;
  
  /**
   * Create the user interface and show it on screen.
   */
 
  public
  UserInterface (JopsClient inMaster)
  {
    super ("JOPS (proof-of-concept Java-Originated Presentation System)");
    playerPanes = new HashMap <String, GenericPlayerPane> ();
    master = inMaster;
  }
  
  
  /**
   * Create a player rack node.
   */
  
  public void
  addPlayerPane (PlayerHook hook)
  { 
    GenericPlayerPane playerPane = PlayerPaneFactory.makePlayerPane (hook);
    System.out.println (playerPane);
    playerRack.add (playerPane);
    System.out.println ("sauce");
    playerPanes.put (hook.getPlayerName (), playerPane);
 
    playerRack.validate ();
    playerRack.repaint ();
    
    scrollPane.validate ();
  }

  
  /**
   * Delete a player rack node.
   * 
   * @param playerPane  The node to delete.
   */

  
  public void
  dropPlayerPane (GenericPlayerPane playerPane)
  {
    playerRack.remove (playerPane);
    playerPanes.remove (playerPane.getName ());
    
    playerRack.validate ();
    playerRack.repaint ();
    
    scrollPane.validate ();
  }
  
  
  /**
   * Delete a player rack node given its name.
   * 
   * @param name  The name of the player whose rack node should be deleted.
   */
  
  public void
  dropPlayerPane (String name)
  {
    dropPlayerPane (playerPanes.get (name));
  }


  /**
   * Get the mixer controller underneath the user interface.
   * @return  The mixer controller.
   */
  
  public JopsClient
  getController ()
  {
    return master;
  }

  
  /** 
   * Initialise the GUI, creating the main window etc.
   */

  public void
  initialise ()
  {
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    
    Container contentPane = getContentPane ();
    contentPane.setLayout (new BoxLayout (contentPane, BoxLayout.PAGE_AXIS));
    
    playerRack = new JPanel ();
    playerRack.setLayout (new BoxLayout (playerRack, BoxLayout.LINE_AXIS));   
    
    scrollPane = new JScrollPane (playerRack);
       
    scrollPane.setPreferredSize (new Dimension (600, 220));
    
    JPanel bapsControls = new JPanel ();
    bapsControls.setLayout (new BoxLayout (bapsControls, BoxLayout.LINE_AXIS));
    
    JButton addPlayerButton = new JButton ("Add Player");
    
    final UserInterface thisFrame = this;
    
    addPlayerButton.addActionListener (new ActionListener ()
    {
      public void
      actionPerformed (ActionEvent event)
      {
        PlayerDialog dialog = new PlayerDialog (thisFrame, null);
        dialog.setVisible (true);
      }
    });    
    
    bapsControls.add (addPlayerButton);
    
    contentPane.add (scrollPane);
    contentPane.add (bapsControls);
    
    pack ();
    setVisible (true);
    
    ServerDialog sd = new ServerDialog (this);
    sd.setVisible (true);
  }
}
