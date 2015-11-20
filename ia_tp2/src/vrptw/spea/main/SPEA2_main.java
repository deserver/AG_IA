
package vrptw.spea.main;

import vrptw.spea.baseS.Problem;
import vrptw.spea.baseS.Operator;
import vrptw.spea.baseS.SolutionSet;
import vrptw.spea.baseS.Configuration;
import vrptw.spea.baseS.Algorithm;
import vrptw.spea.otros.Kursawe;
import vrptw.spea.otros.ProblemFactory;
import vrptw.spea.operadores.CrossoverFactory;
import vrptw.spea.operadores.MutationFactory;
import vrptw.spea.operadores.SelectionFactory;
import java.io.IOException;

import vrptw.spea.util.JMException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class SPEA2_main {
  public static Logger      logger_ ;      
  public static FileHandler fileHandler_ ; 
  
  
  public static void main(String [] args) throws JMException, IOException {
    Problem   problem   ;         // El problema a Resolver
    Algorithm algorithm ;         // Algoritmo A ser utilizado
    Operator  crossover ;         // Crossover 
    Operator  mutation  ;         // Mutacion
    Operator  selection ;         // Seleccion de operadores
        
    
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("SPEA2.log"); 
    logger_.addHandler(fileHandler_) ;
    
    if (args.length == 1) {
      Object [] params = {"Real"};
      problem = (new ProblemFactory()).getProblem(args[0],params);
    } // if
    else { 
      problem = new Kursawe(3, "Real"); 
     
    } 
    
    algorithm = new SPEA2(problem);
    
   
    algorithm.setInputParameter("Cantidad Poblacion",100);
    algorithm.setInputParameter("tamaño del archivo",100);
    algorithm.setInputParameter("cantidad maxima de evalucion",25000);
      
    // Mutation and Crossover for Real codification
    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover");                   
    crossover.setParameter("probabilidad",0.9);                   
    crossover.setParameter("indice de distribucion",20.0);
    mutation = MutationFactory.getMutationOperator("mutacionpolinomial");                    
    mutation.setParameter("probabilidad",1.0/problem.getNumberOfVariables());
    mutation.setParameter("distribucionindice",20.0);
    
   
 
    selection = SelectionFactory.getSelectionOperator("BinaryTournament") ;                           
    
 
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutacion",mutation);
    algorithm.addOperator("seleccion",selection);
    
   
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;

       
    logger_.info("Tiempo de Ejecucion Total: " + estimatedTime);
    logger_.info("Valores objetivos funcionales ");
    population.printObjectivesToFile("Funcionales");
    logger_.info("Valores de variablesVariables ");
    population.printVariablesToFile("VAR");      
  }
} 
