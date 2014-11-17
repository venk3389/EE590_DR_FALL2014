package intfcMatlab;

import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabTypeConverter;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
/*
 * 
 * 
 * PLEASE - NAME OF A CLASS STARTS WITH A CAP LETTER ! Done
 * 
 * */

public class AccessControlMatlab {
	
	private static MatlabProxy proxy;
	private static MatlabProxyFactory factory;
	private static MatlabTypeConverter processor;
	//dont need to make these variables static : con with static is their memory profile remains even after the code exists.
	// we need the matlab instance to exists until the server restarts.
	// also before you create a proxy, check if there is an active proxy object in memory.
	// if yes use that, else create a new one
	
	private  java.lang.String varName;
	private  java.lang.Object val;
	private  java.lang.Object result;
	private  java.lang.Object[] resArr;
	private  java.lang.String commandMat;
	private  java.lang.String func;
	private  java.lang.String matArrayName = "xyArr";
	private  MatlabNumericArray matArray;
	//private  double [][] javaArr;
	private  int nargoutM;
	private  int isErr;
	private  boolean isExistSession;
	
	public AccessControlMatlab(){
		/*
		factory = new MatlabProxyFactory();
		try {
			proxy = factory.getProxy();
			isErr = 0;
		}
		catch (MatlabConnectionException e) {
			e.printStackTrace();
			//Connection exception
			isErr = 1;
		}
		*/
	}
	
	public void createMatInstance(){
		factory = new MatlabProxyFactory();
		try {
			proxy = factory.getProxy();
			isErr = 0;
		}
		catch (MatlabConnectionException e) {
			e.printStackTrace();
			//Connection exception
			isErr = 1;
		}
	}
	
	public void createMatTypeConverter(){
		processor = new MatlabTypeConverter(proxy);
	}
	
	
	public void matSetNumericArray(java.lang.String arrayName, double[][] array){
		matArrayName = arrayName;
		MatlabNumericArray matArray = new MatlabNumericArray(array, null);
		try{
			//processor.setNumericArray(matArrayName,matArray);
			processor.setNumericArray(matArrayName, new MatlabNumericArray(array, null));
		}
		catch(MatlabInvocationException e){
			e.printStackTrace();
			//Invocation exception
			isErr = 2;
		}
	}
	
	public MatlabNumericArray matGetNumericArray(java.lang.String arrayName){
		matArrayName = arrayName;
		try{
			matArray = processor.getNumericArray(matArrayName);
			//javaArr = (double[][])matArray;
		}
		catch(MatlabInvocationException e){
			e.printStackTrace();
			//Invocation exception
			isErr = 2;
		}
		return matArray;
		
	}
	

	public void setMatVariable(java.lang.String variableName, java.lang.Object value){
		varName = variableName; 
		val = value;
		try {
			 proxy.setVariable(varName, val);
		}
		catch(MatlabInvocationException e) {
			e.printStackTrace();
			//Invocation exception
			isErr = 2;
		}
	}

	public java.lang.Object getMatVariable(java.lang.String variableName){
		varName = variableName; 
		try {
			result = proxy.getVariable(varName);	
		}
		catch(MatlabInvocationException e) {
			e.printStackTrace();
			//Invocation exception
			isErr = 2;
		}
		return result;
	}
	
	public void evalMat(java.lang.String command){
		commandMat = command;
		try {
			proxy.eval(commandMat);
		}
		catch(MatlabInvocationException e) {
			e.printStackTrace();
			//Invocation exception
			isErr = 2;
		}
	}
	        
    public java.lang.Object[] returningEvalMat(java.lang.String command,int nargout){
    	// directly use the function parameters, unless these could be used across other methods
    	// during execution
    	//never return from exception, use finally to do so.
		commandMat = command;
		nargoutM = nargout; 
		try {
			resArr = proxy.returningEval(commandMat,nargoutM);
			
			}
			catch(MatlabInvocationException e) {
				e.printStackTrace();
				//Invocation exception
				isErr = 2;
			}
		    return resArr;
	}
	
	public void fevalMat(java.lang.String functionName,java.lang.Object... args){
		func = functionName; 
		try {
		proxy.feval(func,args);
		}
		catch(MatlabInvocationException e) {
			e.printStackTrace();
			//Invocation exception
			isErr = 2;
		}
	}
	
	public java.lang.Object[] returningFevalMat(java.lang.String functionName,int nargout,java.lang.Object... args){
		func = functionName; 
		nargoutM = nargout; 
		try {
			resArr = proxy.returningFeval(func,nargoutM,args);
		
			}
			catch(MatlabInvocationException e) {
				e.printStackTrace();
				//Invocation exception
				isErr = 2;
				
			}
		    return resArr;
	}
	
	public void exitMat(){
		try {
			 proxy.exit();
		}
		catch(MatlabInvocationException e) {
			e.printStackTrace();
			//Invocation exception
			isErr = 2;
		}
	}
	
	public void disconnectMat(){
			 proxy.disconnect();
	}
	
	public void isConnectedMat(){
		 proxy.isConnected();
	}
	
	public boolean isExistingSessionMat(){
		 isExistSession = proxy.isExistingSession();
		 return isExistSession;
	}
	
	public  void sleepM(int time){
		try{
			Thread.sleep(time);
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isErr = 3;
		}
	}
	public int getErr(){
		return isErr;
	}
}
