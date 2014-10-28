package intfcMatlab;

import java.util.Arrays;

public class MatlabHelper {

	public static java.lang.String varNameM;
	public static java.lang.Object valM;
	public static java.lang.String commandMatM;
	//public static java.lang.String funcM;
	//public static int nargoutMat;
	public static double resultM;
	//public static java.lang.Object [] resArrM;
	public static int timeN = 10;
	
	public static void runMatSim1(accessControlMatlab matSimObj){
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

	public static void runMatSim2(accessControlMatlab matSimObj){
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
	
	public static void runMatSim3(accessControlMatlab matSimObj){
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
	    double val = ((double[]) matSimObj.returningEvalMat("array(2,2)", 1)[0])[0];
	    System.out.println("Result: " + val);
	}
	public static void main(String[] args) {
		accessControlMatlab matHandleObj = new accessControlMatlab();
		matHandleObj.createMatInstance();
		runMatSim1(matHandleObj);
		runMatSim2(matHandleObj);
		runMatSim3(matHandleObj);
		matHandleObj.exitMat();
	}

}