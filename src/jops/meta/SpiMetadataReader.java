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

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;

/**
 * A metadata reader for extracting ID3 tags.
 * 
 * @author Matt Windsor
 *
 */
public class SpiMetadataReader extends FileMetadataReader implements MetadataReader
{ 
  
  /**
   * Create a new FLAC metadata reader.
   * 
   * Uses code from the Oracle Java forums,
   * http://192.9.162.102/thread.jspa?messageID=4502993#4502993
   * 
   * @param inFileName  The filename to be read from.
   */
  
  public
  SpiMetadataReader (String inFileName)
  {
    super (inFileName);
    
    File file = new File (inFileName);
    
    AudioFileFormat base = null;
    
    try
      {
        base = AudioSystem.getAudioFileFormat (file);
      }
    catch (UnsupportedAudioFileException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    
    if (base instanceof TAudioFileFormat)
      {
        Map<?, ?> properties = base.properties ();
        
        setTitle ((String) properties.get ("title"));
        setAlbum ((String) properties.get ("album"));
        setArtist ((String) properties.get ("author"));
        setDuration (new Duration ((Long) properties.get ("duration")));
      }
 
  }
}
