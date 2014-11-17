package intfcMatlab;
import org.json.simple.JSONObject;

public class TestMatHelper {
	
	static JSONObject simDataJsonObj = new JSONObject();
	public static void main(String[] args) {
		double PRR_THRES = 0.1;
		double GRID_UNIT = 3.0; // This param is used only for Grid topology
		double PATH_LOSS_EXPONENT = 4.7;
		double SHADOWING_STANDARD_DEVIATION = 8.0;
		double TEMPORAL_CORRELATION_COEFFICIENT = 0.5;
		double SPATIAL_CORRELATION_COEFFICIENT = 0.5;
		double TERRAIN_DIMENSIONS_X = 100.0; // Only required for Uniform and Random topology type
		double TERRAIN_DIMENSIONS_Y = 100.0; // Only required for Uniform and Random topology type
		double [][] xyArrTest = {{0.0,0.0},{1.0,0.0},{2.0,0.0},{3.0,0.0}};

		int NUMBER_OF_NODES = 36; // for grid topology only, no: of nodes should be perfect square
		int TOPOLOGY = 3; 
		int SIM_CNT = 2;
		int MODULATION = 3;
		int ENCODING = 3;
		int PREAMBLE_LENGTH = 2;
		int FRAME_LENGTH = 50;
		 	
		MatlabHelper matObj = new MatlabHelper(PRR_THRES,GRID_UNIT,PATH_LOSS_EXPONENT,SHADOWING_STANDARD_DEVIATION,TEMPORAL_CORRELATION_COEFFICIENT,SPATIAL_CORRELATION_COEFFICIENT,TERRAIN_DIMENSIONS_X,TERRAIN_DIMENSIONS_Y,xyArrTest,NUMBER_OF_NODES,TOPOLOGY,SIM_CNT,MODULATION,ENCODING,PREAMBLE_LENGTH,FRAME_LENGTH);
		simDataJsonObj = matObj.runMatLinkLayerSim();
	}

}
