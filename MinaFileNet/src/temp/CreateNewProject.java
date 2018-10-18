package temp;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;

import com.filenet.api.admin.Choice;
import com.filenet.api.admin.ChoiceList;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.MarkingList;
import com.filenet.api.collection.MarkingSetSet;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.ChoiceType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.DynamicReferentialContainmentRelationship;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.Marking;
import com.filenet.api.security.MarkingSet;
import com.filenet.api.util.Id;

public class CreateNewProject {
//	public static String developmentGoupsChoiceListId = "{A5BD8039-7DAF-4160-9CC4-00ABF2506459}"; //On SODIC Site
//	public static String developmentClId="{4DCFACA3-AD90-4CC1-8AC6-485E86C2C8C5}";            	//On SODIC Site
//	public static String projectMarkingSetId = "{86C2ACF3-2695-4C17-89F0-F10C5DF85475}";      	//On SODIC Site
//	public static String P8adminStr = "P8Admins@SODIC.LOC";      	//On SODIC Site
	public static String developmentGoupsChoiceListId = "{A5BD8039-7DAF-4160-9CC4-00ABF2506459}"; //On VMware
	public static String developmentClId="{4DCFACA3-AD90-4CC1-8AC6-485E86C2C8C5}";            	//On VMware
	public static String projectMarkingSetId = "{2B6E95FC-19DF-4239-8D98-83E40683139B}";      	//On VMware
	public static String P8adminStr = "P8Admins@Test.com";      	//On VMware
	public static void createNewProject(ObjectStore os,String Development,String DevelopmentViewers,String Project,String Admins,String Modifiers,String Viewers,String OutgoingCode) throws Exception
	{
		
		
		Domain dom=os.get_Domain();
//		Check if not that development created before create it 
		if(!CheckDevelopmentExist(os,new Id(developmentClId), Development))
		{
//			add it in development Choice list
			ChoiceList DevelopmentCL = Factory.ChoiceList.fetchInstance(os, new Id(developmentClId), null);
			DevelopmentCL.get_ChoiceValues().add(CreateGroupChoice(Development));
			DevelopmentCL.save(RefreshMode.REFRESH);
//			add it in development Groups choice list
			ChoiceList DevelopmentGroupsCL = Factory.ChoiceList.fetchInstance(os, new Id(developmentGoupsChoiceListId), null);
			DevelopmentGroupsCL.get_ChoiceValues().add(CreateGroupChoice(Development));
			DevelopmentGroupsCL.save(RefreshMode.REFRESH);
		}

//		Check if there folder under FNOBS1 by development name
		if(isFolderExist(os, "/".concat(Development.substring(Development.indexOf("_")+1))))
		{	
//			Add DevelopmentViewers to Security of folder
			Folder folder=Factory.Folder.fetchInstance(os, "/".concat(Development.substring(Development.indexOf("_")+1)), null);
			folder.set_Permissions(getPermissionsforDevelopmentFolder(DevelopmentViewers));
			folder.save(RefreshMode.REFRESH);
		}
//		else create it
		else
		{
			addFolder(os, Development.substring(Development.indexOf("_")+1));
//			Add DevelopmentViewers to Security of folder
			Folder folder=Factory.Folder.fetchInstance(os, "/".concat(Development.substring(Development.indexOf("_")+1)), null);
			folder.set_Permissions(getPermissionsforDevelopmentFolder(DevelopmentViewers));
			folder.save(RefreshMode.REFRESH);
		}
//		Check if project not created before create it
		if(!CheckMarkingSetExist(dom, new Id(projectMarkingSetId), Project))
		{
//			add it in marking set project
//			put the 3 Groups of security on that marking
			addMarking(dom, Project, Admins, Modifiers, Viewers);
//			add it in development groups choice list under its development
			addProjectInDevelopmentGroup(os, new Id(developmentGoupsChoiceListId), Project, Project.substring(Project.indexOf("_")+1), Development);
		}

//		Check if there is an OutgoingLetter Object by that name before
//			add that project in project list
//		else create it
//			add that project in project list
		addOutgoingLetterCode(os, OutgoingCode, Project);
//		Check if there is a folder by project name under development
		if(isFolderExist(os, "/".concat(Development.substring(Development.indexOf("_")+1)).concat("/").concat(Project.substring(Project.indexOf("_")+1))))
		{	
//			Apply The 3 Layers of security on that folder
			Folder folder=Factory.Folder.fetchInstance(os, "/".concat(Development.substring(Development.indexOf("_")+1)).concat("/").concat(Project.substring(Project.indexOf("_")+1)), null);
			folder.set_Permissions(getPermissionsforProjectFolder(Admins, Modifiers, Viewers));
			folder.save(RefreshMode.REFRESH);
		}
//		else create it
		else
		{
			addSubFolder(os,  "/".concat(Development.substring(Development.indexOf("_")+1)), Project.substring(Project.indexOf("_")+1));
//			Apply The 3 Layers of security on that folder
			Folder folder=Factory.Folder.fetchInstance(os, "/".concat(Development.substring(Development.indexOf("_")+1)).concat("/").concat(Project.substring(Project.indexOf("_")+1)), null);
			folder.set_Permissions(getPermissionsforProjectFolder(Admins, Modifiers, Viewers));
			folder.save(RefreshMode.REFRESH);
		}
	}
	
