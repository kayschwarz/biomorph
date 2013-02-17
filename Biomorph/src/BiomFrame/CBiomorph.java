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
import java.util.*;
/** CBiomorph : defines a Biomorph as a Biot */
class CBiomorph extends CBiot
{
  final int NbGene=9;
  final int GenomeScale=20;
  final int Delta=GenomeScale/GenomeScale;
  // Used for BiomSize
  float XMax=-99999;
  float XMin=99999;
  float YMax=-99999;
  float YMin=99999;

  int XOffsets[];
  int YOffsets[];

  CBiomorphUniverse MyUniverse;
  /** Position on Grid */
  int Place;
  /** Pointer to App GraphTools */
  JPGraphTools GT;

  /** Create a Biom in Universe */
  public CBiomorph(CBiomorphUniverse univ) {
    SetGenome(NbGene);
    MyUniverse=univ;
    GT= MyUniverse.App.GrTools;
    XOffsets=new int[MyUniverse.NbDirections];
    YOffsets=new int[MyUniverse.NbDirections];
    Place=1;
    Direction = 2;  // Up
    Position.x=0;
    Position.y=0;
    SetDefaultGenes();
  }

  /** Randomly creates Biomorph at Place*/
  public CBiomorph(CBiomorphUniverse univ,boolean rnd,int place)
  {
    SetGenome(NbGene);
    MyUniverse=univ;
    GT=MyUniverse.App.GrTools;
    XOffsets=new int[MyUniverse.NbDirections];
    YOffsets=new int[MyUniverse.NbDirections];
    Place=place+1;
    Direction= 2; // Up
    Position.x=0;
    Position.y=0;
    if(rnd) {
      for(int i=0;i<NbGene;i++)
        SetGene(i+1,(int)(Math.random()*GenomeScale) *
                  (Math.random()>0.5 ? Delta : -1*Delta));
      SetGene(9,(int)(Math.random()*6)+4);
    }
    else
      SetDefaultGenes();
  }

  /** Creates a descendant with a mutation on gene nummutant */
  public CBiomorph(CBiomorph father,int nummutant) {
    SetGenome(NbGene);
    MyUniverse=father.MyUniverse;
    GT=MyUniverse.App.GrTools;
    XOffsets=new int[MyUniverse.NbDirections];
    YOffsets=new int[MyUniverse.NbDirections];
    // In normal display - e.d. not random gen - the Place is linked to
    // the gen mutation.
    Place=nummutant+1;
    for(int i=0;i<NbGene;i++) {
      SetGene(i+1,father.GetGene(i+1));
    }
    Direction = father.Direction;
    Position.x=father.Position.x;
    Position.y=father.Position.y;
    int genecrt=(int)((nummutant-1)/2)%9+1;
    int d=Delta;
    if((nummutant)%2==1)
      d *= -1;
    SetGene(genecrt,father.GetGene(genecrt)+d);
  }

  /** Set Biom genome to default values */
  void SetDefaultGenes() {
    SetGene(1, GenomeScale);
    SetGene(2, GenomeScale);
    SetGene(3, GenomeScale);
    SetGene(4, -1*GenomeScale);
    SetGene(5, (int)(Math.sin(Math.PI/4.0)*GenomeScale)*-1);
    SetGene(6, 0);
    SetGene(7, (int)(Math.sin(Math.PI/4.0)*GenomeScale));
    SetGene(8, 1*GenomeScale);
    SetGene(9, 5);
  }

  /** Gene setter */
	void SetGene(int GeneNum, int GeneValue) {
    // Normalisation
    if(GeneNum!=9) {
      if(GeneValue>GenomeScale*2)
        GeneValue=GenomeScale*2;
      else if(GeneValue<GenomeScale*-2)
        GeneValue=GenomeScale*-2;
    }
      // Order Gene
    else {
      if(GeneValue>12)
        GeneValue=12;
      else if(GeneValue<2)
        GeneValue=2;
    }
    // Gene modification
		Genome.setElementAt(new Integer(GeneValue),GeneNum-1);
  }

  /** Return the nth Gene as Integer */
	int GetGene(int GeneNum) {
    Integer g=(Integer)(Genome.elementAt(GeneNum-1));
    return(g.intValue());
  }

