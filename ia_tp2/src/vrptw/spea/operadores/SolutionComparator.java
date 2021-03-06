/**
 * SolutionComparator.java
 * 
 * @author Juan J. Durillo
 * @version 1.0
 */
package vrptw.spea.operadores;

import java.util.Comparator;

import vrptw.spea.baseS.Configuration;
import vrptw.spea.baseS.Solution;
import vrptw.spea.util.Distance;
import vrptw.spea.util.JMException;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on the values of the variables.
 */
public class SolutionComparator implements Comparator {
   
  /**
   * Establishes a value of allowed dissimilarity
   */
  private static final double EPSILON  = 1e-10;    
        
  /**
   * Compares two solutions.
   * @param o1 Object representing the first <code>Solution</code>. 
   * @param o2 Object representing the second <code>Solution</code>.
   * @return 0, if both solutions are equals with a certain dissimilarity, -1
   * otherwise.
   * @throws JMException 
   * @throws JMException 
   */
  public int compare(Object o1, Object o2) {
    Solution solution1, solution2;        
    solution1 = (Solution)o1;
    solution2 = (Solution)o2;
        
    if (solution1.numberOfVariables() != solution2.numberOfVariables())
      return -1;

    try {
      if ((new Distance()).distanceBetweenSolutions(solution1,solution2) < EPSILON)
        return 0;
    } catch (JMException e) {
      Configuration.logger_.severe("SolutionComparator.compare: JMException ") ; 
    }
                
    return -1;
  } // compare
} // SolutionComparator
