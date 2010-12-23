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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.FrameListener;
import org.kc7bfi.jflac.frame.Frame;
import org.kc7bfi.jflac.metadata.Metadata;
import org.kc7bfi.jflac.metadata.VorbisComment;

/**
 * A reader for FLAC metadata.
 * 
 * Based on JFLAC sample code.
 * 
 * @author Matt Windsor
 *
 */

public class FlacMetadataReader extends FileMetadataReader implements MetadataReader, FrameListener
{
  private String fileName;
  
  private FileInputStream is;
  
  FLACDecoder decoder;
  
  /**
   * Create a new FLAC metadata reader.
   * 
   * @param inFileName  The filename to be read from.
   */
  
  public
  FlacMetadataReader (String inFileName)
  {
    super (inFileName);
    
    fileName = inFileName;
    
    is = null;
    try
      {
        is = new FileInputStream (fileName);
      }
    catch (FileNotFoundException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    decoder = new FLACDecoder (is);
    decoder.addFrameListener (this);
    try
      {
        decoder.decode ();
      }
    catch (IOException e)
      {
        // Ignore IO exceptions - one will be raised by terminating the 
        // stream after gaining metadata.
      }
  }
  
  
  /**
   * Get the first Vorbis comment with the given tag, or "Unknown" if none exist.
   * 
   * @param inComment  The VorbisComment object from which to extract the comment.
   * @param tag  The string tag to look up.
   */
  
  private String
  getComment (VorbisComment inComment, String tag)
  { 
    String[] commentArray = null;
    
    try
      {
        commentArray = inComment.getCommentByName (tag);
      }
    catch (NullPointerException e)
      {
        // This means there is no comment by this name, so ignore it.
        commentArray = null;
      }
    
    if (commentArray != null && commentArray.length >= 1)
      return commentArray[0];
    else    
      return "Unknown";
  }


  /**
   * Stub for error processing.
   * 
   * @param msg  The error message.
   */
  
  public void
  processError (String msg)
  {
    // TODO Auto-generated method stub
    
  }

  
  
  /**
   * Stub for frame processing.
   * 
   * This serves to halt the decoder once it reaches actual FLAC frames 
   * (which we assume to be stored after all Vorbis comments).
   * 
   * @param frame  The frame to (not) process.
   */
  
  public void
  processFrame (Frame frame)
  {
    try
      {
        is.close ();
      }
    catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }
  
  
  /**
   * Process the FLAC metadata, extracting artist, album and title information if available.
   * 
   * @param metadata  A metadata block.
   */

  public void
  processMetadata (Metadata metadata)
  {
    if (metadata instanceof VorbisComment)
      {
        VorbisComment vcom = (VorbisComment) metadata;
        
        setArtist (getComment (vcom, "ARTIST"));
        setAlbum (getComment (vcom, "ALBUM"));
        setTitle (getComment (vcom, "TITLE"));
      }
  }
}