  /** Calculate X and Y Offsets from the genes */
  void CalcXOffset() {
    // Load up the X offsets from the genes
    XOffsets[0] = GetGene(2) * -1;
    XOffsets[1] = GetGene(1) * -1;
    XOffsets[2] = 0;
    XOffsets[3] = GetGene(1);
    XOffsets[4] = GetGene(2);
    XOffsets[5] = GetGene(3);
    XOffsets[6] = 0;
    XOffsets[7] = GetGene(3) * -1;

    // Load up the Y offsets from the genes
    YOffsets[0] = GetGene(6);
    YOffsets[1] = GetGene(5);
    YOffsets[2] = GetGene(4);
    YOffsets[3] = GetGene(5);
    YOffsets[4] = GetGene(6);
    YOffsets[5] = GetGene(7);
    YOffsets[6] = GetGene(8);
    YOffsets[7] = GetGene(7);
  }

  /** Live function : Draw */
  public void Live(JPView v) {
    CBiomView bv=(CBiomView) v;

    // How deep will we branch?
    int Order = GetGene(9);
    // Calculate X and Y Offset arrays
    CalcXOffset();

    int xOffs=0;
    int yOffs=0;
    float lh=0.0F;
    float lv=0.0F;
    Position.x=Position.y=0;
    XMax=-99999.0F;
    XMin=0.0F;
    YMax=-99999.0F;
    YMin=0.0F;

    CalcBiomSize(Position.x,Position.y,Order,Direction,XOffsets,YOffsets);

    Color crtcol;
    crtcol=bv.GrBuff.getColor();
    if( bv.GetType()==CBiomView.TYPEMAIN) {
      if(Place==1)
        v.GrBuff.setColor(Color.red);
      xOffs= (((Place-1)%5))*bv.HorizStep;
      yOffs= ((int)((Place-1)/5+1)*bv.VerticStep);
    }
    else {
      xOffs=0;
      yOffs=v.Height;
    }
    lh=(float) 1.1*Math.abs(XMax-XMin);
    lv=(float) 1.1*(Math.abs(YMax-YMin));
    GT.Resize(bv.HorizStep,bv.VerticStep,lh,lv);
    GT.SetOrigOffset(xOffs,yOffs);
    Position.x=lh/2;
    Position.y=(YMin<0 ? Math.abs(YMin)+lv*0.05F : lv*0.05F);

    Draw(bv.GrBuff, Position.x, Position.y, Order, Direction, XOffsets, YOffsets);
    bv.GrBuff.setColor(crtcol);
  }

  /** Determines the global size of the Biom */
  void CalcBiomSize(float X, float Y,int Length,int Direction, int XOffsets[], int YOffsets[])
  {
    Direction = (Direction + MyUniverse.NbDirections)%MyUniverse. NbDirections ;
    float NewX = X + Length * XOffsets[Direction];
    float NewY = Y - Length * YOffsets[Direction];
    if(NewX>XMax) XMax=NewX;
    if(NewX<XMin) XMin=NewX;
    if(NewY>YMax) YMax=NewY;
    
    if(YMax<0) YMax=0;

    if(NewY<YMin) YMin=NewY;

    if(Length>0) {
        // Draw one to the left
        CalcBiomSize(NewX, NewY, Length-1, Direction-1, XOffsets, YOffsets);
        // Draw one to the right
        CalcBiomSize(NewX, NewY, Length-1, Direction+1, XOffsets, YOffsets);
    }
  }

  /** Recursive Drawing algorithm */
  void Draw(Graphics g, float X, float Y, int Length, int Direction,
                                          int XOffsets[], int YOffsets[])
  {
    Direction = (Direction + MyUniverse.NbDirections)%MyUniverse. NbDirections ;
    float NewX = X + Length * XOffsets[Direction];
    float NewY = Y - Length * YOffsets[Direction];
    g.drawLine(GT.LToRX(X),GT.LToRY(Y),GT.LToRX(NewX),GT.LToRY(NewY));
    if(Length>0) {
        // Draw left
        Draw(g, NewX, NewY, Length-1, Direction-1, XOffsets, YOffsets);
        // Draw right
        Draw(g, NewX, NewY, Length-1, Direction+1, XOffsets, YOffsets);
    }
  }

// ****************************************************************************
//  Gene display management

