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

package jops.meta;

/**
 * Factory for metadata readers.
 * 
 * @author Matt Windsor
 *
 */

public class
JopsMetadata
{
  /**
   * Retrieve a metadata reader suitable for the given filename.
   * 
   * @param filename  The filename of the song to create a reader for.
   * 
   * @return  A MetadataReader which can be queried for metadata concerning 
   * the given song.  
   * 
   * If there are no suitable metadata readers available, a NullMetadataReader
   * will be returned.
   */
  
  public static MetadataReader
  getMetadataReader (String filename)
  {
    MetadataReader meta = null;
    
    if (filename.endsWith (".flac"))
        meta = new FlacMetadataReader (filename); 
    else
        meta = new SpiMetadataReader (filename);
    
    return meta;
  }
}
