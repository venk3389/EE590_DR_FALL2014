package intfcMatlab;

import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;

public class accessControlMatlab {
	
	private static MatlabProxy proxy;
	private static MatlabProxyFactory factory;
	private static java.lang.String varName;
	private static java.lang.Object val;
	private static java.lang.Object result;
	private static java.lang.Object[] resArr;
	private static java.lang.String commandMat;
	private static java.lang.String func;
	private static int nargoutM;
	private static int isErr;
	private static boolean isExistSession;
	
	public accessControlMatlab(){
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
	
	public static void createMatInstance(){
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
	
	public static void setMatVariable(java.lang.String variableName, java.lang.Object value){
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

	public static java.lang.Object getMatVariable(java.lang.String variableName){
		varName = variableName; 
		try {
			result = proxy.getVariable(varName);
			return result;
		}
		catch(MatlabInvocationException e) {
			e.printStackTrace();
			//Invocation exception
			isErr = 2;
			return result;
		}
	}
	
	public static void evalMat(java.lang.String command){
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
	        
    public static java.lang.Object[] returningEvalMat(java.lang.String command,int nargout){
		commandMat = command;
		nargoutM = nargout; 
		try {
			resArr = proxy.returningEval(commandMat,nargoutM);
			return resArr;
			}
			catch(MatlabInvocationException e) {
				e.printStackTrace();
				//Invocation exception
				isErr = 2;
				return resArr;
			}
	}
	
	public static void fevalMat(java.lang.String functionName,java.lang.Object... args){
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
	
	public static java.lang.Object[] returningFevalMat(java.lang.String functionName,int nargout,java.lang.Object... args){
		func = functionName; 
		nargoutM = nargout; 
		try {
			resArr = proxy.returningFeval(func,nargoutM,args);
			return resArr;
			}
			catch(MatlabInvocationException e) {
				e.printStackTrace();
				//Invocation exception
				isErr = 2;
				return resArr;
			}
	}
	
	public static void exitMat(){
		try {
			 proxy.exit();
		}
		catch(MatlabInvocationException e) {
			e.printStackTrace();
			//Invocation exception
			isErr = 2;
		}
	}
	
	public static void disconnectMat(){
			 proxy.disconnect();
	}
	
	public static void isConnectedMat(){
		 proxy.isConnected();
	}
	
	public static boolean isExistingSessionMat(){
		 isExistSession = proxy.isExistingSession();
		 return isExistSession;
	}
	
	public static void sleepM(int time){
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