  /** Some gene display init. */
  void InitDispGene(CBiomView v) {
    v.PaintGenomeBack();
    CalcXOffset();
    // Search the max X and Y genome size
    int xmax=GenomeScale;
    int ymax=GenomeScale;
    for(int i=0;i<NbGene-1;i++) {
      if (XOffsets[i]>xmax)
        xmax=XOffsets[i];
      if(Math.abs(YOffsets[i])>ymax)
        ymax=Math.abs(YOffsets[i]);
    }
    GT.Resize(Math.round(v.DispGeneHStep/3.0F),Math.round(v.DispGeneVStep/3.0F),xmax*2*1.5F,ymax*2*1.5F);
  }

  /** Set display OrigOffset for a Direction */
  void SetDirOrigOffset(int numdir,CBiomView v) {
    int xOffs=0;
    int yOffs=0;
    if(numdir>0 && numdir<4) {
      // calculate Origin
      xOffs=4*(v.DispGeneHStep )+((numdir-1)%3)*(v.DispGeneHStep/3);
      yOffs=3*(v.DispGeneVStep)+(v.DispGeneVStep/3);

    }
    else if(numdir==0) {
      xOffs=4*(v.DispGeneHStep);
      yOffs=3*(v.DispGeneVStep)+((2*v.DispGeneVStep/3));
    }
    else if(numdir==4) {
      xOffs=4*(v.DispGeneHStep)+2*(v.DispGeneHStep/3);
      yOffs=3*(v.DispGeneVStep)+((2*v.DispGeneVStep/3));
    }
    else if(numdir>4 && numdir<8) {
      xOffs= 4*(v.DispGeneHStep)+ (7-numdir)*(v.DispGeneHStep/3);
      yOffs= 3*(v.DispGeneVStep)+(3*(v.DispGeneVStep/3));
    }
    else if(numdir==8) {      // order
      xOffs=4*(v.DispGeneHStep)+(v.DispGeneHStep/3);
      yOffs=3*(v.DispGeneVStep)+((2*v.DispGeneVStep/3));
    }
    GT.SetOrigOffset(xOffs+(v.DispGeneHStep/6),yOffs-(v.DispGeneVStep/6));
  }

  /** Display the Biom genome (e.d directions) in lower right square */
  void DispGenome(CBiomView v) {
    InitDispGene(v);
    // Disp 8 directions
    for(int i=0;i<8;i++) {
      SetDirOrigOffset(i,v);
      v.GrBuff.drawOval(GT.LToRX((float)GenomeScale*-1F),GT.LToRY((float)(GenomeScale)),
        GT.LToRX((float)GenomeScale*1F)-GT.LToRX((float)GenomeScale*-1F),
        GT.LToRY((float)(GenomeScale)*-1F)-GT.LToRY((float)(GenomeScale)));
      v.GrBuff.drawLine(GT.LToRX(0.0F),GT.LToRY(0.0F),
              GT.LToRX((float)XOffsets[i]),GT.LToRY(-1*(float)YOffsets[i]));
    }
    // Disp Order
    SetDirOrigOffset(8,v);
    FontMetrics metric=v.GrBuff.getFontMetrics(v.GrBuff.getFont());
    int sizefont=v.GrBuff.getFont().getSize();
    Integer o=new Integer(GetGene(9));
    String s=o.toString();
    v.GrBuff.drawString(s,GT.LToRX(0.0F-(metric.stringWidth(s)/2)),GT.LToRY(0.0F-sizefont/2));
  }

  /** Return the value of direction Genes at clicking point */
  PosReal GetClickGeneValue(CBiomView v,int numdir,int x,int y) {
    PosReal pr=new PosReal();
    InitDispGene(v);
    SetDirOrigOffset(numdir,v);
    if(numdir >2 && numdir<6)
      pr.x=GT.RToLX(x)*1;
    else
      pr.x=GT.RToLX(x)*-1;
    pr.y=GT.RToLY(y)*-1;
    DispGenome(v);
    return(pr);
  }

  /** Set the values of direction Genes according to clicking point */
  void SetClickGeneValue(CBiomView v,int numdir,PosReal pos) {
    switch(numdir) {
      case 0:
      case 4:
        SetGene(2,Math.round(pos.x));
        SetGene(6,Math.round(pos.y));
        break;
      case 1:
      case 3:
        SetGene(1,Math.round(pos.x));
        SetGene(5,Math.round(pos.y));
        break;
      case 2:
        SetGene(4,Math.round(pos.y));
        break;
      case 5:
      case 7:
        SetGene(3,Math.round(pos.x));
        SetGene(7,Math.round(pos.y));
        break;
      case 6:
        SetGene(8,Math.round(pos.y));
        break;
    }
  }

} // CBiomorph


