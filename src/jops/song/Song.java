/**
 * 
 */
package jops.song;
import java.io.Serializable;

import jops.meta.JopsMetadata;
import jops.meta.MetadataReader;


/**
 * Information representing one song in the song library.
 * 
 * @author Matt Windsor
 *
 */
public class Song implements Serializable
{
  /**
   * 
   */
  
  private static final long serialVersionUID = -4321793987296117808L;

  private String title;
  private String artist;
  private String album;
  private String filename;
  
  
  /**
   * Create a new song record, retrieving the metadata from a metadata reader 
   * (if possible).
   * 
   * @param inFilename  The file-name of the song.
   */
  
  public
  Song (String inFilename)
  {
    title = "Unknown";
    artist = "Unknown";
    album = "Unknown";
    filename = inFilename;

    
    MetadataReader meta = JopsMetadata.getMetadataReader (filename);
    
    title = meta.getTitle ();
    artist = meta.getArtist ();
    album = meta.getAlbum ();
  }

  
  /**
   * Create a new song record, explicitly passing metadata.
   * 
   * @param inTitle     The title of the song.
   * @param inArtist    The artist of the song.
   * @param inAlbum     The album of the song.
   * @param inFilename  The file-name of the song.
   */
  
  public
  Song (String inTitle, String inArtist, String inAlbum, String inFilename)
  {
    title = inTitle;
    artist = inArtist;
    album = inAlbum;
    filename = inFilename;
  }

  /**
   * Get the album of the song.
   * 
   * @return  the album of the song.
   */
  
  public String
  getAlbum ()
  {
    return album;
  }
  
  
  /**
   * Get the artist of the song.
   * 
   * @return  the artist of the song.
   */
  
  public String
  getArtist ()
  {
    return artist;
  }
  
  
  /**
   * Get the filename of the song.
   * 
   * @return  the filename of the song.
   */
  
  public String
  getFilename ()
  {
    return filename;
  }
  
  
  /**
   * Get the title of the song.
   * 
   * @return  the title of the song.
   */
  
  public String
  getTitle ()
  {
    return title;
  }

  
  /**
   * Get a string representing the song.
   * 
   * @return  a string representation of the song
   */
  
  public String
  toString ()
  {
    return artist + " - " + title;
  }
}
