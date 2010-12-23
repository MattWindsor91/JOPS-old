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
 * A dummy metadata reader that returns "Unknown" for all queries.
 * 
 * @author Matt Windsor
 *
 */
public class NullMetadataReader implements MetadataReader
{

  /**
   * @see jops.meta.MetadataReader#getAlbum()
   */
  public String
  getAlbum ()
  {
    return "Unknown";
  }

  
  /**
   * @see jops.meta.MetadataReader#getAlbum()
   */
  
  public String
  getArtist ()
  {
    return "Unknown";
  }

  
  /**
   * @see jops.meta.MetadataReader#getDuration()
   */
  
  public Duration
  getDuration ()
  {
    return new Duration (0L);
  }
  
  /**
   * @see jops.meta.MetadataReader#getTitle()
   */
  
  public String
  getTitle ()
  {
    return "Unknown";
  }
    
}
