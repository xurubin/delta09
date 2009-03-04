package org.delta.logic;

public class Nand3 extends Function {
  /**
   * UID for serialisation.
   */
  private static final long serialVersionUID = 1L;

  public Nand3() {
    super(3);
  }

  @Override
  public State evaluate() {
    Function nand = new Nand();
    
    Function and = new And();
    and.setArgument(0, getArgument(1));
    and.setArgument(1, getArgument(2));
    
    nand.setArgument(0, getArgument(0));
    nand.setArgument(1, and);
    
    return nand.evaluate();
  }

}
