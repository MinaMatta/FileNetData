package com.Mina.FileNet.util;

import java.util.Iterator;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.MarkingList;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.Marking;
import com.filenet.api.security.MarkingSet;
import com.filenet.api.util.Id;

public class MarkingSetHandler {

	public static void ReplaceMarkingSetValue(Document Doc,String MarkingSetListProperty,String NewValue)
	{
		try 
		{
			Doc.refresh();
			Doc.getProperties().getStringListValue(MarkingSetListProperty).remove(0);
			Doc.getProperties().getStringListValue(MarkingSetListProperty).add(NewValue);
			System.out.println("Changed to "+NewValue);
			Doc.save(RefreshMode.REFRESH);
		}
		catch (Exception e) 
		{	
		}
	}

	public static boolean CheckMarkingExist(Domain dom, Id id,String markingValue){
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

	public static void addMarking(Domain dom,Id MarkingSetId,String markingValue,String P8adminStr,String Admins,String Modifiers,String Viewers)
	{
		int APAdminAccessMask=234881024;
		int APModifierAccessMask=201326592;
		int APViewerAccessMask=134217728;
		MarkingSet markingSet = Factory.MarkingSet.fetchInstance(dom, MarkingSetId,null);
		Marking marking=Factory.Marking.createInstance();
		marking.set_MarkingValue(markingValue);
		marking.set_ConstraintMask(-1);
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
		marking.set_Permissions(APL);
		markingSet.get_Markings().add(marking);
		markingSet.save(RefreshMode.REFRESH);
	}

}
