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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import jops.song.Song;


public class SongSelection implements Transferable
{
  private Song song;
  public static final DataFlavor dataFlavor = new DataFlavor (Song.class, "Song");  
  
  public
  SongSelection (Song inSong)
  {
    song = inSong;
  }
  
  
  @Override
  public Object 
  getTransferData (DataFlavor arg0) throws UnsupportedFlavorException, IOException
  {
    if (arg0.equals (dataFlavor))
      return song;
    
    return null;
  }

  @Override
  public
  DataFlavor[] getTransferDataFlavors ()
  {
    DataFlavor [] ret = {dataFlavor};
    return ret;
  }

  @Override
  public boolean
  isDataFlavorSupported (DataFlavor arg0)
  {
    return (arg0.equals (dataFlavor));
  }
}
