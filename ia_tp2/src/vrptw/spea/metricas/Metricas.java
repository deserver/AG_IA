package vrptw.spea.metricas;

import vrptw.spea.baseS.Solution;
import vrptw.spea.baseS.SolutionSet;

/**
 *
 * @author Internet Designer
 */
public class Metricas {

    ConjuntoPareto pareto1;
    ConjuntoPareto pareto2;

    public static void main(String[] args) {

        Metricas miPrueba = new Metricas();
        String ruta = "parametros/generados/";
        String[] arrayAlgoritmoEjecucion = {"MOACS", "M3AS", "NSGA", "SPEA"}; 
        int decimales = 4;
        //String pr = "vrptw_rc101"; 
        String pr = "vrptw_c101";
        String cadenaYtrue = ruta + "YTRUE-" + pr + ".txt";

        double[] distanciaFinal = new double[arrayAlgoritmoEjecucion.length];
        double[] extension = new double[arrayAlgoritmoEjecucion.length];
        double[] distribucion = new double[arrayAlgoritmoEjecucion.length];
        double[] error = new double[arrayAlgoritmoEjecucion.length];

        System.out.println("********RESULTADO*******");
        System.out.println("\tAlgo\tDista\tDistra\tExt\tError");
        for (int i = 0; i < arrayAlgoritmoEjecucion.length; i++) {
        	if (i == 3){
	            String algoritmoEjecucion = arrayAlgoritmoEjecucion[i];
	            String cadAlgoritmo = ruta + pr + "-" + algoritmoEjecucion + ".txt";
	            System.out.print("\t" + algoritmoEjecucion + "\t");
	
	            //1RA. Metrica: Distancia al Ytrue
	            String fileYtrue = cadenaYtrue; //N ejecuciones de los 4 algoritmos
	            String fileYprima = cadAlgoritmo;          //N ejecuciones de un solo algoritmo
	
	            distanciaFinal[i] = miPrueba.getDistanciaYTrue(fileYtrue, fileYprima);
	            System.out.print(Truncar(distanciaFinal[i], decimales) + "\t");
	
	            //2DA. Metrica: Distribucion
	            distribucion[i] = miPrueba.getDistribucion(fileYprima);
	            System.out.print(Truncar(distribucion[i], decimales) + "\t");
	
	            //3RA. Metrica: Extension
	
	            extension[i] = miPrueba.getExtension(fileYprima);
	            System.out.print(Truncar(extension[i], decimales) + "\t");
	            
	            //4TA. Metrica: Error
	            
	            error[i] = miPrueba.getErrorYTrue(fileYtrue, fileYprima);
	            System.out.print(Truncar(error[i], decimales) + "\n");
	
	            miPrueba.GenerarYTrue(fileYprima, ruta + algoritmoEjecucion);
        	}
        }
        miPrueba.GenerarYTrue(cadenaYtrue, ruta + "YTRUE");


    }

    public static double Truncar(double nD, int nDec) {
                if (nD > 0) {
        nD = Math.floor(nD * Math.pow(10, nDec)) / Math.pow(10, nDec);
        } else {
        nD = Math.ceil(nD * Math.pow(10, nDec)) / Math.pow(10, nDec);
        }
        return nD;
    }

    public static double maximoValor(double[] miVector) {
        double miMaximo = Double.MIN_VALUE;
        for (int i = 0; i < miVector.length; i++) {
            if (miMaximo < miVector[i]) {
                miMaximo = miVector[i];
            }
        }
        return miMaximo;
    }

    public double getDistribucion(String fileYTrue) {
        MetricsUtil metricsUtils = new MetricsUtil();

        pareto1 = GenerarYTrue(fileYTrue);

        SolutionSet yTrueSolutionSet = pareto1.getSolutionSet();

        double sumaCantidadMaxima = 0.0;
        double rho = 0.1 * getExtension(fileYTrue);
        for (int i = 0; i < pareto1.solutionSet.size(); i++) {

            Solution solutionYtrue = yTrueSolutionSet.get(i);
            double[] yTruePunto = {solutionYtrue.getObjective(0), solutionYtrue.getObjective(1)};

            double cantidadMaxima = 0.0;
            for (int j = 0; j < pareto1.solutionSet.size(); j++) {

                Solution solutionYPrima = yTrueSolutionSet.get(j);
                double[] yPrimaPunto = {solutionYPrima.getObjective(0), solutionYPrima.getObjective(1)};


                Double aux = metricsUtils.distance(yTruePunto, yPrimaPunto);

                if (aux > rho) {
                    cantidadMaxima++;
                }


            }
            sumaCantidadMaxima = sumaCantidadMaxima + cantidadMaxima;

        }

        double distanciaFinal = (1.0 / yTrueSolutionSet.size()) * sumaCantidadMaxima;

        return distanciaFinal;

    }

