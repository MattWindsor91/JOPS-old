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

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jops.song.Song;


public class GraphicalSongList extends JList implements ListSelectionListener
{
  private static final long serialVersionUID = 8271253371823577761L;
  private FilePlayerPane parent;
  
  public
  GraphicalSongList (List<Song> inSongs, FilePlayerPane inParent)
  {
    super ();
    
    parent = inParent;
    
    DefaultListModel model = new DefaultListModel ();
    
    for (Song song: inSongs)
      model.addElement (song);
    
    setModel (model);
    setSelectionMode (ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    setLayoutOrientation (JList.VERTICAL);
    setVisibleRowCount (-1);

    setDragEnabled (true);
    setDropMode (DropMode.INSERT);
    
    setTransferHandler (new SongListTransferHandler ());
    
    addListSelectionListener (this);
    
    setSelectedIndex (0);
  }
  
  /**
   * Listening function for file list changes.
   * 
   * @param event  The list selection event.
   */
  
  public void
  valueChanged (ListSelectionEvent event)
  {
    parent.handleSongChange ();
    
    if (getSelectedIndex () > -1)
      parent.changeFile (((Song) getModel ().getElementAt (getSelectedIndex ())).getFilename ());
  }
}
