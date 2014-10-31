package intfcMatlab;

import java.util.Arrays;
/*
 * 
 * 
 * Remove unused variables, imports. 
 * Access static methods in a static way i.e. dont refernce a class and access the static method. Just classname.method
 * Static key word should be used only when you want to persist data even after termination of code. Think on this line, else memory will wasted.
 * 
 * 
 * 
 * */
public class MatlabHelper {

	public java.lang.String varNameM;
	public java.lang.Object valM;
	public  java.lang.String commandMatM;
	//public java.lang.String funcM;
	//public int nargoutMat;
	public double resultM;
	//public java.lang.Object [] resArrM;
	public int timeN = 10;
	
	public void runMatSim1(AccessControlMatlab matSimObj){
		matSimObj.evalMat("clear");
		varNameM = "a";
		valM = 5;
		commandMatM = "a=a+6";
		
			for (int i = 0; i < 5; i++) {
				matSimObj.setMatVariable(varNameM, valM);
				matSimObj.evalMat(commandMatM);
				resultM = ((double[])matSimObj.getMatVariable(varNameM))[0];
				System.out.println("Result: " + resultM);
				//accessControlMatlab.evalMat("clc");
				matSimObj.sleepM(timeN);
			}
	}

	public void runMatSim2(AccessControlMatlab matSimObj){
		matSimObj.evalMat("clear");
		varNameM = "b";
		valM = 5;
		commandMatM = "b=b*2";
		
			for (int i = 0; i < 5; i++) {
				matSimObj.setMatVariable(varNameM, valM);
				matSimObj.evalMat(commandMatM);
				resultM = ((double[])matSimObj.getMatVariable(varNameM))[0];
				System.out.println("Result: " + resultM);
				//accessControlMatlab.evalMat("clc");
				matSimObj.sleepM(timeN);
			}
	}
	
	public void runMatSim3(AccessControlMatlab matSimObj){
		matSimObj.evalMat("clear");
		//Create an array for this example
		matSimObj.evalMat("array = magic(3)");
		//Invoke eval, specifying 1 argument to be returned - arguments are returned as an array
	    Object[] returnArguments = matSimObj.returningEvalMat("array(2,2)", 1);
	    //Retrieve the first (and only) element from the returned arguments
	    Object firstArgument = returnArguments[0];
	    //Like before, cast and index to retrieve the double value
	    double innerValue = ((double[]) firstArgument)[0];
	    //Print the result
	    System.out.println("Result: " + innerValue);

	    //Or all in one step
	    //double val = ((double[]) matSimObj.returningEvalMat("array(2,2)", 1)[0])[0];
	    //System.out.println("Result: " + val);
	}
	
	public void runMatSim4(AccessControlMatlab matSimObj)
	{
		
        double [] randArr;
        Object[] returnArg;
        
        matSimObj.evalMat("clear");
	    //By specifying 3 return arguments, returns as String arrays the loaded M-files, MEX files, and Java classes
	    Object[] inmem = matSimObj.returningFevalMat("inmem", 3);
	    System.out.println("Java classes loaded:");
	    System.out.println(Arrays.toString((String[]) inmem[2]));
	    
	    //Retrieve MATLAB's release date by providing the -date argument
	    //returnArg = matSimObj.returningFevalMat("ones", 1, 1, 5);
	    //randArr = (double[])returnArg[0];
	    //System.out.println("randn array values");
	    //System.out.println("Array Length:"+ randArr.length);
	   // for(double x: randArr){
			//   System.out.println(x);
		  // }
	    
	  //matSimObj.fevalMat("path","path","C:\\MatlabSim");
	  returnArg = matSimObj.returningFevalMat("summerObj", 1, 2, 4);
	  randArr = (double[])returnArg[0];
	  System.out.println("sum result: "+ randArr[0]);
	    
	}
	/*Return double array*/
	public void runMatSim5(AccessControlMatlab matSimObj)
	{
		
        double [] outputArr;
        Object[] returnArg;
        int NUMBER_OF_NODES = 4; 
        int SIM_TIME = 4;
        //matSimObj.evalMat("clear");
        returnArg = matSimObj.returningFevalMat("prrLinkSim", 1, NUMBER_OF_NODES, SIM_TIME);
        outputArr = (double[])returnArg[0];
        
        System.out.println("PRR for Links");
        /*
        for(double x: outputArr){
        	System.out.println(x);
        }*/
        int count = 0;
        for(int simCount = 1; simCount <= SIM_TIME; simCount++)
        {
        	System.out.printf("\nPRR for Links at time t%d\n",simCount);
        	for(int linkCountR = 1; linkCountR <= NUMBER_OF_NODES; linkCountR++)
        	{	
        		for(int linkCountC = 1; linkCountC <= NUMBER_OF_NODES; linkCountC++)
            	{
	    			System.out.printf("link%d -> link%d : ",linkCountR,linkCountC);
	    			System.out.print(outputArr[count++]+"\n");
            	}
    		}
        }
		 
	}
	
