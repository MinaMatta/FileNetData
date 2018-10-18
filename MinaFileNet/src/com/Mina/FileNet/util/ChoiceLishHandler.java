package com.Mina.FileNet.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.filenet.api.admin.Choice;
import com.filenet.api.admin.ChoiceList;
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.collection.EngineCollection;
import com.filenet.api.constants.ChoiceType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;

public class ChoiceLishHandler {
	public static void findClassesAndPropertiesForChoiceList(ObjectStore os,String ChoiceListName)
	{
		ArrayList<String> ClassesUsers=new ArrayList<String>();
		ArrayList<String> PropertiesUsers =new ArrayList<String>();
		SearchScope scope = new SearchScope(os);
		
		SearchSQL sql = new SearchSQL("SELECT * FROM ClassDefinition"); // or "SELECT * FROM ClassDefinition WHERE SuperclassDefinition = OBJECT('{...}')" to filter by superclass, for example
		
		EngineCollection coll = scope.fetchObjects(sql, null, null, false);
		
		Iterator<?> it = coll.iterator();
		
		while (it.hasNext()) {
			Boolean HasUsers=false;
			ClassDefinition CD = (ClassDefinition) it.next();
			Iterator iter = CD.get_PropertyDefinitions().iterator();
			PropertyDefinition PD = null;
			while (iter.hasNext())
			{
				PD = (PropertyDefinition) iter.next();
				ChoiceList temp=PD.get_ChoiceList();
				String CLName="";
				if(temp!=null)
					CLName=temp.get_Name();
				if(CLName.equalsIgnoreCase(ChoiceListName))
				{
					if(!PropertiesUsers.contains(PD.get_SymbolicName()))
						PropertiesUsers.add(PD.get_SymbolicName());
					HasUsers=true;
				}
			}
			if(HasUsers)
				ClassesUsers.add(CD.get_SymbolicName());
		}
	}
	
	public static void setChoiceListForPropertyDefinitions(ObjectStore os,String[] Classes,String[] PropertyDefinitions,ChoiceList CL)
	{
		System.out.println("\n\nSetChoiceListForPropertyDefinitions\n");
		PropertyFilter PF = new PropertyFilter(); 
		PF.addIncludeProperty(new FilterElement(null, null, null, PropertyNames.PROPERTY_DEFINITIONS, null));
		ArrayList<String> Properties=new ArrayList<String>(Arrays.asList(PropertyDefinitions));
		for (int i = 0; i < Classes.length; i++) 
		{
			System.out.println("\nClass Definition = "+Classes[i]);
			ClassDefinition CD = Factory.ClassDefinition.fetchInstance(os, Classes[i], PF);
			Iterator iter = CD.get_PropertyDefinitions().iterator();
			PropertyDefinition PD = null;
			while (iter.hasNext())
			{
				PD = (PropertyDefinition) iter.next();
				String PropertySymbolicName = PD.get_SymbolicName();
				if(Properties.contains(PropertySymbolicName))
				{
					System.out.println("Property definition selected : " + PropertySymbolicName);
					PD.set_ChoiceList(CL);
				}
			}
			CD.save(RefreshMode.REFRESH);
		}
	}
	
	public static void setChoiceListForPropertyTemplate(ObjectStore os,String[] PropertyTemplates,ChoiceList CL)
	{
		System.out.println("\n\nSetChoiceListForPropertyDefinitions\n");
		Iterator iter = os.get_PropertyTemplates().iterator();
		PropertyTemplate PT = null;
		ArrayList<String> Properties=new ArrayList<String>(Arrays.asList(PropertyTemplates));
		// Loop until property template found in object store
		while (iter.hasNext())
		{
			PT = (PropertyTemplate) iter.next();
			String PropertySymbolicName = PT.get_SymbolicName();
			if (Properties.contains(PropertySymbolicName))
			{
				System.out.println("Property Template selected : " + PropertySymbolicName);
				PT.set_ChoiceList(CL);
				PT.save(RefreshMode.REFRESH);
			}
		}
	}
	
	public static Choice createStringChoice(String DisplayName,String Value) 
	{
		Choice objChoiceMidStr1 = Factory.Choice.createInstance();
		objChoiceMidStr1.set_ChoiceType(ChoiceType.STRING);
		objChoiceMidStr1.set_DisplayName(DisplayName);
		objChoiceMidStr1.set_ChoiceStringValue(Value);
		return objChoiceMidStr1;
	}
	
	public static boolean checkItemExistInChoiceList(ObjectStore os, Id id,String choiceValue){
		try
		{
			com.filenet.api.admin.ChoiceList choiceList = Factory.ChoiceList.fetchInstance(os, id, null);
			com.filenet.api.collection.ChoiceList choices = choiceList.get_ChoiceValues();
			Choice choice;
			Iterator choiceIt = choices.iterator();
			while (choiceIt.hasNext())
			{
				choice = (Choice)choiceIt.next();
				if(choice.get_DisplayName().compareTo(choiceValue)==0)
				{
					return true;
				}
			}
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public static void addValueInGroup(ObjectStore os, Id id,String choiceValue,String choiceDisplayName,String groupValue){
		
		try{
			com.filenet.api.admin.ChoiceList choiceList = Factory.ChoiceList.fetchInstance(os, id, null);
			com.filenet.api.collection.ChoiceList choices = choiceList.get_ChoiceValues();
			Choice choice;
			Iterator choiceIt = choices.iterator();
			while (choiceIt.hasNext())
			{
				choice = (Choice)choiceIt.next();
				if(choice.get_DisplayName().compareTo(groupValue)==0)
				{
					choice.get_ChoiceValues().add(createStringChoice(choiceDisplayName, choiceValue));
					System.out.println("add it in development groups choice list under its development");
				}
			}
			choiceList.save(RefreshMode.REFRESH);
			}catch(Exception e){
		}
		
		
	}
	
	public static String getChoiceValue(ObjectStore os, Id id,String choiceDisplayName){
			try{
				com.filenet.api.admin.ChoiceList choiceList = Factory.ChoiceList.fetchInstance(os, id, null);
				com.filenet.api.collection.ChoiceList choices = choiceList.get_ChoiceValues();
				Choice choice;
				Iterator choiceIt = choices.iterator();
				while (choiceIt.hasNext()){
					choice = (Choice)choiceIt.next();
					if(choice.get_DisplayName().compareTo(choiceDisplayName)==0){
						return choice.get_ChoiceStringValue();
					}
				}
				return "";
				}catch(Exception e){
					e.printStackTrace();
					return "";
				}
	 }
	
	public static String getGroupName(ObjectStore os, Id id,String choiceValue){
			
			try{
				com.filenet.api.admin.ChoiceList choiceList = Factory.ChoiceList.fetchInstance(os, id, null);
				com.filenet.api.collection.ChoiceList choices = choiceList.get_ChoiceValues();
				Choice choice;
				Iterator choiceIt = choices.iterator();
				while (choiceIt.hasNext())
				{
					choice = (Choice)choiceIt.next();
					com.filenet.api.collection.ChoiceList choices2 = choice.get_ChoiceValues();
					Choice choice2;
					Iterator choiceIt2 = choices2.iterator();
					while (choiceIt2.hasNext())
						{
						choice2 = (Choice)choiceIt2.next();
						if(choice2.get_ChoiceStringValue().compareTo(choiceValue)==0){
						return choice.get_DisplayName();
						}
						}
					}
				return "";
				}catch(Exception e){
					
					return "";
			}
			
		}
}
