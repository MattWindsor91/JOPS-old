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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jops.client.PlayerHook;
import jops.song.SongManager;



/**
 * A user interface pane to display controls and information for a player unit.
 * 
 * @author Matt Windsor
 *
 */

public class FilePlayerPane extends GenericPlayerPane
{
  private static final long serialVersionUID = 2397881702131121435L;
  
  private JList fileList;
  
  /**
   * Create a PlayerPane to expose controls for the given player.
   *
   * @param hook  The PlayerHook used to communicate with the player.
   */
  
  public
  FilePlayerPane (PlayerHook hook)
  {
    super (hook);

    initHeader ();
    
    JPanel filePlayerLabelPanel = new JPanel (new GridLayout (1, 1));
    JLabel filePlayerLabel = new JLabel ("Available music:");
    filePlayerLabel.setAlignmentX (Component.RIGHT_ALIGNMENT);
        
    filePlayerLabelPanel.add (filePlayerLabel);
    add (filePlayerLabelPanel);
        
    SongManager sm = new SongManager (); 
        
    fileList = new GraphicalSongList (sm.getSongList (), this);

        
    JScrollPane listScroller = new JScrollPane (fileList);
    listScroller.setPreferredSize (new Dimension (250, 80));
    add (listScroller);
  
    initFooter ();
  }

  
  public void
  changeFile (String filename)
  {
    getHook ().changeFile (filename);
  }

  
  public void
  handleSongChange ()
  {
    getStartStopPane ().handleSongChange ();
  }
}