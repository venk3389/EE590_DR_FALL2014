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
    private double [] arrXY;
    private double [] arrPRR;
    private double [] arrRSSI;
    private double [] arrPE;
    
    JSONObject outerObj = new JSONObject();
	JSONArray nodeElemList = new JSONArray();
	JSONArray edgeElemList = new JSONArray();
    
    public CreateNodeLinkStatsJson(int nNodes, int nEdges, double [] xyArr,double [] prrArr,double [] rssiArr,double [] peArr ){
    	numNodes = nNodes;
    	numEdges = nEdges;
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

		for(int i = 0; i < numNodes; i++) {
			
			rcolor = ((i+1)*2)%255;
			gcolor = ((i+1)*4)%255;
			bcolor = ((i+1)*6)%255;
			
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
			
			    JSONObject edgeObj = new JSONObject();
			    edgeId = String.format("e%d", edgeCount);
			    srcNode = String.format("n%d", i);
			    destNode = String.format("n%d", j);
			    edgeCount = edgeCount + 1;
			    
			    rcolor = 255;
				gcolor = ((i+1)*4)%255;
				bcolor = ((i+1)*6)%255;
				
				edgeColor = String.format("rgb(%d,%d,%d)", rcolor,gcolor,bcolor);
				
			    edgeObj.put("id", edgeId);
			    edgeObj.put("source", srcNode);
			    edgeObj.put("target", destNode);
			    edgeObj.put("color", edgeColor);
			    
			    edgeElemList.add(edgeObj);
	    	}
		}
	    
		outerObj.put("nodes", nodeElemList);
		outerObj.put("edges", edgeElemList);
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
