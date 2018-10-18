package com.Mina.FileNet.util;

import com.filenet.api.action.Delete;
import com.filenet.api.action.PendingAction;
import com.filenet.api.action.Update;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.IndependentlyPersistableObject;

public class ChangePreprocessorHandler {

	public void compareCurrentValue(IndependentlyPersistableObject sourceObj,String propertySN,String OldValue)
	{
		if (sourceObj.getProperties().isPropertyPresent(propertySN))
		  {
			  String NewValue=sourceObj.getProperties().getStringValue(propertySN); 
			  if(!NewValue.equals(OldValue))
			  {
				  throw new RuntimeException("Please, Don't Modify "+propertySN+" From This Document.");
			  }
		  }
	}
	
	/*
	public boolean preprocessObjectChange(IndependentlyPersistableObject sourceObj)
	{
	   try 
	   		{
	    	  boolean retValue=false;
	    	  
	    	  GetLogFilePath();
	    	  Domain dom = Factory.Domain.fetchInstance(sourceObj.getConnection(), null, null);
	    	  os = Factory.ObjectStore.fetchInstance(dom,"UMC", null);
	    	  PendingAction actions[] = sourceObj.getPendingActions();	    	 
	    	  doc=(Document) sourceObj;
	    	  for ( int i = 0; i < actions.length ; i++ )
	    	  {
	    		 if ( actions[i] instanceof Delete )
	    		  {
	    		  retValue=true;
	    		  }
	    		  else if ( actions[i] instanceof Update )
	    		  {
	    		  retValue=true;
	    		  }
	    		  else
	    		  {
	    			  retValue=true;
	    		  }
	    	  }
	    	  return retValue;
	    	}
	    	
	   catch (Exception e) 
	   {
	   		throw new RuntimeException(e);//to get the error on the user interface
	   }	
	}
	*/
}
