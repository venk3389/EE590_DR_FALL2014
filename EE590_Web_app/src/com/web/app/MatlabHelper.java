package com.web.app;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;

public class MatlabHelper {

	static MatlabProxy proxy;
	static MatlabProxyFactory factory;

	public MatlabHelper() {
		factory=new MatlabProxyFactory();
	}
	public static void print(){

		double result = 0;
		try {
			proxy = factory.getProxy();
			for (int i = 0; i < 5; i++) {
				proxy.setVariable("a", 5);
				proxy.eval("a=a+6");
				//proxy.eval("a");
				result = ((double[]) proxy.getVariable("a"))[0];
				System.out.println("Result: " + result);
				//proxy.eval("clc");
				Thread.sleep(5000);
			}
			proxy.exit();
			if(proxy!=null)
				proxy.disconnect();
		} catch (MatlabInvocationException e) {
			e.printStackTrace();
			result=1;
		}
		catch (MatlabConnectionException e) {
			e.printStackTrace();
			result=1;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MatlabHelper mat=new MatlabHelper();
		print();
	}

}
