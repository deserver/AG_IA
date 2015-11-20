
package vrptw.spea.vrptw;

import vrptw.spea.baseS.Problem;
import vrptw.spea.baseS.Solution;
import vrptw.spea.baseS.Configuration.SolutionType_;
import vrptw.spea.baseS.Configuration.VariableType_;
import vrptw.spea.base.variable.Permutation;

import java.io.*;


public class VRPTW extends Problem {

    public int numberOfCust_;
    public int capacity;
    public double[][] distanceMatrix_;
    ClienteVRP [] customers;
    ClienteVRP cust,cust2;
    double x,y,dem,begin,end,servTime;
    double carga;
    int camiones = 1;
    double tiemposervicioGlobal = 0;
    


    public VRPTW(String filename) throws FileNotFoundException, IOException {
        numberOfVariables_ = 1;
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        problemName_ = "VRPTW";

        solutionType_ = SolutionType_.Permutation;

        variableType_ = new VariableType_[numberOfVariables_];
        length_ = new int[numberOfVariables_];

        variableType_[0] = Enum.valueOf(VariableType_.class, "Permutation");

        readProblem(filename);
        System.out.println(numberOfCust_);
        length_[0] = numberOfCust_;
        
     
    }


    
    public void evaluate(Solution solution) {
        double fitness1 = 0.0;


        for (int i = 0; i < (numberOfCust_ - 1); i++) {
            int x;
            int y;

            x = ((Permutation) solution.getDecisionVariables().variables_[0]).vector_[i];
            y = ((Permutation) solution.getDecisionVariables().variables_[0]).vector_[i + 1];

            fitness1 += distanceMatrix_[x][y];
          
        } // for

        int firstCity;
        int lastCity;

        firstCity = ((Permutation) solution.getDecisionVariables().variables_[0]).vector_[0];
        lastCity = ((Permutation) solution.getDecisionVariables().variables_[0]).vector_[numberOfCust_ - 1];
        fitness1 += distanceMatrix_[firstCity][lastCity];

        solution.setObjective(0, fitness1);
        solution.setObjective(1, camiones);
    } 
    

    public void readProblem(String fileName) throws FileNotFoundException,
            IOException {

        Reader inputFile = new BufferedReader(
                new InputStreamReader(
                new FileInputStream(fileName)));

        StreamTokenizer token = new StreamTokenizer(inputFile);
        try {
            token.nextToken();
            token.nextToken();
            
            numberOfCust_ = (int) token.nval;
            token.nextToken();
            token.nextToken();
            capacity = (int) token.nval;
           
            boolean found;
            found = false;
            token.nextToken();
            while (!found) {
                if ((token.sval != null) &&
                        ((token.sval.compareTo("SERVICE") == 0))) {
                    found = true;
                } else {
                    token.nextToken();
                }
            } 
            customers = new ClienteVRP[numberOfCust_];
            distanceMatrix_ = new double[numberOfCust_][numberOfCust_];
            cust= new ClienteVRP();
            cust2= new ClienteVRP();
            token.nextToken();
            token.nextToken();

            // Cargar objetivo 1
            for (int i=0; i<numberOfCust_; i++) {
                cust= new ClienteVRP();
               token.nextToken();
                cust.setX(token.nval);
                token.nextToken();
                cust.setY(token.nval);
                token.nextToken();
                cust.setDemanda(token.nval);
                token.nextToken();
                cust.setTimeStart(token.nval);
                token.nextToken();
                cust.setTimeEnd(token.nval);
                token.nextToken();
                cust.setServiceTime(token.nval);
                customers[i] = cust; 
                token.nextToken();
               
                
                /*if (!(tiemposervicioGlobal >= cust.getTimeStart())){
                	while (!(tiemposervicioGlobal >= cust.getTimeStart()))
                		tiemposervicioGlobal++;
                	}
                else if ((tiemposervicioGlobal+cust.getServiceTime() <= cust.getTimeEnd())){
                	 carga += cust.getDemanda();
                     tiemposervicioGlobal += cust.getServiceTime();
                     if (carga >= capacity){
                         camiones++;
                         carga = 0;
                     }
				}*/
                	
                carga += cust.getDemanda();
                if (carga >= capacity){
                    camiones++;
                    carga = 0;
                }
               
                


            } 
            
          double dist;
              for (int k = 0; k < numberOfCust_; k++) {
                distanceMatrix_[k][k] = 0;
                for (int j = k + 1; j < numberOfCust_; j++) {
                    cust = customers[k];
                    cust2 = customers[j];
                    dist = Math.sqrt(Math.pow((cust.getX() - cust2.getX()), 2) +
                            Math.pow(((cust.getY() - cust2.getY())), 2));
                  distanceMatrix_[k][j] = dist;
                  distanceMatrix_[j][k] = dist;
                }
              }
        } // try
        catch (Exception e) {
            System.err.println("VRPTW.readProblem(): error when reading data file " + e);
            System.exit(1);
        } // catch

    } 
} 
