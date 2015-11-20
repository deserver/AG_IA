
package vrptw.spea.main;

import vrptw.spea.baseS.Problem;
import vrptw.spea.baseS.Operator;
import vrptw.spea.baseS.SolutionSet;
import vrptw.spea.baseS.Solution;
import vrptw.spea.baseS.Algorithm;
import vrptw.spea.util.JMException;
import vrptw.spea.util.Ranking;
import vrptw.spea.util.Spea2Fitness;


public class SPEA2 extends Algorithm{
          
 
  public static final int TOURNAMENTS_ROUNDS = 1;

  
  private Problem problem_;    

  
  public SPEA2(Problem problem) {                
    this.problem_ = problem;        
  } // Spea2
   
 
  public SolutionSet execute() throws JMException{   
    int populationSize, archiveSize, maxEvaluations, evaluations;
    Operator crossoverOperator, mutationOperator, selectionOperator;
    SolutionSet solutionSet, archive, offSpringSolutionSet;    
    
    
    populationSize = ((Integer)getInputParameter("populationSize")).intValue();
    archiveSize    = ((Integer)getInputParameter("archiveSize")).intValue();
    maxEvaluations = ((Integer)getInputParameter("maxEvaluations")).intValue();
        
    
    crossoverOperator = operators_.get("crossover");
    mutationOperator  = operators_.get("mutation");
    selectionOperator = operators_.get("selection");        
    
    solutionSet  = new SolutionSet(populationSize);
    archive     = new SolutionSet(archiveSize);
    evaluations = 0;
        
    
    Solution newSolution;
    for (int i = 0; i < populationSize; i++) {
      newSolution = new Solution(problem_);
      problem_.evaluate(newSolution);            
      problem_.evaluateConstraints(newSolution);
      evaluations++;
      solutionSet.add(newSolution);
    }                        
        
    while (evaluations < maxEvaluations){               
      SolutionSet union = ((SolutionSet)solutionSet).union(archive);
      Spea2Fitness spea = new Spea2Fitness(union);
      spea.fitnessAssign();
      archive = spea.environmentalSelection(archiveSize);                       
     
      offSpringSolutionSet= new SolutionSet(populationSize);    
      Solution  [] parents = new Solution[2];
      while (offSpringSolutionSet.size() < populationSize){           
        int j = 0;
        do{
          j++;                
          parents[0] = (Solution)selectionOperator.execute(archive);
        } while (j < SPEA2.TOURNAMENTS_ROUNDS);                    
        int k = 0;
        do{
          k++;                
          parents[1] = (Solution)selectionOperator.execute(archive);
        } while (k < SPEA2.TOURNAMENTS_ROUNDS); 
        
        
        Solution [] offSpring = (Solution [])crossoverOperator.execute(parents);            
        mutationOperator.execute(offSpring[0]);            
        problem_.evaluate(offSpring[0]);
        problem_.evaluateConstraints(offSpring[0]);            
        offSpringSolutionSet.add(offSpring[0]);
        evaluations++;
      } 
      solutionSet = offSpringSolutionSet;                   
    } 
        
    Ranking ranking = new Ranking(archive);
    return ranking.getSubfront(0);
  }     
} 
