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
import java.applet.*;

/** BiomFrame : The only Object of that class
is to provide a Browser interface for the Frame */
public class BiomFrame extends Applet {

  boolean isStandalone = false;
  GridLayout gridLayout1 = new GridLayout();
  Button button1 = new Button();

  /** Link to Biomorph Frame/App. */
  CBiomApp BiomApp;

  public BiomFrame() {
  }

  public void init() {
    try  {
      jbInit();
    }
    catch(Exception e)  {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    button1.setLabel("Push to lauch applet");
    button1.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        button1_actionPerformed(e);
      }
    });
    this.setLayout(gridLayout1);
    this.add(button1, null);
    BiomApp=new CBiomApp(this);
  }

  public String getAppletInfo() {
    return GetName()+" "+GetVersionNum();
  }

  public String[][] getParameterInfo() {
    return null;
  }

  public String GetVersionNum() {
    return("1.1-10/01/2000");
  }

  public String GetName() {
    return("Biomorph Viewer");
  }

  public String GetAuthor() {
    return("Jean-Philippe Rennard");
  }

  public String GetInfos() {
    return(
      "\nBiomorph Applet\n"+
      "Copyright(c):\n "+GetAuthor()+" 01/2000\n\n"+
      "Inspired by R. Dawkins : \n"+
      "   The Blind Watchmaker\n\n"+
      "www.rennard.org/alife/"+"\n"+
      "alife@rennard.org");
  }
  
  /** Lauch BiomApp */
  void button1_actionPerformed(ActionEvent e) {
    Dimension dim= getToolkit().getScreenSize();
//    BiomApp.setLocation(dim.width/4,dim.height/4);
    BiomApp.setSize((int)(0.75*dim.width),(int)(0.75*dim.height));
    BiomApp.setLocation(20,20);
    BiomApp.show();
    BiomApp.start();
  }
}