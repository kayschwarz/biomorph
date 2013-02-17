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

import java.util.Vector;
//***************************************************************************
//      Def of abstract Living Entity (Biots)
//***************************************************************************
/** Defines the different Types of Biots */
class CBiotType
{
  static final String Type[]={"Biomorph"};
}
/** CBiot : Abstract Living Entity class. Defines the common
living properties and methods **/
abstract class CBiot
{
  /** Pointer to living entity universe */
  public CUniverse MyUniverse;

  CGenome Genome;
  private double EnergyLevel;
  int Direction;
  PosReal Position=new PosReal();

  public CBiot() {
  }
  /** Creates a Biot in a Universe **/
  public CBiot(CUniverse univ) {
    MyUniverse=univ;
  }
  /** Universe getter */
  public CUniverse GetMyUniverse() {
    return(MyUniverse);
  }
  /** Type of entity manager */
  CBiotType MyType;
  public CBiotType GetMyType() {
    return(MyType);
  }
  public void SetGenome(int gensize) {
    Genome=new CGenome(gensize);
  }
  public void SetBiotType(CBiotType t) {
    MyType=t;
  }
  public double GetEnergyLevel() {
    return(EnergyLevel);
  }
  //
  abstract void Live(JPView v);

}
/** CGenome : Genome defined as an Object Collection */ 
class CGenome extends Vector
{
  public CGenome(int nbgene) {
    for(int i=0;i<nbgene;i++) {
      this.addElement(null);
    }
  }
}

