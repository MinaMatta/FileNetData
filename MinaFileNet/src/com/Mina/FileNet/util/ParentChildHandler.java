package com.Mina.FileNet.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import com.filenet.api.constants.ComponentRelationshipType;
import com.filenet.api.constants.CompoundDocumentState;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.VersionBindType;
import com.filenet.api.core.ComponentRelationship;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.Id;

public class ParentChildHandler {
	public void AttachChildToParent(ObjectStore os,Id ChildID,Id ParentID,String RelationName) 
	{
		Document parent = Factory.Document.fetchInstance(os, ParentID,null);
		Document child =Factory.Document.fetchInstance(os, ChildID,null);
		try
		{	
			parent.setUpdateSequenceNumber(null);
			parent.set_CompoundDocumentState(CompoundDocumentState.COMPOUND_DOCUMENT);
	    	parent.save(RefreshMode.REFRESH);
	    	ComponentRelationship cr = Factory.ComponentRelationship.createInstance(os,null);
	    	cr.set_ParentComponent(parent);
	        cr.set_ChildComponent(child);
	        cr.set_Name(RelationName);
	        cr.set_ComponentRelationshipType(ComponentRelationshipType.DYNAMIC_CR);
	        cr.set_VersionBindType(VersionBindType.LATEST_MAJOR_VERSION);
	        cr.save(RefreshMode.REFRESH);
		}
		catch(Exception e)
		{
		}
	}
	
	public void unattachAttachmentFromRequest(ObjectStore os, Document childDoc) throws IOException {
		ComponentRelationship compRel=(ComponentRelationship)childDoc.get_ParentRelationships().iterator().next();
//		Document parentDoc = compRel.get_ParentComponent();
		ComponentRelationshipType CRType=compRel.get_ComponentRelationshipType();
        if(!CRType.equals(ComponentRelationshipType.URICR))
        {
        	compRel.delete();
        	compRel.save(RefreshMode.REFRESH);
        }
	}
	
	private void removeCompundState(ObjectStore os, Document doc) throws IOException
	{
		Iterator itr=doc.get_ChildDocuments().iterator();
		if (!itr.hasNext()) 
		{
			doc.set_CompoundDocumentState(CompoundDocumentState.STANDARD_DOCUMENT);
			doc.save(RefreshMode.REFRESH);
		}
	}
}
