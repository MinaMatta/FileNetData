package com.Mina.FileNet.util;

import com.filenet.api.admin.CmChangePreprocessorDefinition;
import com.filenet.api.admin.SubscribableClassDefinition;
import com.filenet.api.collection.CmChangePreprocessorDefinitionList;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.events.CmChangePreprocessorAction;
import com.filenet.api.util.Id;

public class ProcessHandlersHandler {

	public static void associateChangePreprocessortoDocumentClass(ObjectStore os,Id ChangePreActionId,String ChangePreprocessorActionName,Id DocumentClassId)
	{
		//Create change preprocessor definition object.
		CmChangePreprocessorDefinition cpDef = Factory.CmChangePreprocessorDefinition.createInstance(os);
		cpDef.set_DisplayName(ChangePreprocessorActionName);
		cpDef.set_IsEnabled(true);

		//Get CmChangePreprocessorAction and set on definition object.
		CmChangePreprocessorAction action = Factory.CmChangePreprocessorAction.getInstance(os, "CmChangePreprocessorAction",ChangePreActionId);

		cpDef.set_ChangePreprocessorAction(action);

		CmChangePreprocessorDefinitionList cpdList=Factory.CmChangePreprocessorDefinition.createList();
		cpdList.add(cpDef);

		//Get AccountsReceivable class definition and set definition list object on it. 
		SubscribableClassDefinition objClassDef = Factory.SubscribableClassDefinition.getInstance(os, DocumentClassId);
		objClassDef.set_ChangePreprocessorDefinitions(cpdList);

		objClassDef.save(RefreshMode.REFRESH);
	}
	
}
