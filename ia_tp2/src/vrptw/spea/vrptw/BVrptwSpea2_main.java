/**
 * BTspSpea2_main.java 
 *
 * @author Juan J. Durillo
 * @version 1.0
 */
package vrptw.spea.vrptw;

import vrptw.spea.baseS.Problem;
import vrptw.spea.baseS.Operator;
import vrptw.spea.baseS.SolutionSet;
import vrptw.spea.baseS.Configuration;
import vrptw.spea.baseS.Algorithm;
import vrptw.spea.otros.ProblemFactory;
import vrptw.spea.operadores.CrossoverFactory;
import vrptw.spea.operadores.MutationFactory;
import vrptw.spea.operadores.SelectionFactory;
import vrptw.spea.main.SPEA2;
import java.io.IOException;

import vrptw.spea.util.JMException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


public class BVrptwSpea2_main {

    public static Logger logger_;      // Logger object

    public static FileHandler fileHandler_; // FileHandler object


    /**
     * @param args Command line arguments. The first (optional) argument specifies 
     *             the problem to solve.
     * @throws JMException 
     */
    public static void main(String[] args) throws JMException, IOException {

        for (int i = 0; i < 20; i++) {
            Problem problem;         // The problem to solve

            Algorithm algorithm;         // The algorithm to use

            Operator crossover;         // Crossover operator

            Operator mutation;         // Mutation operator

            Operator selection;         // Selection operator

            //String name = "vrptw_rc101";
            String name = "vrptw_c101";
            String problemName = "src/vrptw/spea/instancias/";


            // Logger object and file to store log messages
            logger_ = Configuration.logger_;
            fileHandler_ = new FileHandler("SPEA2.log");
            logger_.addHandler(fileHandler_);

            if (args.length == 1) {
                Object[] params = {"Permutation"};
                problem = (new ProblemFactory()).getProblem(args[0], params);
            } // if
            else { // Default problem

                problem = new VRPTW(problemName + name + ".txt");

            } // else

            algorithm = new SPEA2(problem);

            // Algorithm params    
            algorithm.setInputParameter("populationSize", 10);
            algorithm.setInputParameter("archiveSize", 10);
            algorithm.setInputParameter("maxEvaluations", 100);

            // Mutation and Crossover for Real codification
            crossover = CrossoverFactory.getCrossoverOperator("TwoPointsCrossover");
            crossover.setParameter("probability", 0.7);
            crossover.setParameter("distribuitionIndex", 20.0);
            mutation = MutationFactory.getMutationOperator("SwapMutation");
            mutation.setParameter("probability", 0.3);
            mutation.setParameter("distributionIndex", 20.0);

            /* Mutation and Crossover Binary codification */
            /*
            crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover");                   
            crossover.setParameter("probability",0.9);                   
            mutation = MutationFactory.getMutationOperator("BitFlipMutation");                    
            mutation.setParameter("probability",1.0/80);
             */

            /* Selection Operator */
            selection = SelectionFactory.getSelectionOperator("BinaryTournament");

            // Add the operators to the algorithm
            algorithm.addOperator("crossover", crossover);
            algorithm.addOperator("mutation", mutation);
            algorithm.addOperator("selection", selection);

            // Execute the Algorithm
            long initTime = System.currentTimeMillis();
            SolutionSet population = algorithm.execute();
            long estimatedTime = System.currentTimeMillis() - initTime;

            // Result messages 
            logger_.info("Total execution time: " + estimatedTime);
            logger_.info("Objectives values have been writen to file FUN");
            population.printObjectivesToFile("parametros/FUN");
            logger_.info("Variables values have been writen to file VAR");
            population.printVariablesToFile("parametros/VAR");
            //population.printObjectivesToFile("c:\\instancias-parametros\\generado\\" + name + "-SPEA.txt");
            population.printObjectivesToFile("/home/sergio/" + name + "-SPEA.txt");
            

        }
    }//main
} // BTspSpea2_main.java
