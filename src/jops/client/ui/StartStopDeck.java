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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Matt Windsor
 *
 */
public class StartStopDeck extends JPanel
{

  /**
   * 
   */
  private static final long serialVersionUID = -3218255534536646336L;

  GenericPlayerPane master;
  
  final JButton startButton;
  final JButton stopButton;
  final JButton deleteButton;
  final JButton editButton;
  
  /**
   * Create a start/stop/pause control deck.
   * 
   * @param inMaster  The player pane to which the control deck belongs to.
   */
  
  public StartStopDeck (GenericPlayerPane inMaster)
  {
    super (new GridLayout (2, 2));
    
    master = inMaster;
    
    int numberofRows = 2;
    
    // Add another row of buttons for Add/Delete if the player pane is a file management one.
    
    if (inMaster instanceof FilePlayerPane)
      numberofRows++;
    
    // Start and stop buttons
    
    startButton = new JButton ("Start");
    stopButton = new JButton ("Stop");
    stopButton.setEnabled (false);
    
    startButton.addActionListener (new ActionListener ()
    {
      public void
      actionPerformed (ActionEvent event)
      {
        if (startButton.getText () == "Start")
          {
            // Button is "Start" and Stop/Edit disabled
            // (Player stopped)
            if (stopButton.isEnabled () == false)
              {
                master.getHook ().start ();
                stopButton.setEnabled (true);

                
                // Cannot edit/delete started player.
                
                editButton.setEnabled (false);
                deleteButton.setEnabled (false);
              }
            // Button is "Start" and Stop enabled
            // (Player started but paused)
            else
              {
                master.getHook ().setPaused (false); 
              }
            
            startButton.setText ("Pause");
          }
        else
          {
            // Button is "Pause"
            // (Player started and unpaused)
            master.getHook ().setPaused (true);
            startButton.setText ("Start");
          }
      }
    });

    stopButton.addActionListener (new ActionListener ()
    {
      public void
      actionPerformed (ActionEvent event)
      {
        master.getHook ().stop ();
        startButton.setText ("Start");
        stopButton.setEnabled (false);
        
        // Can now edit/delete stopped player.
        
        editButton.setEnabled (true);
        deleteButton.setEnabled (true);
      }
    });
    
    add (startButton);
    add (stopButton);
    
    // Player management buttons
    
    editButton = new JButton ("Edit Player...");
    
    editButton.addActionListener (new ActionListener ()
    {
      public void
      actionPerformed (ActionEvent event)
      {
        master.editPlayer ();
      }
    });    
    
    deleteButton = new JButton ("Delete Player");
    
    deleteButton.addActionListener (new ActionListener ()
    {
      public void
      actionPerformed (ActionEvent event)
      {
        master.unloadPlayer ();
      }
    });
    
    add (editButton);
    add (deleteButton);
  }

  /**
   * Reset the control deck after the song has been changed.
   * 
   * If the song was paused or stopped, the new song will load stopped, and 
   * thus the control deck will be reset to show the stopped song state.
   * 
   * If the song was playing, the new song will load playing.
   */
  
  public void
  handleSongChange ()
  {
    // Button is "Pause"
    // (Player started and unpaused)
    if (startButton.getText () == "Pause")
      {
        // No change necessary.
      }
    else 
      {
        // Set to the stopped song state
        // (Stop greyed out, Play button reads "Start")
        startButton.setText ("Start");
        stopButton.setEnabled (false);
      }
  }
}
