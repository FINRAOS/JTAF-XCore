package org.finra.jtaf.core.ztest;

import java.io.File;

import org.finra.jtaf.core.AutomationEngine;
import org.junit.Assert;
import org.junit.Test;

public class DuplicateRepeatTest {
	private static AutomationEngine engine = null;
	   
	
	    @Test
	    public void testBuildmodel() {
	        engine = AutomationEngine.getInstance();

	        	try {
	        		 engine.buildModel(new File("mocktestlibrary/dupe.commands.xml"), new File("mocktestscripts"));
	        		 Assert.assertEquals("Oops! We have more then one command with same name ('repeat') (case insensitive)! Fix your test commands, please.", engine.mc.getCurrentSection().getName());
	        		 
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}   
	 

	    	
	    }
	
}
