package com.Mina.FileNet.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
public class PropertiesHandler 
{
	public static ArrayList GetPropertiesTypes(ObjectStore os,String FilenetClass, ArrayList FNattr)
	{
		ArrayList types=new ArrayList();
//		String PropertyType=GetPropertyType(os, FNattr.get(i).toString(), FilenetClass);
		ClassDescription objClassDesc = Factory.ClassDescription.fetchInstance(os, FilenetClass, null);
		PropertyDescriptionList objPropDescs = objClassDesc.get_PropertyDescriptions();   
		Iterator iter = objPropDescs.iterator();
		PropertyDescription objPropDesc = null;
		ArrayList<PropertyDescription> properties=new ArrayList<PropertyDescription>();
		while (iter.hasNext())
		{                                               
		   objPropDesc = (PropertyDescription) iter.next();                      
		   if (FNattr.contains(objPropDesc.get_SymbolicName()))
		   {
			  properties.add(objPropDesc);
		   }
		}
		for (int i = 0; i < FNattr.size(); i++) 
		{
			for (int j = 0; j < properties.size(); j++) 
			{	
				if (FNattr.get(i).toString().compareTo(properties.get(j).get_SymbolicName().toString())==0) 
				{
					types.add(properties.get(j).get_DataType());
					break;
				}
			}
		}
		return types;
	}
}
