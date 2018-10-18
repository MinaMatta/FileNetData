package com.Mina.FileNet.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.security.AccessPermission;

public class SecurityHandler {

	public void RemoveUsersFromPermissions(Folder f)
	{
		Iterator iter=f.get_Permissions().iterator();
		Boolean hasUsers=false;
		while (iter.hasNext()){
			AccessPermission type = (AccessPermission)iter.next();
			if(type.get_GranteeType().toString().compareTo("USER")==0||type.get_GranteeName().compareTo("#AUTHENTICATED-USERS")==0)
			{
				hasUsers=true;
				break;
			}
		}
		if(hasUsers)
		{
			AccessPermissionList listeAutorisation=Factory.AccessPermission.createList();
			Iterator iter2=f.get_Permissions().iterator();
			while (iter2.hasNext()){
				AccessPermission type = (AccessPermission)iter2.next();
				if(type.get_GranteeType().toString().compareTo("USER")!=0&&type.get_GranteeName().compareTo("#AUTHENTICATED-USERS")!=0)
				{
					listeAutorisation.add(type);
				}
			}
			f.set_Permissions(listeAutorisation);
			f.save(RefreshMode.REFRESH);
		}
	}
	
	public static void removePermissionsListFromFolder(Folder f,String[] PermissionList)
	{
		Iterator iter=f.get_Permissions().iterator();
		Boolean hasECM=false;
		List<String> permissions = Arrays.<String>asList(PermissionList);
		while (iter.hasNext()){
			AccessPermission type = (AccessPermission)iter.next();
			if(permissions.contains(type.get_GranteeName()))
			{
				hasECM=true;
				break;
			}
		}
		if(hasECM)
		{
			AccessPermissionList listeAutorisation=Factory.AccessPermission.createList();
			Iterator iter2=f.get_Permissions().iterator();
			while (iter2.hasNext()){
				AccessPermission type = (AccessPermission)iter2.next();
				if(!permissions.contains(type.get_GranteeName()))
				{
					listeAutorisation.add(type);
				}
			}
			f.set_Permissions(listeAutorisation);
			f.save(RefreshMode.REFRESH);
		}
	}

	public static void RemovePermissionsListFromDocument(Document doc,String[] PermissionList)
{
	Iterator iter=doc.get_Permissions().iterator();
	Boolean hasECM=false;
	List<String> permissions = Arrays.<String>asList(PermissionList);
	while (iter.hasNext()){
		AccessPermission type = (AccessPermission)iter.next();
		if(permissions.contains(type.get_GranteeName()))
		{
			hasECM=true;
			break;
		}
	}
	if(hasECM)
	{
		AccessPermissionList listeAutorisation=Factory.AccessPermission.createList();
		Iterator iter2=doc.get_Permissions().iterator();
		while (iter2.hasNext()){
			AccessPermission type = (AccessPermission)iter2.next();
			if(!permissions.contains(type.get_GranteeName()))
			{
				listeAutorisation.add(type);
			}
		}
		doc.set_Permissions(listeAutorisation);
		doc.save(RefreshMode.REFRESH);
	}
}

	public static AccessPermissionList getPermissionsforProjectFolder(String P8adminStr,String Admins,String Modifiers,String Viewers)
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
}
