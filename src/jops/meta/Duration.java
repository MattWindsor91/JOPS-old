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

public class Duration
{
  private int microseconds;
  private int seconds;
  private int minutes;
  private int hours;
  private long days;
  
  /**
   * Create a Duration object to hold a duration.
   * 
   * @param duration  The duration, expressed in microseconds.
   */
  
  public
  Duration (Long duration)
  {
    long time = duration;
    
    microseconds = (int) (time % 100000);
    time -= microseconds;
    time /= 1000000;
    
    seconds = (int) (time % 60);
    time -= seconds;
    time /= 60;
    
    minutes = (int) (time % 60);
    time -= minutes;
    time /= 60;
    
    hours = (int) (time % 24);
    time -= hours;
    time /= 24;
    
    days = (long) time;
  }
  
  /**
   * Acquire a string representation of this duration.
   * 
   * @return the string representation.
   */
  
  public String
  ToString ()
  {
    String returnVal = new String ();
    
    if (days == 1)
      returnVal += days + " day, ";
    else if (days > 1)
      returnVal += days + " days, ";
    
    if (hours > 0 && hours < 10)
      returnVal += "0" + hours + ":";
    else if (hours >= 10)
      returnVal += hours + ":";
    
    if (minutes == 0)
      returnVal += "00:";
    else if (minutes < 10)
      returnVal += "0" + minutes + ":";
    else
      returnVal += minutes + ":";

    if (seconds == 0)
      returnVal += "00";
    else if (seconds < 10)
      returnVal += "0" + seconds;
    else
      returnVal += seconds;
    
    return returnVal;
  }
}