    public double getExtension(String fileYTrue) {
        MetricsUtil metricsUtils = new MetricsUtil();

        pareto2 = GenerarYTrue(fileYTrue);

        SolutionSet yTrueSolutionSet = pareto2.getSolutionSet();
        Solution solutionYPrima;
        Solution solutionYtrue;
        double sumaDistanciaMaxima = 0.0;
        for (int i = 0; i < pareto2.solutionSet.size(); i++) {

            solutionYtrue = yTrueSolutionSet.get(i);
            double[] yTruePunto = {solutionYtrue.getObjective(0), solutionYtrue.getObjective(1)};

            double distanciaMaxima = Double.MIN_VALUE;
            for (int j = 0; j < pareto2.solutionSet.size(); j++) {

                solutionYPrima = yTrueSolutionSet.get(j);
                double[] yPrimaPunto = {solutionYPrima.getObjective(0), solutionYPrima.getObjective(1)};


                Double aux = metricsUtils.distance(yTruePunto, yPrimaPunto);

                if (distanciaMaxima < aux) {
                    distanciaMaxima = aux;
                }


            }
            sumaDistanciaMaxima = sumaDistanciaMaxima + distanciaMaxima;

        }

        double distanciaFinal = Math.sqrt(sumaDistanciaMaxima);

        return distanciaFinal;

    }

    public double getDistanciaYTrue(String fileYTrue, String fileYPrima) {
        MetricsUtil metricsUtils = new MetricsUtil();

        pareto1 = GenerarYTrue(fileYTrue);
        pareto2 = GenerarYTrue(fileYPrima);

        SolutionSet yTrueSolutionSet = pareto1.getSolutionSet();
        SolutionSet yPrimaSolutionSet = pareto2.getSolutionSet();

        double sumaDistanciaMinima = 0.0;
        for (int i = 0; i < pareto1.solutionSet.size(); i++) {

            Solution solutionYtrue = yTrueSolutionSet.get(i);
            double[] yTruePunto = {solutionYtrue.getObjective(0), solutionYtrue.getObjective(1)};

            double distanciaMinima = Double.MAX_VALUE;
            for (int j = 0; j < pareto2.solutionSet.size(); j++) {

                Solution solutionYPrima = yPrimaSolutionSet.get(j);
                double[] yPrimaPunto = {solutionYPrima.getObjective(0), solutionYPrima.getObjective(1)};


                Double aux = metricsUtils.distance(yTruePunto, yPrimaPunto);

                if (distanciaMinima > aux) {
                    distanciaMinima = aux;
                }

            }
            sumaDistanciaMinima = sumaDistanciaMinima + distanciaMinima;

        }

        double distanciaFinal = (1.0 / (yTrueSolutionSet.size())) * sumaDistanciaMinima;

        return distanciaFinal;

    }
    
    public double getErrorYTrue(String fileYTrue, String fileYPrima){
        MetricsUtil metricsUtils = new MetricsUtil();

        pareto1 = GenerarYTrue(fileYTrue);
        pareto2 = GenerarYTrue(fileYPrima);
        
        SolutionSet yTrueSolutionSet = pareto1.getSolutionSet();
        SolutionSet yPrimaSolutionSet = pareto2.getSolutionSet();
        
        double acumulador = 0;
        for (int i = 0; i < pareto1.solutionSet.size(); i++) {

            Solution solutionYtrue = yTrueSolutionSet.get(i);
            double[] yTruePunto = {solutionYtrue.getObjective(0), solutionYtrue.getObjective(1)};

            double distanciaMinima = Double.MAX_VALUE;
            for (int j = 0; j < pareto2.solutionSet.size(); j++) {

                Solution solutionYPrima = yPrimaSolutionSet.get(j);
                double[] yPrimaPunto = {solutionYPrima.getObjective(0), solutionYPrima.getObjective(1)};
                

                 if (yTruePunto[0] == yPrimaPunto[0] && yTruePunto[1] == yPrimaPunto[1]){
                	 acumulador++;
                 }

            }

        }
        
        double error = 1.0 - ( (1.0 / (yPrimaSolutionSet.size())) * acumulador);
        
        
        
    	return error;
    }

    public ConjuntoPareto GenerarYTrue(String file) {
        MetricsUtil metricsUtils = new MetricsUtil();

        SolutionSet solutionSet = metricsUtils.readNonDominatedSolutionSet(file);


        ConjuntoPareto pareto = new ConjuntoPareto();

        for (int i = 0; i < solutionSet.size(); i++) {

            double solObjetivo1 = ((Solution) solutionSet.get(i)).getObjective(0);
            double solObjetivo2 = ((Solution) solutionSet.get(i)).getObjective(1);

            if (pareto.agregarNoDominado(solObjetivo1, solObjetivo2) == 1) {
                pareto.eliminarDominados(solObjetivo1, solObjetivo2);
            }

        }
        pareto.solutionSet.printObjectivesToFile(file + "-OPTIMO");
        return pareto;
    }

    public ConjuntoPareto GenerarYTrue(String file, String algorithm) {
        MetricsUtil metricsUtils = new MetricsUtil();

        SolutionSet solutionSet = metricsUtils.readNonDominatedSolutionSet(file);


        ConjuntoPareto pareto = new ConjuntoPareto();

        for (int i = 0; i < solutionSet.size(); i++) {

            double solObjetivo1 = ((Solution) solutionSet.get(i)).getObjective(0);
            double solObjetivo2 = ((Solution) solutionSet.get(i)).getObjective(1);

            if (pareto.agregarNoDominado(solObjetivo1, solObjetivo2) == 1) {
                pareto.eliminarDominados(solObjetivo1, solObjetivo2);
            }

        }
        pareto.solutionSet.printObjectivesToFile(algorithm + "-OPTIMO.csv", true);
        return pareto;
    }

    public double getCardinalidad(String fileYPrima) {
        pareto2 = GenerarYTrue(fileYPrima);
        return pareto2.solutionSet.size();
    }
}
