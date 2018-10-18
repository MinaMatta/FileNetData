package com.Mina.FileNet.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.AccessPermission;

public class CustomContractHandler {

	public static boolean CheckContractCopies(String MyID,ObjectStore os){
		Folder folder = Factory.Folder.fetchInstance(os, MyID, null);
		StringList FileNetCommandList=folder.getProperties().getStringListValue("FileNetCommand");
		ArrayList<String> CopyToList=new ArrayList<String>();
		String CopyOf="";
		for(int i=0;i<FileNetCommandList.size();i++){
			if(FileNetCommandList.get(i).toString().contains("Copy To:")){
				CopyToList.add(FileNetCommandList.get(i).toString().split(":")[1]);
			}else if(FileNetCommandList.get(i).toString().contains("Copy Of:")){
				CopyOf=FileNetCommandList.get(i).toString().split(":")[1];
			}
		}
		boolean hasCopies=false;
		if(CopyOf!=""||CopyToList.size()>0)
		{
			hasCopies=true;
		}
		return hasCopies;
	}
	
	public static CustomObject getCCObject(ObjectStore o,String ContractNo,String ContractFolderID)throws Exception{

		String select = " d.ContractNo, d.FoldersIDList,d.Id ";
		String from = "CustomContract";
		String where = "d.ContractNo = '"+ContractNo+"' AND '"+ContractFolderID+"' in d.FoldersIDList";
		IndependentObjectSet set =  FilenetSearch.searchForObjectsInObjectStore(o,select,from,where,null,0);
		com.filenet.api.core.CustomObject resDoc=null;
		if(set.isEmpty()){
		}else{
			for( Iterator i = set.iterator(); i.hasNext(); )
			{
				resDoc = (com.filenet.api.core.CustomObject)i.next();
				resDoc=Factory.CustomObject.fetchInstance(o, resDoc.get_Id(), null);
				break;
			}
		}
		return resDoc;
	}

	public static void UpdateContractFolderSecurity(ObjectStore os,Folder f)
	{
		String PAth=f.get_PathName();
		String[] foldersArray=PAth.substring(1).split("/");
		if(foldersArray.length==5)
		{
			String ProjectPath         = "/".concat(foldersArray[0]).concat("/").concat(foldersArray[1]);
			Folder ProjectFolder=Factory.Folder.fetchInstance(os, ProjectPath, null);
			AccessPermissionList ContractAPL=Factory.AccessPermission.createList();
			Iterator iter3=ProjectFolder.get_Permissions().iterator();
			int AdminAccessMask=999415;
			int ModifierAccessMask=135159;
			int ViewerAccessMask=131201;
			while (iter3.hasNext())
			{
				AccessPermission ap = Factory.AccessPermission.createInstance();
				AccessPermission type = (AccessPermission)iter3.next();
				if(type.get_GranteeType().toString().compareTo("USER")!=0&&type.get_GranteeName().compareTo("#AUTHENTICATED-USERS")!=0)
				{
					ap.set_GranteeName(type.get_GranteeName());
					ap.set_AccessType(type.get_AccessType());
					ap.set_InheritableDepth(type.get_InheritableDepth());
					if(type.get_GranteeName().contains("Admin"))
					{
						ap.set_AccessMask(AdminAccessMask);
					}
					else if(type.get_GranteeName().contains("Modifier"))
					{
						ap.set_AccessMask(ModifierAccessMask);
					}
					else if(type.get_GranteeName().contains("Viewer"))
					{
						ap.set_AccessMask(ViewerAccessMask);
					}
					ContractAPL.add(ap);
				}
			}
			f.set_Permissions(ContractAPL);
			f.save(RefreshMode.REFRESH);
		}
	}
	
}
