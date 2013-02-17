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
//***************************************************************************
//  GRAPH-TOOLS
//
//    Diverse Graphical tools.
//***************************************************************************
/** Various Graphic functions **/
class JPGraphTools
{
    /** Normal representation */
    static final int REPRBASE=0;
    /** Anisotropic representation */
    static final int REPRANISOTROPIC=1;
    /** Isotropic representation */
    static final int REPRISOTROPIC=2;
    /** Current Reprensentation **/
    int ReprCrt;
    /** Logical size (theorical size for calculation) **/
    float lWidth,lHeight;
    /** Pixel size according to logical and real sizes **/
    float pWidth,pHeight;
    /** Current Canvas size **/
    int MaxX,MaxY;
    /** Origin Offsets **/
    int OrigOffsetX,OrigOffsetY;
    /** default constructor. Init size to 0 **/
    public JPGraphTools() {
      ReprCrt=REPRBASE;
      Resize(0,0);
    }
    /** Constructor. Receives size of the working Canvas **/
    public JPGraphTools(int mx, int my,int repr) {
      SetRepresentation(repr);
      Resize(mx,my);
    }
    /** set current origin offsets **/
    public void SetOrigOffset(int x,int y) {
      OrigOffsetX=x;
      OrigOffsetY=y;
    }
    /**set current working size **/
    public void Resize(int mx,int my) {
      MaxX=mx-1;
      MaxY=my-1;
      OrigOffsetX=0;
      OrigOffsetY=MaxY;
      SetLogicSize(MaxX,MaxY);
    }
    /**set current working and logical size **/
    public void Resize(int mx,int my,float lx,float ly) {
      MaxX=mx-1;
      MaxY=my-1;
      OrigOffsetX=0;//MaxX;
      OrigOffsetY=MaxY;
      SetLogicSize(lx,ly);
    }
    /** set Representation **/
    public void SetRepresentation(int r) {
      switch(r) {
        case REPRANISOTROPIC:
        case REPRISOTROPIC:
          ReprCrt=r;
          break;
        default :
          ReprCrt=REPRBASE;
      }
    }
    /** Get Representation **/
    public int GetRepresentation() {
      return(ReprCrt);
    }
    /** set logical size **/
    public void SetLogicSize(float w,float h) {
      lWidth=w;
      lHeight=h;
      switch(ReprCrt) {
        case REPRANISOTROPIC:
          pWidth=lWidth/MaxX;
          pHeight=lHeight/MaxY;
          break;
        case REPRISOTROPIC:
          pWidth=pHeight=Math.max(lWidth/MaxX,lHeight/MaxY);
          break;
        default:
          pWidth=1;
          pHeight=1;
      }
    }


    /** Logical to system display conversion
    Return : x=x, y=MaxY-y */
    public int LToRX(float x_) {
      switch(ReprCrt) {
        case REPRBASE:
          if(x_>MaxX)
            return(MaxX);
          return(Math.round(x_)+OrigOffsetX);
        case REPRANISOTROPIC:
          return(Math.round((x_/pWidth))+OrigOffsetX);
        case REPRISOTROPIC:
          return(Math.round(x_/pWidth)+OrigOffsetX);
      }
      return(0);
    }
    public int LToRY(float y_) {
      switch(ReprCrt) {
        case REPRBASE:
          if(y_>MaxY)
            return(0);
          return(OrigOffsetY-Math.round(y_));
        case REPRANISOTROPIC:
          return(OrigOffsetY-Math.round(y_/pHeight));
        case REPRISOTROPIC:
          return(OrigOffsetY-Math.round(y_/pHeight));
      }
        return(0);
    }

    /** System display to Logical conversion */
    public float RToLX(int x_) {
      switch(ReprCrt) {
        case REPRBASE:
          if(x_>MaxX+OrigOffsetX)
            return(MaxX);
          return(x_-OrigOffsetX);
        case REPRANISOTROPIC:
        case REPRISOTROPIC:
          return((x_-OrigOffsetX)*pWidth);
      }
      return(0);
    }
    public float RToLY(int y_) {
      switch(ReprCrt) {
        case REPRBASE:
          if(y_>MaxY)
            return(0);
          return(OrigOffsetY-MaxY-y_);
        case REPRANISOTROPIC:
          return( (OrigOffsetY-y_)*pHeight );
        case REPRISOTROPIC:
          return( (OrigOffsetY-y_)*pHeight );
      }
        return(0);
    }

} // JPGraphFunct
//***************************************************************************
/** PosReal : Implements Position in real (float) **/
class PosReal
{
  int NbDim;
  float x;
  float y;
  float z;
  /** Default 2D Constructor*/
  public PosReal() {
    x=0;
    y=0;
    NbDim=2;
  }
  /** Constructor 2D */
  public PosReal(float x_,float y_) {
    x=x_;
    y=y_;
    NbDim=2;
  }
  /** Constructor 3D */
  public PosReal(float x_,float y_, float z_) {
    x=x_;
    y=y_;
    z=z_;
    NbDim=3;
  }
  /** Converts Directly PosReal in java.awt.Point */
   public void PosRealToPoint(Point pt) {
      pt.x=(int)Math.round(x);
      pt.y=(int)Math.round(y);
  }
  /** Converts Indirectly PosReal in java.awt.Point */
  public Point PosRealToPoint() {
    Point p=new Point();
    p.x=(int)Math.round(x);
    p.y=(int)Math.round(y);
    return(p);
  }

} // PosReal
//***************************************************************************
