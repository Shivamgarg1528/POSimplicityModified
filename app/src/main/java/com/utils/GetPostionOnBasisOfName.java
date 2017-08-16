package com.utils;

public class GetPostionOnBasisOfName {

	public static int getPositionOfUserBasedOnName(String name){
		if(name.equalsIgnoreCase("Admin"))
			return 0;
		else if(name.equalsIgnoreCase("Clerk"))
			return 1;
		else if(name.equalsIgnoreCase("Manager"))
			return 2;
		else if(name.equalsIgnoreCase("Superviosr"))
			return 3;
		else 
			return 0;
	}
}
