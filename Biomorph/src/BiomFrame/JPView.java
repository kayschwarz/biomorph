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
/** JPView : Views. Gives a Canvas to a panel and
manages double buffering enventually resizing.
To use this class, just create inheriting classes for each canvas you need
and define only the paint function (optionnaly a constructor to
initiate background color.) **/

class JPView extends Canvas
{
  /** Image for double buffering **/
  Image ImgBuff;
  /** Graphics context for double buffering **/
  Graphics GrBuff;
  /** View size **/
  int Height,Width;
  /** Logical size for calculation **/
  float lHeight, lWidth;
  /** Default constructor **/
  public JPView() {
//    AddListener();
  }
  /** Construct a View with a bkgrd color **/
  public JPView(Color c) {
    setBackground(c);
    setForeground(Color.white);
  }
    /** remove buffer */
  public void destroy() {
    GrBuff.dispose();
  }

  /** Set the dimension variables, returns the Dimension **/
  Dimension SetDim() {
    Dimension d=getParent().getSize();
    Height=d.height;
    Width=d.width;
    return(d);
  }
  /** Set the logical size **/
  void SetLogicalSize(float w,float h) {
    lWidth=w;
    lHeight=h;
  }
  /** Modify the size of the view **/
  void ResizeGrBuff() {
    SetDim();
    ImgBuff=null;
    ImgBuff=createImage(Width,Height);
    GrBuff=ImgBuff.getGraphics();
  }
  /** Clear Background **/
  void ClearBkg() {
    GrBuff.setColor(getBackground());
    GrBuff.fillRect(0,0,Width,Height);
    GrBuff.setColor(this.getForeground());
  }

  public void paint(Graphics g) {
    if(ImgBuff!=null)
      g.drawImage(ImgBuff,0,0,this);
  }
}
