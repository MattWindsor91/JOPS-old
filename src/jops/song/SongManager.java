/**
 * 
 */
package jops.song;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A manager for retrieving songs from the rest of the BAPS system.
 * 
 * @author Matt Windsor
 *
 */

public class SongManager
{
  private ArrayList<Song> list;
  
  public
  SongManager ()
  {
    list = new ArrayList<Song> ();
    
    Connection conn = null;
    try
      {
        conn = DriverManager.getConnection(
            "jdbc:postgresql:jops",
            "jops",
            "Dz92Fp64");
        
        Statement st = conn.createStatement ();
        st.setFetchSize (50);
        
        ResultSet rs = st.executeQuery ("SELECT jops_song.filename FROM jops_song INNER JOIN jops_songlist_song ON jops_song.songid = jops_songlist_song.songid WHERE jops_songlist_song.songlistid = 1");
        
        while (rs.next ())
          {
            list.add (new Song (rs.getString ("filename")));           
          }
      }
    catch (SQLException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace ();
      }
    finally
      {
        try
          {
            conn.close ();
          }
        catch (SQLException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace ();
          }
      }
  }

  /**
   * Get the song list used by this song manager.
   * 
   * @return  the SongList
   */
  
  public List<Song>
  getSongList ()
  {
    return list;
  }
}
