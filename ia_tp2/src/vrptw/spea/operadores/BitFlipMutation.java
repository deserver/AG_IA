/**
 * BitFlipMutation.java
 * @author Juan J. Durillo
 * @version 1.0
 * 
 */
package vrptw.spea.operadores;

import vrptw.spea.base.variable.Binary;
import vrptw.spea.baseS.Configuration;
import vrptw.spea.baseS.Solution;
import vrptw.spea.baseS.Configuration.SolutionType_;
import vrptw.spea.baseS.DecisionVariables;
import vrptw.spea.util.JMException;
import vrptw.spea.util.PseudoRandom;
import vrptw.spea.baseS.Operator;

 /**
  * This class implements a bit flip mutation operator.
  * NOTE: the operator is applied to binary solutions and it is applied to the 
  * whole solution as a single variable.
  */
public class BitFlipMutation extends Operator{
    
  /**
   * Constructor
   * Creates a new instance of the Bit Flip mutation operator
   */
  public BitFlipMutation() {    
  } // BitFlipMutation

 /**
  * Perform the mutation operation
  * @param probability Mutation probability
  * @param solution The solution to mutate
 * @throws JMException 
  */
  public void doMutation(double probability, Solution solution) throws JMException {        
    try {
      for (int i = 0; i < solution.getDecisionVariables().size(); i++) {                
        for (int j = 0; j < ((Binary)solution.getDecisionVariables().variables_[i]).getNumberOfBits(); j++) {
          if (PseudoRandom.randDouble() < probability) {
            ((Binary) solution.getDecisionVariables().variables_[i]).bits_.flip(j);
          }
        }
      }
            
      for (int i = 0; i < solution.getDecisionVariables().size(); i++)
        ((Binary) solution.getDecisionVariables().variables_[i]).decode();
            
    } catch (ClassCastException e1) {
      Configuration.logger_.severe("BitFlipMutation.doMutation: " + 
          "ClassCastException error" + e1.getMessage()) ;
      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".doMutation()") ;    
    }                
  } // doMutation
  
  /**
  * Executes the operation
  * @param object An object containing a solution to mutate
  * @return An object containing the mutated solution
   * @throws JMException 
  */
  public Object execute(Object object) throws JMException {
    Solution solution = (Solution)object;
    
    if ((solution.getType() != SolutionType_.Binary) && 
        (solution.getType() != SolutionType_.BinaryReal)) {
      Configuration.logger_.severe("BitFlipMutation.execute: the solution " +
          "is not of the right type. The type should be 'Binary' or " +
          "'BinaryReal', but " +solution.getType() + " is obtained");

      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;
    } // if 
     
    Double probability = (Double)getParameter("probability");     
    if (probability == null)
    {
      Configuration.logger_.severe("BitFlipMutation.execute: probability not " +
      "specified");
      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;  
    } 
        
    doMutation(probability.doubleValue(),solution);
    return solution;
   } // execute
} // BitFlipMutation
