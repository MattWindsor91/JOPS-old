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

import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class PlayerDialog extends JDialog
{
  /**
   * 
   */
  private static final long serialVersionUID = -4738249004138144593L;

  private JLabel mixerLabel;
  private JLabel playerLabel;
  private JComboBox mixerList;
  private JComboBox playerList;
  private JButton okButton;
  private JButton cancelButton;
  private UserInterface master;
  private JDialog thisDialog;
  
  /**
   * Create a new Add or Edit Player dialog.
   * 
   * @param inMaster  The parent of the dialog.
   * @param playerToEdit  The name of the player to edit, or null if a new player
   *                      should be created.
   */
  
  public
  PlayerDialog (UserInterface inMaster, final String playerToEdit)
  {
    super (inMaster, "Edit Player...", Dialog.ModalityType.APPLICATION_MODAL);
   
    master = inMaster;
    thisDialog = this;
    
    Container contentPane = getContentPane ();
    GroupLayout layout = new GroupLayout (contentPane);
    
    contentPane.setLayout (layout);
    
    layout.setAutoCreateGaps (true);
    layout.setAutoCreateContainerGaps (true);
    
    // Populate player items. TODO: Do this in a less hard-coded way
    
    String[] playOptions = {"FLAC input from file"};
    
    // Populate mixer items.
    
    String[] mixOptions = master.getController ().getMixerList ();
    
    playerLabel = new JLabel ("Choose input: ");
    mixerLabel = new JLabel ("Choose output: ");

    playerList = new JComboBox (playOptions);
    playerList.setSelectedIndex (0);
    
    mixerList = new JComboBox (mixOptions);
    mixerList.setSelectedIndex (0);
    
    
    // Add Player button (if edit name is null)  
    if (playerToEdit == null)
      {   
        setTitle ("Add Player...");
        
        okButton = new JButton ("Add Player");
        
        okButton.addActionListener (new ActionListener () 
        {
          public void
          actionPerformed (ActionEvent event)
          { 
            // TODO: Add more players
            
            switch (playerList.getSelectedIndex ())
            {
              default:
              case 0:
                master.getController ().addPlayer ("SPI", mixerList.getSelectedIndex ());
                break;
            }
            
            thisDialog.dispose ();
          }
        });
      }
    // Edit player button (if otherwise)
/*    else
      {
        Player player = master.getController ().getPlayer (playerToEdit);
        
        setTitle ("Edit Player...");
        mixerList.setSelectedIndex (master.getController ().getIndexOfMixer (player.getMixer ()));
        
        okButton = new JButton ("Edit Player");
        
        okButton.addActionListener (new ActionListener () 
        {
          public void
          actionPerformed (ActionEvent event)
          {
            // TODO: fix
            Player player = null;
            
            // TODO: Add more players
            
            switch (playerList.getSelectedIndex ())
            {
              default:
              case 0:
                master.getController ().addPlayer ("SPI", mixerList.getSelectedIndex ());
                break;
            }
            
            master.getController ().replacePlayer (playerToEdit, player);
            
            thisDialog.dispose ();
          }
        });
      }*/
    
    // Cancel button
    
    cancelButton = new JButton ("Cancel");
   
    cancelButton.addActionListener (new ActionListener ()
    {
      public void
      actionPerformed (ActionEvent event)
      {
        thisDialog.dispose ();
      }
    });
    
    // Layout
    
    
    layout.setHorizontalGroup
      (
        layout.createSequentialGroup ()
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING) 
            .addComponent (playerLabel)
            .addComponent (mixerLabel))
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING) 
            .addComponent (playerList)
            .addComponent (mixerList))
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING)
            .addComponent (okButton)
            .addComponent (cancelButton))
      );
    
    layout.setVerticalGroup
      (
        layout.createSequentialGroup ()
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING)
            .addComponent (playerLabel)
            .addComponent (playerList)
            .addComponent (okButton))
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING)
            .addComponent (mixerLabel)
            .addComponent (mixerList)
            .addComponent (cancelButton))
      );
    
    layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
    
    pack ();
  }
}
