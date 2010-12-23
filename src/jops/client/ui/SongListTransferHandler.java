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
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import jops.song.Song;


public class SongListTransferHandler extends TransferHandler
{
  /**
   * 
   */
  private static final long serialVersionUID = 7959723166721815808L;

  
  @Override
  public boolean
  canImport (TransferSupport support)
  {
    if (!support.isDataFlavorSupported (SongSelection.dataFlavor))
      return false;
    
    JList.DropLocation drop = (javax.swing.JList.DropLocation) support.getDropLocation ();
    
    if (drop.getIndex () > -1)
      return true;
    else
      return false;
  }
  
  
  @Override
  protected Transferable
  createTransferable (JComponent c)
  {
    if (c instanceof JList)
      {
        JList cj = (JList) c;
        
        if (cj.getSelectedIndex () > -1)
          return new SongSelection ((Song) cj.getModel ().getElementAt (cj.getSelectedIndex ()));
      }
    
    return null;
  }
  
  
  @Override
  protected void
  exportDone (JComponent c, Transferable t, int action)
  {
    if (action == MOVE && c instanceof JList)
      {
        JList cj = (JList) c;
        
        if (cj.getModel () instanceof DefaultListModel && cj.getSelectedIndex () > -1)
          {
            DefaultListModel model = (DefaultListModel) cj.getModel ();
            
            model.remove (cj.getSelectedIndex ());
          }
      }
  }
  
  
  @Override
  public int
  getSourceActions (JComponent c)
  {
    return COPY_OR_MOVE;
  }
  
  
  @Override
  public boolean
  importData (TransferSupport support)
  {
    if (!canImport (support))
      return false;
    
    Transferable t = support.getTransferable ();
    
    try
      {
        if (t.getTransferData (SongSelection.dataFlavor) instanceof Song)
          {
            Song song = (Song) t.getTransferData (SongSelection.dataFlavor);
        
            JList.DropLocation drop = (javax.swing.JList.DropLocation) support.getDropLocation ();
            Component c = support.getComponent ();
            
            if (c instanceof JList)
              {
                JList cj = (JList) c;
        
                if (cj.getModel () instanceof DefaultListModel && drop.getIndex () > -1)
                  {
                    // Store previous selection and bump it up if the new item is before it on the list.
                    int temp = cj.getSelectedIndex ();
                    
                    if (temp >= drop.getIndex ())
                      temp++;
                    
                    DefaultListModel model = (DefaultListModel) cj.getModel ();
            
                    model.add (drop.getIndex (), song);
                    
                    cj.setSelectedIndex (temp);
                    
                    return true;
                  }
              }
          }
      }           
    catch (UnsupportedFlavorException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    
    return false;
  }
}
