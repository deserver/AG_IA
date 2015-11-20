
package vrptw.spea.archivo;

import vrptw.spea.baseS.SolutionSet;
import vrptw.spea.baseS.Solution;
import vrptw.spea.operadores.DominanceComparator;
import vrptw.spea.util.AdaptiveGrid;
import java.util.*;


public class AdaptiveGridArchive extends SolutionSet {  
  
  
  private AdaptiveGrid grid_;
  
 
  private int maxSize_;
  
  
  private Comparator dominance_;
    
  
  public AdaptiveGridArchive(int maxSize,int bisections, int objectives) {
    super(maxSize);
    maxSize_   = maxSize;
    dominance_ = new DominanceComparator();
    grid_      = new AdaptiveGrid(bisections,objectives);
  } 
  public boolean add(Solution solution) {
    
    Iterator<Solution> iterator = solutionsList_.iterator();
        
    while (iterator.hasNext()){
      Solution element = iterator.next();
      int flag = dominance_.compare(solution,element);
      if (flag == -1) { 
        iterator.remove(); 
        int location = grid_.location(element);
        if (grid_.getLocationDensity(location) > 1) {
          grid_.removeSolution(location);            
        } else {
          grid_.updateGrid(this);
        } 
      } 
      else if (flag == 1) { 
        return false; 
      } // else if           
    } // while
        
   
    if (size() == 0){ 
      solutionsList_.add(solution);
      grid_.updateGrid(this);        
      return true;
    } //
        
    if (size() < maxSize_){              
      grid_.updateGrid(solution,this); 
      int location ;
      location= grid_.location(solution);
      grid_.addSolution(location); 
      solutionsList_.add(solution); 
      return true;
    } 
    grid_.updateGrid(solution,this);
    int location = grid_.location(solution);
    if (location == grid_.getMostPopulated()) { 
      return false; 
    } else {
     
      iterator = solutionsList_.iterator();
      boolean removed = false;
      while (iterator.hasNext()) {
        if (!removed) {
          Solution element = iterator.next();
          int location2 = grid_.location(element);
          if (location2 == grid_.getMostPopulated()) {
            iterator.remove();
            grid_.removeSolution(location2);
          } // if
        } // if
      } // while
      
      
      grid_.addSolution(location);
      solutionsList_.add(solution);            
    }
    return true;
  } // add
    
  
  public AdaptiveGrid getGrid() {
    return grid_;
  } 
} 