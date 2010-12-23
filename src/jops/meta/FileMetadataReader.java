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
 * An abstract class implementing base functionality for file-based metadata readers.
 * 
 * @author Matt Windsor
 *
 */
public abstract class FileMetadataReader
{
  private String fileName;
  
  private String artist;
  private String album;
  private String title;
  private Duration duration;
  
  
  public
  FileMetadataReader (String inFileName)
  {
    fileName = inFileName;
    setDuration (null);
    setArtist ("Unknown");
    
  }
  
  /**
   * Get the album of the song, or "Unknown" if it is not available.
   * 
   * @return  the album of the song.
   */

  public String 
  getAlbum ()
  {
    return album;
  }
  

  
  /**
   * Get the artist of the song, or "Unknown" if it is not available.
   * 
   * @return  the artist of the song.
   */

  public String 
  getArtist ()
  {
    return artist;
  }

  
  public Duration
  getDuration ()
  {
    return duration;
  }
  
  
  public String
  getFileName ()
  {
    return fileName;
  }
  
  
  /**
   * Get the title of the song, or "Unknown" if it is not available.
   * 
   * @return  the title of the song.
   */

  public String 
  getTitle ()
  {
    return title;
  }

  public void setAlbum (String album)
  {
      this.album = album;
  }

  public void setArtist (String artist)
  {
      this.artist = artist;
  }

  public void setDuration (Duration duration)
  {
      this.duration = duration;
  }

  public void setTitle (String title)
  {
      this.title = title;
  }
}
