
package vrptw.spea.archivo;

import vrptw.spea.baseS.SolutionSet;
import vrptw.spea.baseS.Solution;
import vrptw.spea.operadores.EqualSolutions;
import vrptw.spea.operadores.DominanceComparator;
import vrptw.spea.operadores.CrowdingDistanceComparator;
import java.util.Comparator;
import vrptw.spea.util.Distance;


public class CrowdingArchive extends SolutionSet {    
  
  
  private int maxSize_;
  
  
  private int objectives_;    
  
  
  private Comparator dominance_;
  
  
  private Comparator equals_; 

  
  private Comparator crowdingDistance_; 
  
  
  private Distance distance_; 
    
  
  public CrowdingArchive(int maxSize, int numberOfObjectives) {
    super(maxSize);
    maxSize_          = maxSize;
    objectives_       = numberOfObjectives;        
    dominance_        = new DominanceComparator();
    equals_           = new EqualSolutions();
    crowdingDistance_ = new CrowdingDistanceComparator();
    distance_         = new Distance();
    
  } // CrowdingArchive
    
  
  
  public boolean add(Solution solution){
    int flag = 0;
    int i = 0;
    Solution aux; //Store an solution temporally
    while (i < solutionsList_.size()){
      aux = solutionsList_.get(i);            
            
      flag = dominance_.compare(solution,aux);
      if (flag == 1) {               // The solution to add is dominated
        return false;                // Discard the new solution
      } else if (flag == -1) {       // A solution in the archive is dominated
        solutionsList_.remove(i);    // Remove it from the population            
      } else {
          if (equals_.compare(aux,solution)==0) { // There is an equal solution 
        	                                      // in the population
            return false; // Discard the new solution
          }  // if
          i++;
      }
    }
    // Insert the solution into the archive
    solutionsList_.add(solution);        
    if (size() > maxSize_) { // The archive is full
      distance_.crowdingDistanceAssignment(this,objectives_);
      sort(crowdingDistance_);
      //Remove the last
      remove(maxSize_);
    }        
    return true;
  } // add
} // CrowdingArchive
