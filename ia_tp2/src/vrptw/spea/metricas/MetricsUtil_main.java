/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vrptw.spea.metricas;

import vrptw.spea.baseS.Solution;
import vrptw.spea.baseS.SolutionSet;

/**
 *
 * @author Internet Designer
 */
public class MetricsUtil_main {

    public static void main(String [] args) {



        MetricsUtil metricsUtils = new MetricsUtil();

        SolutionSet solutionSet = metricsUtils.readNonDominatedSolutionSet("src/files/algorithms/frontpareto/resultado_frente_tsp_spea.txt");


        ConjuntoPareto pareto = new ConjuntoPareto();

        for (int i = 0; i < solutionSet.size(); i++) {

                double solObjetivo1 = ((Solution) solutionSet.get(i)).getObjective(0);
                double solObjetivo2 = ((Solution) solutionSet.get(i)).getObjective(1);

                if (pareto.agregarNoDominado(solObjetivo1, solObjetivo2) == 1) {
                    pareto.eliminarDominados(solObjetivo1, solObjetivo2);
                }
                
            }

        pareto.solutionSet.printObjectivesToFile("src/files/algorithms/frontparetopromedio/frontparetopromediospeatsp.txt");

    }

    
}