	public static boolean CheckDevelopmentExist(ObjectStore os, Id id,String choiceValue){
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
	
	public static Choice CreateStringChoice(String DisplayName,String Value) 
	{
		Choice objChoiceMidStr1 = Factory.Choice.createInstance();
		objChoiceMidStr1.set_ChoiceType(ChoiceType.STRING);
		objChoiceMidStr1.set_DisplayName(DisplayName);
		objChoiceMidStr1.set_ChoiceStringValue(Value);
		System.out.println("Display Name = "+DisplayName+"\t Value = "+Value+" \n");
		return objChoiceMidStr1;
	}
	
	public static Choice CreateGroupChoice(String DisplayName) 
	{
		Choice objChoiceMidStr1 = Factory.Choice.createInstance();
		objChoiceMidStr1.set_ChoiceType(ChoiceType.MIDNODE_STRING);
		objChoiceMidStr1.set_DisplayName(DisplayName);
		objChoiceMidStr1.set_ChoiceValues(Factory.Choice.createList());
		System.out.println("Display Name = "+DisplayName);
		return objChoiceMidStr1;
	}

	public static boolean isFolderExist (ObjectStore os, String folderPath){
		try{
			Folder f1 = Factory.Folder.fetchInstance(os, folderPath, null);
			return true;
		}catch(Exception e){
			try{	
				}catch(Exception t){
					
				}
			return false;
		}
	}
	
	public static boolean addFolder(ObjectStore os, String folderName) {
		
		try{
	      Folder folder = Factory.Folder.createInstance(os, null);
	      Folder rootFolder = os.get_RootFolder();
	      folder.set_FolderName(folderName);
	      folder.set_Parent(rootFolder);
	      folder.save(null);
	      return true;
		}catch(Exception e){
			try{
				}catch(Exception t){
					System.out.println(t.getMessage());
				}
			return false;
		}
	   }

	public static boolean addSubFolder (ObjectStore os, String parentPath, String subFolderName){
		
		try{
			Folder parentFolder = Factory.Folder.fetchInstance(os, parentPath, null);
			Folder subFolder = parentFolder.createSubFolder(subFolderName);
			subFolder.save(RefreshMode.REFRESH);
			return true;
		}catch(Exception e){
			
			
			return false;
		}
	}
	
	public static boolean CheckMarkingSetExist(Domain dom, Id id,String markingValue){
		try{
		com.filenet.api.security.MarkingSet markingSet = Factory.MarkingSet.fetchInstance(dom, id,null);
		MarkingList markingList = markingSet.get_Markings();
		Marking marking;
		Iterator markingIt = markingList.iterator();
		while (markingIt.hasNext()){
			marking = (Marking)markingIt.next();
			if(marking.get_MarkingValue().compareTo(markingValue)==0){
				return true;
			}
		}
		return false;
		}catch(Exception e){
			
				
				return false;
		}
	}

	public static void addMarking(Domain dom,String Project,String Admins,String Modifiers,String Viewers)
	{
		int APAdminAccessMask=234881024;
		int APModifierAccessMask=201326592;
		int APViewerAccessMask=134217728;
		MarkingSet markingSet = Factory.MarkingSet.fetchInstance(dom, new Id(projectMarkingSetId),null);
		Marking m=Factory.Marking.createInstance();
		m.set_MarkingValue(Project);
		m.set_ConstraintMask(-1);
		AccessPermissionList APL=Factory.AccessPermission.createList();
		AccessPermission P8Admins = Factory.AccessPermission.createInstance();
		AccessPermission AdminsAP = Factory.AccessPermission.createInstance();
		AccessPermission ModifiersAP = Factory.AccessPermission.createInstance();
		AccessPermission ViewersAP = Factory.AccessPermission.createInstance();
		
		P8Admins.set_AccessMask(APAdminAccessMask);
		AdminsAP.set_AccessMask(APAdminAccessMask);
		ModifiersAP.set_AccessMask(APModifierAccessMask);
		ViewersAP.set_AccessMask(APViewerAccessMask);
		
		P8Admins.set_AccessType(AccessType.ALLOW);
		AdminsAP.set_AccessType(AccessType.ALLOW);
		ModifiersAP.set_AccessType(AccessType.ALLOW);
		ViewersAP.set_AccessType(AccessType.ALLOW);
		
		P8Admins.set_InheritableDepth(0);
		AdminsAP.set_InheritableDepth(0);
		ModifiersAP.set_InheritableDepth(0);
		ViewersAP.set_InheritableDepth(0);
		
		P8Admins.set_GranteeName(P8adminStr);
		AdminsAP.set_GranteeName(Admins);
		ModifiersAP.set_GranteeName(Modifiers);
		ViewersAP.set_GranteeName(Viewers);
		
		APL.add(P8Admins);
		APL.add(AdminsAP);
		APL.add(ModifiersAP);
		APL.add(ViewersAP);
		m.set_Permissions(APL);
		markingSet.get_Markings().add(m);
		markingSet.save(RefreshMode.REFRESH);
	}

	public static AccessPermissionList getPermissionsforProjectFolder(String Admins,String Modifiers,String Viewers)
	{
		int AdminAccessMask=999415;
		int ModifierAccessMask=135159;
		int ViewerAccessMask=131201;
		AccessPermissionList APL=Factory.AccessPermission.createList();
		AccessPermission P8Admins = Factory.AccessPermission.createInstance();
		AccessPermission AdminsAP = Factory.AccessPermission.createInstance();
		AccessPermission ModifiersAP = Factory.AccessPermission.createInstance();
		AccessPermission ViewersAP = Factory.AccessPermission.createInstance();
		
		P8Admins.set_AccessMask(AdminAccessMask);
		AdminsAP.set_AccessMask(AdminAccessMask);
		ModifiersAP.set_AccessMask(ModifierAccessMask);
		ViewersAP.set_AccessMask(ViewerAccessMask);
		
		P8Admins.set_AccessType(AccessType.ALLOW);
		AdminsAP.set_AccessType(AccessType.ALLOW);
		ModifiersAP.set_AccessType(AccessType.ALLOW);
		ViewersAP.set_AccessType(AccessType.ALLOW);
		
		P8Admins.set_InheritableDepth(-1);
		AdminsAP.set_InheritableDepth(-1);
		ModifiersAP.set_InheritableDepth(-1);
		ViewersAP.set_InheritableDepth(-1);
		
		P8Admins.set_GranteeName(P8adminStr);
		AdminsAP.set_GranteeName(Admins);
		ModifiersAP.set_GranteeName(Modifiers);
		ViewersAP.set_GranteeName(Viewers);
		
		APL.add(P8Admins);
		APL.add(AdminsAP);
		APL.add(ModifiersAP);
		APL.add(ViewersAP);
		return APL;
	}
	
	public static AccessPermissionList getPermissionsforDevelopmentFolder(String Viewers)
	{
		int AdminAccessMask=999415;
		int ViewerAccessMask=131201;
		AccessPermissionList APL=Factory.AccessPermission.createList();
		AccessPermission P8Admins = Factory.AccessPermission.createInstance();
		AccessPermission ViewersAP = Factory.AccessPermission.createInstance();
		
		P8Admins.set_AccessMask(AdminAccessMask);
		ViewersAP.set_AccessMask(ViewerAccessMask);
		
		P8Admins.set_AccessType(AccessType.ALLOW);
		ViewersAP.set_AccessType(AccessType.ALLOW);
		
		P8Admins.set_InheritableDepth(0);
		ViewersAP.set_InheritableDepth(0);
		
		P8Admins.set_GranteeName(P8adminStr);
		ViewersAP.set_GranteeName(Viewers);
		
		APL.add(P8Admins);
		APL.add(ViewersAP);
		return APL;
	}

	public static void addProjectInDevelopmentGroup(ObjectStore os, Id id,String choiceValue,String choiceDisplayName,String groupValue){
		
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
					choice.get_ChoiceValues().add(CreateStringChoice(choiceDisplayName, choiceValue));
					System.out.println("add it in development groups choice list under its development");
				}
			}
			choiceList.save(RefreshMode.REFRESH);
			}catch(Exception e){
		}
		
		
	}

	public static void addOutgoingLetterCode(ObjectStore o,String OutgoingLetterCode,String Project)throws Exception{

		String select = "d.OutgoingLetterCode,d.Project";
		String from = "SODICProject";
		String where = "d.OutgoingLetterCode = '"+OutgoingLetterCode+"'";
		
		IndependentObjectSet set = searchForDocuments(o,select,from,where,null,0);
		
		if(set.isEmpty()){
			CustomObject resDoc=Factory.CustomObject.createInstance(o, "SODICProject");
			resDoc.getProperties().putObjectValue("OutgoingLetterCode", OutgoingLetterCode);
			resDoc.getProperties().putValue("Project", Factory.StringList.createList());
			resDoc.getProperties().getStringListValue("Project").add(Project);
			resDoc.save(RefreshMode.REFRESH);
			fillInFolder(o, resDoc.get_Id(), "/Administration");
		}else{
			Iterator it=set.iterator();
			while (it.hasNext()) {
				CustomObject resDoc = (CustomObject)it.next();
				System.out.println(resDoc.getProperties().getStringValue("OutgoingLetterCode"));
//				resDoc=Factory.CustomObject.fetchInstance(o, resDoc.get_Id(), null);
				resDoc.getProperties().getStringListValue("Project").add(Project);
				resDoc.save(RefreshMode.REFRESH);
			}
		}

	}

	public static IndependentObjectSet searchForDocuments (ObjectStore o, String select,String from,String where,String orderBy ,int maxRecords){
		try{
			SearchSQL sqlObject = new SearchSQL();
		    sqlObject.setSelectList(select);
		    sqlObject.setFromClauseInitialValue(from, "d", true);
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
		    
		    //writeToFile(logFile,true,"SQL: " + sqlObject.toString());
		    return set;
		    
		    //return myObjects;
		}catch(Exception e){
			try{	
				
				}catch(Exception t){
					
				}
			return null;
		}
		
	    
	}

	public static boolean fillInFolder(ObjectStore os, Id docId, String folderPath){
		CustomObject myDoc = Factory.CustomObject.fetchInstance(os, docId, null);
		
		Folder parentFolder = Factory.Folder.fetchInstance(os, folderPath, null);
	    try {
	        ReferentialContainmentRelationship drcr = (ReferentialContainmentRelationship) parentFolder.file(myDoc, AutoUniqueName.AUTO_UNIQUE, myDoc.get_Name(),DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
	        drcr.save(RefreshMode.REFRESH);
	        return true;
	     }catch(Exception e){
	     	
	    	 return false;
	     }
	}
	
}
