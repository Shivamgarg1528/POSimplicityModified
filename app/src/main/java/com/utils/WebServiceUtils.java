package com.utils;

public class WebServiceUtils {

	public static synchronized boolean isAnyAsyncTaskLeft(int sizeOfArray,WebServiceCall[] webServiceObjArray){

		int flagInt            = 0;
		for(int index = 0; index <  sizeOfArray; index ++){
			WebServiceCall webServiceCall = webServiceObjArray[index];
			if(webServiceCall != null && webServiceCall.isTaskCompleted()){
				continue;
			}
			else{
				flagInt = 1;
				break;
			}
		}
		return (flagInt == 0)? false : true;
	}
	
	public static synchronized boolean isAnyAsyncTaskExceptionOccurOneTime(int sizeOfArray,WebServiceCall[] webServiceObjArray){

		int flagInt            = 0;
		for(int index = 0; index <  sizeOfArray; index ++){
			WebServiceCall webServiceCall = webServiceObjArray[index];
			if(webServiceCall != null && webServiceCall.isExceptionOcuur()){
				flagInt ++;
			}
		}
		return (flagInt == 1)? true : false;
	}
	
	public static synchronized boolean isAnyAsyncTaskNoIntenetOccurOneTime(int sizeOfArray,WebServiceCall[] webServiceObjArray){

		int flagInt            = 0;
		for(int index = 0; index <  sizeOfArray; index ++){
			WebServiceCall webServiceCall = webServiceObjArray[index];
			if(webServiceCall != null && webServiceCall.isNoInterent()){
				System.out.println("FlagValue:+++++= "+flagInt);
				
				flagInt ++;
			}
		}
		System.out.println("FlagValue:= "+flagInt);
		return (flagInt == 1)? true : false;
	}
}