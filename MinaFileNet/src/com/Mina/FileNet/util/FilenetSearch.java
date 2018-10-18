package com.Mina.FileNet.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;

public class FilenetSearch {

 	public  static IndependentObjectSet searchForObjectsInObjectStore(ObjectStore o, String select,String from,String where,String orderBy ,int maxRecords)throws Exception{
		try{
			SearchSQL sqlObject = new SearchSQL();
		    sqlObject.setSelectList(select);
		    sqlObject.setFromClauseInitialValue(from, "d", false);
		    sqlObject.setWhereClause(where);
		    if(orderBy != null) sqlObject.setOrderByClause(orderBy);
		    if(maxRecords != 0) sqlObject.setMaxRecords(maxRecords);
		   
		    SearchScope search = new SearchScope(o);

		    // Set the page size (Long) to use for a page of query result data. This value is passed 
		    // in the pageSize parameter. If null, this defaults to the value of 
		    // ServerCacheConfiguration.QueryPageDefaultSize.
		    Integer myPageSize = new Integer(100);

		    // Specify a property filter to use for the filter parameter, if needed. 
		    // This can be null if you are not filtering properties.
		    PropertyFilter myFilter = new PropertyFilter();
		    int myFilterLevel = 1;
		    myFilter.setMaxRecursion(myFilterLevel);
		    myFilter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null)); 
		        
		    // Set the (Boolean) value for the continuable parameter. This indicates 
		    // whether to iterate requests for subsequent pages of result data when the end of the 
		    // first page of results is reached. If null or false, only a single page of results is 
		    // returned.
		    Boolean continuable = new Boolean(true);

		    // Execute the fetchObjects method using the specified parameters.
		    IndependentObjectSet set = search.fetchObjects(sqlObject, myPageSize, myFilter, continuable);
//		    System.out.println("SQL: " + sqlObject.toString());
		    //Uploader4.writeToFile(Uploader4.logFile,true,"SQL: " + sqlObject.toString());
		    return set;
		    
		    //return myObjects;
		}catch(Exception e){
			try{
				e.printStackTrace();
//				UploaderScreen.writeToFile("------------Uploader.searchForDocuments------------");
//				UploaderScreen.writeToFile(e.getMessage());
//				if(e.getMessage()==null)
//					UploaderScreen.writeToFile("null");
				}catch(Exception t){
					t.printStackTrace();
//					UploaderScreen.writeToFile(t.getMessage());
//					if(t.getMessage()==null)
//						UploaderScreen.writeToFile("null");
				
				}
			return null;
		}
		
	    
	}
 
 	public IndependentObjectSet GetAllDocumentsUnderPath(ObjectStore os,String FolderPath) throws Exception
	{
		String select = "d.Id";
		String from = "Document";
		String where = "d.This INSUBFOLDER '"+FolderPath+"'";
		IndependentObjectSet set = FilenetSearch.searchForObjectsInObjectStore(os, select, from, where, null, 0);
		return set;
	}
 	
 	public static IndependentObjectSet GetAllDocumentsByDateCreated(ObjectStore os,String DocumentType,Date fromDate,Date toDate,String[] NotNullProperties) throws Exception
	{
		String select = "d.Id";
		SimpleDateFormat SQLformatter = new SimpleDateFormat("yyyyMMdd");
		String fromDateStr = SQLformatter.format(fromDate).concat("T000000Z");
		String toDateStr = SQLformatter.format(toDate).concat("T000000Z");
		String where = "d.DateCreated > "+fromDateStr+" AND d.DateCreated < "+toDateStr+" AND (";
		for (int i = 0; i < NotNullProperties.length; i++) {
			if(i==NotNullProperties.length-1)
				where+=" d."+NotNullProperties[i]+" is not null )";
			else
				where+=" d."+NotNullProperties[i]+" is not null OR";
		}
		System.out.println(where);
		IndependentObjectSet set = FilenetSearch.searchForObjectsInObjectStore(os, select, DocumentType, where, null, 0);
		return set;
	}
	
}
