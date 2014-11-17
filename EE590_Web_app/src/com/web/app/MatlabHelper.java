package intfcMatlab;
import org.json.simple.JSONObject;

import java.util.Arrays;

public class MatlabHelper {
	
	/*Instance variables*/
	private double prrThres;
	private double gridUnit;
	private double pathLossExponent;
	private double shadowingStdDev;
	private double temporalCorrCoef;
	private double spatialCorrCoef;
	private double terrainDimX; 
	private double terrainDimY;
	private double [][] xyArrThis; 
	private int numNodes;
	private int topologyType; 
	private int simTimeSteps;
	private int modulation;
	private int encScheme;
	private int preambleLen;
	private int frameLen;
	
	public JSONObject simDataObj = new JSONObject();
	
	public MatlabHelper(double prrThresVal,double gridUnitVal,double pathLossExponentVal,double shadowingStdDevVal,double temporalCorrCoefVal,double spatialCorrCoefVal,double terrainDimXVal,double terrainDimYVal,double [][] XYlocArr,int numNodesVal,int topologyTypeVal,int simTimeStepsVal,int modulationVal,int encSchemeVal,int preambleLenVal,int frameLenVal){
		/*Instance variables*/
		prrThres = prrThresVal;
		gridUnit = gridUnitVal;
		pathLossExponent = pathLossExponentVal;
		shadowingStdDev = shadowingStdDevVal;
		temporalCorrCoef = temporalCorrCoefVal;
		spatialCorrCoef = spatialCorrCoefVal;
		terrainDimX = terrainDimXVal; 
		terrainDimY = terrainDimYVal;
		xyArrThis = XYlocArr;
		//xArr1 = XlocArr;
		//yArr2 = YlocArr;
		
		numNodes = numNodesVal;
	    topologyType = topologyTypeVal; 
		simTimeSteps = simTimeStepsVal;
		modulation = modulationVal;
		encScheme = encSchemeVal;
		preambleLen = preambleLenVal;
		frameLen = frameLenVal;
		//xyArrThis = new double [numNodes][2];
		System.out.println("numNodes" + numNodes);
		
		/* Fill the XY co-ordinates array */
		/*
		for(int i = 0; i < numNodes; i++)
		{	
			for(int j = 0; j < 2; j++)
			{
				xyArrThis[i][j] = i*j;
			}
			
		}
		*/
	
  }
	
	public JSONObject runMatLinkLayerSim()
	{
		
		/* First Check if the an active Matlab instance is there ? */
		/* Write the control code here */
		AccessControlMatlab matHandleObj = new AccessControlMatlab();
		matHandleObj.createMatInstance();
		
		Object[] returnArg;
		double [] outputArr1;
		double [] outputArr2;
		double [] outputArr3;
		double [] outputArr4;
		
		matHandleObj.createMatTypeConverter();
		
		matHandleObj.matSetNumericArray("xyArray",xyArrThis);
		returnArg = matHandleObj.returningFevalMat("LinkLayerModel", 4, simTimeSteps,xyArrThis, pathLossExponent,shadowingStdDev,modulation,encScheme,preambleLen,frameLen,numNodes,terrainDimX,terrainDimY,topologyType,gridUnit,temporalCorrCoef, spatialCorrCoef);
		
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
		for(int count = 0; count < numNodes; count++){
			System.out.print(outputArr1[count]+"\t");
			System.out.println(outputArr1[count+numNodes]+"\t");
		}
		
		/* Write each link-link stats on the stdout*/
		
		System.out.println("============= Links Stats ===============");
		int count = 0;
        for(int simCount = 1; simCount <= simTimeSteps; simCount++) // Time step loop
        {
        	System.out.printf("\n At time t%d",simCount);
        	System.out.printf("\n\tPRR \tRSSI \tProb Of Error\n");
        	for(int linkCountR = 1; linkCountR <= numNodes; linkCountR++)
        	{	
        		for(int linkCountC = 1; linkCountC <= numNodes; linkCountC++)
            	{
	    			System.out.printf("link%d -> link%d : ",linkCountR,linkCountC);
	    			System.out.print(outputArr2[count]+"\t "+ outputArr3[count]+"\t "+ outputArr4[count]+"\n");
	    			count++;
            	}
    		}
        }
        
        /* Create JSON data */
        CreateNodeLinkStatsJson nodeLinkStatsJsonObj = new CreateNodeLinkStatsJson(numNodes,numNodes*numNodes,simTimeSteps,prrThres,outputArr1,outputArr2,outputArr3,outputArr4);
        simDataObj = nodeLinkStatsJsonObj.constructJsonData();
        nodeLinkStatsJsonObj.writeJsonDataFile();
        nodeLinkStatsJsonObj.printJsonDataObj();
        
        matHandleObj.exitMat();
        
        return simDataObj;
	}
	
}