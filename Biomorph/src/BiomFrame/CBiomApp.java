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
import java.util.*;

/** Main Application */
public class CBiomApp extends Frame {

  public static BiomFrame MotherApplet;
  CBiomorphUniverse World;
  JPGraphTools GrTools=new JPGraphTools(0,0,JPGraphTools.REPRANISOTROPIC);// .REPRBASE);

  Panel panel1 = new Panel();
  Panel panel3 = new Panel();
  Button ButtonReset = new Button();
  BorderLayout borderLayout1 = new BorderLayout();
  Button ButtonRandom = new Button();
  Button ButtonUndo = new Button();
  Button ButtonAbout = new Button();
  GridLayout gridLayout1 = new GridLayout();
  GridLayout gridLayout2 = new GridLayout();

  public CBiomApp(BiomFrame bf) {
    super(bf.GetName()+" "+bf.GetVersionNum()+" "+ bf.GetAuthor());
    MotherApplet=bf;
    try  {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    panel1.setBackground(Color.yellow);
    panel1.setLayout(gridLayout2);
    this.addComponentListener(new java.awt.event.ComponentAdapter() {

      public void componentResized(ComponentEvent e) {
        this_componentResized(e);
      }
    });
    ButtonReset.setLabel("Reset");
    ButtonReset.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        ButtonReset_actionPerformed(e);
      }
    });
    panel3.setBackground(SystemColor.control);
    panel3.setLayout(gridLayout1);
    ButtonRandom.setLabel("Random");
    ButtonRandom.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        ButtonRandom_actionPerformed(e);
      }
    });
    ButtonUndo.setLabel("Undo");
    ButtonUndo.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        ButtonUndo_actionPerformed(e);
      }
    });

    ButtonAbout.setLabel("About");
    ButtonAbout.addMouseListener(new java.awt.event.MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        ButtonAbout_mouseClicked(e);
      }
    });
    this.addWindowListener(new java.awt.event.WindowAdapter() {

      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    this.add(panel1, BorderLayout.CENTER);
    this.add(panel3, BorderLayout.NORTH);
    panel3.add(ButtonReset, null);
    panel3.add(ButtonRandom, null);
    panel3.add(ButtonUndo, null);
    panel3.add(ButtonAbout, null);
    /** Creates the World with Biomorphs **/
    World=new CBiomorphUniverse(this);
    panel1.add(World.GetMainView(), "MainView");
    repaint();
  }
  /** Here we go ! */
  public void start() {
    World.InitView();
    World.Go();
  }
  /** Resizing management */
  void this_componentResized(ComponentEvent e) {
    World.InitView();
    World.DrawWorld();
  }

  /** Restore Default bioms */
  void ButtonReset_actionPerformed(ActionEvent e) {
    World.Reset();
  }

  /** Generate Random Bioms */
  void ButtonRandom_actionPerformed(ActionEvent e) {
    World.GenRndBioms();
  }

  /** restore previous father */
  void ButtonUndo_actionPerformed(ActionEvent e) {
    World.Undo();
  }
  /** Display About box */
  void ButtonAbout_mouseClicked(MouseEvent e) {
    CBiomAbout Box=new CBiomAbout(this,MotherApplet.GetName(), true);
    Dimension dim= getToolkit().getScreenSize();
    Box.setSize(dim.width/4,dim.height/3);
    Box.setLocation(this.getLocation().x+(this.getSize().width/2)-
        (Box.getSize().width/2),dim.height/2-Box.getSize().height/2);
    Box.show();
  }
  /** Good-Bye */
  void this_windowClosing(WindowEvent e) {
    dispose();
  }
}

//***************************************************************************
//  JPGridBag
//***************************************************************************
/** JPGridBag : Implements GridBagConstraints complex JDK 1.1 constructor for
1.0 compatibility **/
class JPGridBag extends GridBagConstraints
{
  public JPGridBag(int gridx_,int gridy_, int gridwidth_, int gridheight_, double weightx_,
      double weighty_,int anchor_,int fill_,Insets insets_,int ipadx_, int ipady_) {
    gridx=gridx_;
    gridy=gridy_;
    gridwidth=gridwidth_;
    gridheight=gridheight_;
    weightx=weightx_;
    weighty=weighty_;
    anchor=anchor_;
    fill=fill_;
    insets=insets_;
    ipadx=ipadx_;
    ipady=ipady_;
  }
}

