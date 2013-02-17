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
/** CUniverse : Abstract class to define universe Basic properties */
abstract class CUniverse
{
  static final int BIOTS_BKP_SIZE=20;
  /** Pointer to Application */
  Container MyApp;

  final int NbDirections=8;
  /** Biots Collection */
  Vector Biots;
  /** 2d Biots Collection for diverses uses */
  Stack BiotsBkp;
  /** Constructor */
  public CUniverse(Container app) {
    MyApp=app;
    Biots=new Vector();
    BiotsBkp=new Stack();
  }
  /** Return the nth Biot in Collection */
  public CBiot GetBiotAt(int n) {
    return((CBiot)Biots.elementAt(n));
  }
  /** Add a Biot to BKP */
  public void AddBiotBkp(CBiot b) {

    if(BiotsBkp.size()==BIOTS_BKP_SIZE) {
      CBiot bl[];
      bl=new CBiot[BIOTS_BKP_SIZE];
      for(int i=0;i<BIOTS_BKP_SIZE;i++)
        bl[i]=(CBiot)BiotsBkp.pop();
      for(int i=BIOTS_BKP_SIZE-2;i>=0;i--)
          BiotsBkp.push(bl[i]);
    }
    BiotsBkp.push(b);
  }
  /** Remove a Biot from BKP */
  public void RemoveBiotBkp() {
    BiotsBkp.pop();
  }
  /** Empty BiotBkp */
  public void EmptyBiotBkp() {
    BiotsBkp.removeAllElements();
  }
  /** Get Last Biot in BKP */
  public CBiot GetBiotBkp() {
    return( (CBiot) BiotsBkp.peek() );
  }
  /** Run the World */
  abstract public void Go(JPView jpv);
  /** Draw the World */
  abstract public void DrawWorld(JPView jpv);
}

//**************************************************************************
//  CBiomorphUniverse
//    Definition of Biomorph Universe
//**************************************************************************
/** Defines the functions of the Biomorph's Universe*/
class CBiomorphUniverse extends CUniverse
{
  final int NbMutants=18;
  /** Pointer to calling App */
  CBiomApp App;
  /** View of the Universe */
  CBiomView MainView;
  /** Biom at place 1 */
  CBiomorph CurrentBiom;
  /**Current Zooming biom */
  CBiomorph ZoomBiom;

  /** Construct World in Application */
  public CBiomorphUniverse(CBiomApp ba) {
    super((Container) ba);
    App=(CBiomApp) MyApp;

    MainView=new CBiomView(this,Color.black,CBiomView.TYPEMAIN);

    Biots.addElement(new CBiomorph(this));
    CurrentBiom=GetCurrentBiom();

    AddBiotBkp(CurrentBiom);
    CreateMutants(CurrentBiom);
  }

  /** View getter */
  CBiomView GetMainView() {
    return(MainView);
  }
  /** Init View */
  void InitView() {
    MainView.ResizeGrBuff();
    MainView.PaintBackground();
  }
  /** Create offspring set for father */
  void CreateMutants(CBiomorph father) {
    Biots.setSize(NbMutants+1);
    for(int i=0;i<NbMutants;i++) {
      Biots.setElementAt(new CBiomorph(father,i+1),i+1);
    }
  }

  /** Return current biom */
  CBiomorph GetCurrentBiom() {
    return( (CBiomorph)Biots.elementAt(0) );
  }

  /** set biom to CurrentBiom (0) */
  void SetCurrentBiom(CBiomorph biom) {
    Biots.setElementAt(biom,0);
    biom.Place=1;
    CurrentBiom=biom;
    AddBiotBkp(CurrentBiom);
  }

  /** set Zoom biom to CurrentBiom (0) */
  void SetZoomBiom(CBiomorph biom) {
    ZoomBiom=biom;
  }

  /** Undo : Get last Biom in BKP */
  void Undo() {
    if(MainView.Type!=CBiomView.TYPEMAIN)
      return;
    if(BiotsBkp.size()<2)
      return;
    this.RemoveBiotBkp();
    if(BiotsBkp.isEmpty())
      return;
    Biots.setElementAt((CBiomorph)this.GetBiotBkp(),0);
    CurrentBiom=GetCurrentBiom();
    Go(MainView);
  }
  /** Restore default Bioms*/
  void Reset() {
    if(MainView.Type==CBiomView.TYPEMAIN) {
      Biots.setElementAt(new CBiomorph(this),0);
      CurrentBiom=GetCurrentBiom();
      this.EmptyBiotBkp();
      AddBiotBkp(CurrentBiom);
      Go(MainView);
    }
  }

  /** Generate Random Bioms */
  void GenRndBioms() {
    if(MainView.Type==CBiomView.TYPEMAIN) {
      for(int i=0;i<=NbMutants;i++)
        Biots.setElementAt(new CBiomorph(this,true,i),i);
      CurrentBiom=GetCurrentBiom();
      DrawWorld(MainView);
    }
  }

  /** Run the World in View e.d. create offsprings set and draw World*/
  public void Go(JPView jpv) {
    CreateMutants(CurrentBiom);
    DrawWorld(jpv);
  }
  /** Default Run */
  public void Go() {
    Go(MainView);
  }
   /** Default drawing */
   public void DrawWorld() {
    DrawWorld(MainView);
   }
  /** Draw World */
  public void DrawWorld(JPView jpv) {
    CBiomView bv=(CBiomView)jpv;
    bv.PaintBackground();
    if(bv.Type==CBiomView.TYPEMAIN) {
      for(int i=0;i<=NbMutants;i++) {
        ((CBiot)(Biots.elementAt(i))).Live(bv);
      // paint current Biom Genome
      bv.PaintGenomeBack();
      CurrentBiom.DispGenome(bv);
      }
    }
    else {
      ZoomBiom.Live(bv);
    }
    bv.repaint();
  }

}   // CBiomorphUniverse


