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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import jops.protocol.JopsProtocol;

public class ServerDialog extends JDialog
{
  /**
   * 
   */
  private static final long serialVersionUID = -4738249004138144593L;
  
  private UserInterface master;
  protected final ServerDialog thisDialog;
  
  private JLabel serverLabel;
  private JLabel portLabel;
  private JTextField serverField;
  private SpinnerNumberModel portModel;
  private JSpinner portSpinner;
  private JButton okButton;
  private JButton cancelButton;
  private JLabel warnLabel;
  
  /**
   * Create a new Choose Server dialog.
   * 
   * @param inMaster  The parent of the dialog.
   */
  
  public
  ServerDialog (UserInterface inMaster)
  {
    super (inMaster, "Connect to Server", Dialog.ModalityType.APPLICATION_MODAL);
   
    master = inMaster;
    thisDialog = this;
    
    Container contentPane = getContentPane ();
    contentPane.setLayout (new BoxLayout (contentPane, BoxLayout.PAGE_AXIS));
    
    JPanel groupPanel = new JPanel ();
    GroupLayout layout = new GroupLayout (groupPanel);
    
    groupPanel.setLayout (layout);
    
    layout.setAutoCreateGaps (true);
    layout.setAutoCreateContainerGaps (true);
    
    serverLabel = new JLabel ("Server: ");
    portLabel = new JLabel ("Port: ");

    serverField = new JTextField ("localhost");
    serverLabel.setDisplayedMnemonic ('S');
    serverLabel.setLabelFor (serverField);
    
    portModel = new SpinnerNumberModel (JopsProtocol.DEFAULT_PORT,
                                        0, 
                                        32767,
                                        1);
    portLabel.setDisplayedMnemonic ('P');
    portLabel.setLabelFor (portSpinner);
    
    portSpinner = new JSpinner (portModel);
    
    warnLabel = new JLabel ("<html>WARNING: The defaults should be ok. Do not mess with them unless you know what you're doing.</html>");
    warnLabel.setPreferredSize (new Dimension (300, 50));
    warnLabel.setAlignmentX (CENTER_ALIGNMENT);
    
    okButton = new JButton ("OK");
        
    okButton.addActionListener (new ActionListener () 
    {
      public void
      actionPerformed (ActionEvent event)
      { 
        if (master.getController ().connectToServer (serverField.getText (), (Integer) portModel.getValue ()) == true)
          thisDialog.dispose ();
        else
          JOptionPane.showMessageDialog (thisDialog,
                                         "Failed to connect to server.",
                                         "Error",
                                         JOptionPane.ERROR_MESSAGE);
      }
    });
    
    // Cancel button
    
    cancelButton = new JButton ("Cancel");
   
    cancelButton.addActionListener (new ActionListener ()
    {
      public void
      actionPerformed (ActionEvent event)
      {
        System.exit (0);
      }
    });
    
    // Layout
    
    
    layout.setHorizontalGroup
      (
        layout.createSequentialGroup ()
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING) 
            .addComponent (serverLabel)
            .addComponent (portLabel))
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING) 
            .addComponent (serverField)
            .addComponent (portSpinner))
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING)
            .addComponent (okButton)
            .addComponent (cancelButton))
      );
    
    layout.setVerticalGroup
      (
        layout.createSequentialGroup ()
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING)
            .addComponent (serverLabel)
            .addComponent (serverField)
            .addComponent (okButton))
          .addGroup (layout.createParallelGroup (GroupLayout.Alignment.LEADING)
            .addComponent (portLabel)
            .addComponent (portSpinner)
            .addComponent (cancelButton))
      );
    
    layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);

    contentPane.add (groupPanel);
    contentPane.add (warnLabel);
    pack ();
  }
}
