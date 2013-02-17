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

//***************************************************************************
//      Views Def
//***************************************************************************
/** Bioms Views : JPView extension to manage Biomorph **/
class CBiomView extends JPView
{
  static final int TYPEMAIN=0;
  static final int TYPEZOOM=1;
  /** Pointer to the Universe **/
  CBiomorphUniverse MyUniverse;

  int HorizStep, VerticStep;
  int DispGeneHStep, DispGeneVStep;

  int Type;
  int SelectedGrid=0;
  int SelectedGene=0;
  /** Construct a BiomView with background color and type */
  public CBiomView(CBiomorphUniverse univ,Color c, int type) {
      super(c);
      Type=type;
      MyUniverse=univ;
  }

  /** Paint View Background */
  void PaintBackground() {
    this.ClearBkg();
    if(Type==TYPEMAIN) {
      Color colcrt=GrBuff.getColor();
      GrBuff.setColor(getBackground());
      GrBuff.fillRect(0,0,Width,Height);
      GrBuff.setColor(colcrt);
      GrBuff.drawRect(0,Width,Height,0);
      for(int i=1;i<5;i++)
        GrBuff.drawLine(i*(HorizStep),0,i*(HorizStep),Height);
      for(int i=1;i<4;i++)
        GrBuff.drawLine(0,i*(VerticStep),Width,i*(VerticStep));
      PaintGenomeBack();
    }
  }

  /** Paint the Genome Square Background */
  void PaintGenomeBack() {
      Color colcrt=GrBuff.getColor();
      GrBuff.setColor(getBackground());
      GrBuff.fillRect(4*(DispGeneHStep)+1,3*(DispGeneVStep)+1,Width-1,Height-1);
      GrBuff.setColor(colcrt);
      for(int i=1;i<3;i++)
        GrBuff.drawLine(4*(DispGeneHStep)+i*(DispGeneHStep/3),3*(DispGeneVStep),
                4*(DispGeneHStep)+i*(DispGeneHStep/3),Height );
      for(int i=1;i<3;i++)
        GrBuff.drawLine(4*(DispGeneHStep),3*(DispGeneVStep)+i*(DispGeneVStep/3),
            Width,3*(DispGeneVStep)+i*(DispGeneVStep/3));
  }

  /** Returns the # of the grid at point x,y **/
  int GetGridAt(int x,int y) {
    return( (int)
        ( (y/DispGeneVStep)*5 + ((x/DispGeneHStep)+1) ));
  }

  /** Returns the # of the gene at point x,y, if click on Gene disp Square **/
  int GetGridDirAt(int x,int y) {
    int b= ( (y-(DispGeneVStep*3))/(DispGeneVStep/3)*3  +
            ((x-DispGeneHStep*4)/(DispGeneHStep/3))*1) +1;
    if(b<4)
      return(b);
    else if(b==4)
      return(0);
    else if(b==6)
      return(4);
    else if(b>6)
      return((9-b))+5;
    return(8);  //direction
  }

  /** Type getter **/
  public int GetType() {
    return(Type);
  }

  /** Resize Graphic Buffer and Steps */
  public void ResizeGrBuff() {
    super.ResizeGrBuff();
    if(Type==TYPEMAIN) {
      HorizStep=Width/5;
      VerticStep=Height/4;
    }
    else {
      HorizStep=Width;
      VerticStep=Height;
    }
    DispGeneHStep=Width/5;
    DispGeneVStep=Height/4;
  }

  /** Switch between views */
  void SwitchView() {
    if(Type==TYPEMAIN) {
      Type=TYPEZOOM;
      ResizeGrBuff();
      PaintBackground();
      if(SelectedGrid!=20)
        MyUniverse.SetZoomBiom( (CBiomorph)(MyUniverse.GetBiotAt(SelectedGrid-1)));
      else
        MyUniverse.SetZoomBiom(MyUniverse.CurrentBiom);
      MyUniverse.ZoomBiom.Live(this);
      repaint();
    }
    else {
      Type=TYPEMAIN;
      ResizeGrBuff();
      PaintBackground();
      MyUniverse.DrawWorld(this);
    }
  }

  /** Mouse Down : Process Selected grid and Gene/dir */
  public boolean mouseDown(Event e,int x,int y) {
    // if ZoomView restore MainView
    if(Type==TYPEZOOM) {
      SwitchView();
      return(true);
    }
    // select the Biom or Dir/Gene according to Click
    SelectedGrid=GetGridAt(x,y);
    if(SelectedGrid==20) {
      SelectedGene=GetGridDirAt(x,y);
      mouseDrag(e,x,y);
      return(true);
    }
    else
      SelectedGene=0;
    // If Ctrl-Click, set ZoomView
    if(e.controlDown() && SelectedGene==0) {
      SwitchView();
    }
    // else select new father
    else if(SelectedGene==0) {
      MyUniverse.SetCurrentBiom((CBiomorph)(MyUniverse.GetBiotAt(SelectedGrid-1)));
      MyUniverse.Go(this);
    }
    return(true);
  }

  /** Mouse Drag : modify Genome */
  public boolean mouseDrag(Event e,int x,int y) {
      if(SelectedGrid!=20)
        return(false);

      SelectedGene=this.GetGridDirAt(x,y);
      PosReal pr=new PosReal();
      pr=MyUniverse.CurrentBiom.GetClickGeneValue(this,SelectedGene,
            x,y);
      MyUniverse.CurrentBiom.SetClickGeneValue(this,SelectedGene,pr);

      if(Type==TYPEMAIN)
        SwitchView();

      MyUniverse.CurrentBiom.Live(this);
      MyUniverse.DrawWorld(this);

      MyUniverse.CurrentBiom.DispGenome(this);

      return(true);
  }

  /** MouseUp : Select a new Father according to gene modification */
  public boolean mouseUp(Event e,int x,int y) {
    if(SelectedGrid==20) {
      if(Type==TYPEZOOM) {
        SwitchView();
        MyUniverse.SetCurrentBiom(MyUniverse.CurrentBiom);
      }
      MyUniverse.Go(this);
    }
    return(true);
  }
}
