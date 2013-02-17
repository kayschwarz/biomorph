//***************************************************************************
//    Biomorph Viewer 1.1
//      10/1/2000
//
//    This program is a Dawkin's Biomorph generator
//    Jean-Philippe Rennard Jan/2000
//    alife@rennard.org
//    http://www.rennard.org/alife
//
//    This program is free software; you can redistribute it and/or
//    modify it under the terms of the GNU General Public License
//    as published by the Free Software Foundation.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//***************************************************************************
package BiomFrame;

import java.awt.*;
import java.awt.event.*;

/** About Box */
public class CBiomAbout extends Dialog {

  /** Link to Biomorph Applet */
  CBiomApp CallingApp;

  Panel panel1 = new Panel();
  BorderLayout borderLayout1 = new BorderLayout();
  Panel panel2 = new Panel();
  Panel panel3 = new Panel();
  Button ButtonOK = new Button();
  BorderLayout borderLayout2 = new BorderLayout();
  TextArea textArea1 = new TextArea();

  public CBiomAbout(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    CallingApp=(CBiomApp)frame;
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
      jbInit();
      add(panel1);
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public CBiomAbout(Frame frame, boolean modal) {
    this(frame, CBiomApp.MotherApplet.GetAuthor() , modal);
  }

  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    panel2.setBackground(Color.yellow);
    panel2.setLayout(borderLayout2);
    ButtonOK.setLabel("OK");
    ButtonOK.addMouseListener(new java.awt.event.MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        ButtonOK_mouseClicked(e);
      }
    });

    String s1=CallingApp.MotherApplet.GetName();

    textArea1.setBackground(SystemColor.control);
    textArea1.setEditable(false);

    panel1.add(panel2, BorderLayout.CENTER);
    panel2.add(textArea1, BorderLayout.CENTER);
    panel1.add(panel3, BorderLayout.SOUTH);
    panel3.add(ButtonOK, null);
    String s=s1+" v:"+CallingApp.MotherApplet.GetVersionNum()+"\n"+
            CallingApp.MotherApplet.GetInfos();
    textArea1.setText(s);
  }

  protected void processWindowEvent(WindowEvent e) {
    if(e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }

  void cancel() {
    dispose();
  }

  void ButtonOK_mouseClicked(MouseEvent e) {
    cancel();
  }
}