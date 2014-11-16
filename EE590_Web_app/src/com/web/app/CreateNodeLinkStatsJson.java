package intfcMatlab;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CreateNodeLinkStatsJson {
	
	private int numNodes;
    private int numEdges;
    private int nodeSize;
    private int numSimTimeSteps;
    private double linkPrrThresVal; 
    private double [] arrXY;
    private double [] arrPRR;
    private double [] arrRSSI;
    private double [] arrPE;
    
    JSONObject outerObj = new JSONObject();
    JSONObject timeObj   = new JSONObject();
	JSONArray nodeElemList = new JSONArray();
	JSONArray edgeElemList = new JSONArray();
    
    public CreateNodeLinkStatsJson(int nNodes, int nEdges, int nSimTimeSteps, double prrLinkThres, double [] xyArr,double [] prrArr,double [] rssiArr,double [] peArr ){
    	numNodes = nNodes;
    	numEdges = nEdges;
    	numSimTimeSteps = nSimTimeSteps;
    	linkPrrThresVal = prrLinkThres; 
    	arrXY    = xyArr;
    	arrPRR   = prrArr;
    	arrRSSI  = rssiArr;
    	arrPE    = peArr;
    	nodeSize = 3; 	
    }
    
    public void constructJsonData(){
    	
    	int edgeCount = 0;
    	String nodeId; 
    	String nodeLabel; 
    	String nodeColor;
    	String edgeColor;
		
    	String edgeId; 
    	String srcNode; 
    	String destNode; 
    	
    	int rcolor;
    	int gcolor;
    	int bcolor;
    	int nSteps = 4;
    	double perfectPrr = 1.0;
    	double incPrrLinkStepVal;
    	
    	for(int simCount = 1; simCount <= numSimTimeSteps; simCount++){

		for(int i = 0; i < numNodes; i++) {
			
			rcolor = 0;
			gcolor = 0;
			bcolor = 255;
			
			JSONObject nodeObj = new JSONObject();
			nodeId = String.format("n%d", i);
			nodeLabel = String.format("node %d", i);
			nodeColor = String.format("rgb(%d,%d,%d)", rcolor,gcolor,bcolor);
			
			nodeObj.put("id", nodeId);
			nodeObj.put("label", nodeLabel);
			nodeObj.put("x", arrXY[i]);
			nodeObj.put("y", arrXY[i+numNodes]);
			nodeObj.put("size", nodeSize);
			nodeObj.put("color", nodeColor);
			nodeElemList.add(nodeObj);
		}
		
	    for(int i = 0; i < numNodes; i++) {
	    	
	    	for(int j = 0; j < numNodes; j++) {
			
			    if(arrPRR[edgeCount] > linkPrrThresVal){
	    		JSONObject edgeObj = new JSONObject();
			    edgeId = String.format("e%d", edgeCount);
			    srcNode = String.format("n%d", i);
			    destNode = String.format("n%d", j);
			    
			    incPrrLinkStepVal = (perfectPrr - linkPrrThresVal)/nSteps;
			    
			    //System.out.println("thresPrrval:"+linkPrrThresVal);
		    	//System.out.println("incPrrval:"+incPrrLinkStepVal);
		    	
			    /* First Range */
			    if((arrPRR[edgeCount]>linkPrrThresVal) && (arrPRR[edgeCount] <= (linkPrrThresVal+incPrrLinkStepVal))){
			    	rcolor = 255;
				    gcolor = 0;
				    bcolor = 0;
				    //System.out.println("In first range" + arrPRR[edgeCount]);
			    }
			    /* Second Range */
			    else if((arrPRR[edgeCount]>(linkPrrThresVal+incPrrLinkStepVal)) && (arrPRR[edgeCount] <= (linkPrrThresVal+2*incPrrLinkStepVal))){
			    	rcolor = 255;
				    gcolor = 66;
				    bcolor = 0;
				    //System.out.println("In second range"+ arrPRR[edgeCount]);
			    }
			    /* Third Range */
			    else if((arrPRR[edgeCount]>(linkPrrThresVal+2*incPrrLinkStepVal)) && (arrPRR[edgeCount] <= (linkPrrThresVal+3*incPrrLinkStepVal))){
			    	rcolor = 255;
				    gcolor = 255;
				    bcolor = 00;
				    //System.out.println("In third range"+ arrPRR[edgeCount]);
			    }
			    /* Fourth Range */
			    else{
			        rcolor = 0;
				    gcolor = 255;
				    bcolor = 0;
				    //System.out.println("In fourth range"+ arrPRR[edgeCount]);
			    }
				
				edgeColor = String.format("rgb(%d,%d,%d)", rcolor,gcolor,bcolor);
				
			    edgeObj.put("id", edgeId);
			    edgeObj.put("source", srcNode);
			    edgeObj.put("target", destNode);
			    edgeObj.put("color", edgeColor);
			    edgeObj.put("prr", arrPRR[edgeCount]);
			    edgeObj.put("rssi", arrRSSI[edgeCount]);
			    edgeObj.put("pe", arrPE[edgeCount]);
			    edgeElemList.add(edgeObj);
			    }
			    edgeCount = edgeCount + 1;
	    	}
		}
	    
	    timeObj.put("nodes", nodeElemList);
	    timeObj.put("edges", edgeElemList);
		
		outerObj.put("time"+simCount, timeObj);
		
    	}
    }
    
    public void writeJsonDataFile(){
    	try {
    		 
			FileWriter file = new FileWriter("C:\\Users\\anhegde\\mars_workspace\\nodeLinkData.json");
			//file.write(outerObj.toJSONString());
			file.write(outerObj.toString());
			file.flush();
			file.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void printJsonDataObj(){
    	System.out.print(outerObj);
    }
}
