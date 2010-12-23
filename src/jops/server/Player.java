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

package jops.server;

import javax.sound.sampled.Mixer.Info;

/**
 * A player node.
 * 
 * Players receive a sound input (eg from a sound file or mixer input) and
 * send it to a sound output (eg the mixing desk).
 * 
 * @author Matt Windsor
 *
 */

public interface Player extends Runnable
{
  public Info getMixer ();
  public String getMixerName ();
  public String getName ();
  public String getTypeName ();
  public void setName (String nameString);
  public void setPaused (boolean isPaused);
  public void start ();
  public void stop ();
  public void unload ();
}