	public void runMatLinkLayerSim(AccessControlMatlab matSimObj)
	{
		Object[] returnArg;
		double [] outputArr1;
		double [] outputArr2;
		double [] outputArr3;
		double [] outputArr4;
		
		double PATH_LOSS_EXPONENT = 4.7;
		double SHADOWING_STANDARD_DEVIATION = 8.0;
		int MODULATION = 3;
		int ENCODING = 3;
		int PREAMBLE_LENGTH = 2;
		int FRAME_LENGTH = 50;
		int NUMBER_OF_NODES = 25; 
		double TERRAIN_DIMENSIONS_X = 25.0; 
		double TERRAIN_DIMENSIONS_Y = 25.0;
		int TOPOLOGY = 2; 
		double GRID_UNIT = 3.0;

		double TEMPORAL_CORRELATION_COEFFICIENT = 0.5;
		double SPATIAL_CORRELATION_COEFFICIENT = 0.5;
		
		returnArg = matSimObj.returningFevalMat("LinkLayerModel", 4, PATH_LOSS_EXPONENT,SHADOWING_STANDARD_DEVIATION,MODULATION,ENCODING,PREAMBLE_LENGTH,FRAME_LENGTH,NUMBER_OF_NODES,TERRAIN_DIMENSIONS_X,TERRAIN_DIMENSIONS_Y,TOPOLOGY,GRID_UNIT,TEMPORAL_CORRELATION_COEFFICIENT, SPATIAL_CORRELATION_COEFFICIENT);
		
		System.out.println("runMatLinkLayerSim Completed");
		
		/* Handle for Topology array */
		outputArr1 = (double[])returnArg[0];
		/* Handle for PRR array */
		outputArr2 = (double[])returnArg[1];
		/* Handle for RSSI array */
		outputArr3 = (double[])returnArg[2];
		/* Handle for PE array */
		outputArr4 = (double[])returnArg[3];
		
		/* Write the topology on the stdout*/
		System.out.println("Topology:\n\tx\t\t\ty");
		for(int count = 0; count < NUMBER_OF_NODES; count++){
			System.out.print(outputArr1[count]+"\t");
			System.out.println(outputArr1[count+NUMBER_OF_NODES]+"\t");
		}
		
		/* Write each link-link stats on the stdout*/
		
		System.out.println("============= Links Stats ===============");
		int count = 0;
        for(int simCount = 1; simCount <= 100; simCount++)
        {
        	System.out.printf("\n At time t%d",simCount);
        	System.out.printf("\n\tPRR \tRSSI \tProb Of Error\n");
        	for(int linkCountR = 1; linkCountR <= NUMBER_OF_NODES; linkCountR++)
        	{	
        		for(int linkCountC = 1; linkCountC <= NUMBER_OF_NODES; linkCountC++)
            	{
	    			System.out.printf("link%d -> link%d : ",linkCountR,linkCountC);
	    			System.out.print(outputArr2[count]+"\t "+ outputArr3[count]+"\t "+ outputArr4[count]+"\n");
	    			count++;
            	}
    		}
        }
	}
	
	public static void main(String[] args) {
		AccessControlMatlab matHandleObj = new AccessControlMatlab();
		matHandleObj.createMatInstance();
		MatlabHelper matObj = new MatlabHelper();
		//matObj.runMatSim1(matHandleObj);
		//matObj.runMatSim2(matHandleObj);
		//matObj.runMatSim3(matHandleObj);
		//matObj.runMatSim4(matHandleObj);
		//matObj.runMatSim5(matHandleObj);
		matObj.runMatLinkLayerSim(matHandleObj);
		//matHandleObj.exitMat();
	}

}