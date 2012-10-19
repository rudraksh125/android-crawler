package com.nofatclips.crawler.helpers;

public class HashGenerator {

	public static String generateFromString(String s)
	{
		if (s != null)
		{
			return Integer.toString(s.hashCode());
		}
		else
		{
			return "null";
		}
	}
	
}
