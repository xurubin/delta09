package org.delta.logic;

public class Nor3 extends Function {
  /**
   * UID for serialisation.
   */
  private static final long serialVersionUID = 1L;

  public Nor3() {
    super(3);
  }

  @Override
  public State evaluate() {
    Function nor = new Nor();
    
    Function or = new Or();
    or.setArgument(0, getArgument(1));
    or.setArgument(1, getArgument(2));
    
    nor.setArgument(0, getArgument(0));
    nor.setArgument(1, or);
    
    return nor.evaluate();
  }

}
