package com.Mina.FileNet.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.DynamicReferentialContainmentRelationship;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.util.Id;

public class FolderHandler {

	public static void ChangeEntryTemplateandColumnsonFolderByName(ObjectStore os,String FolderName,File EntryXML,File ColumnsXML) throws Exception
	{
		String select = "d.Id";
		String from = "Folder";
		String where = "d.FolderName = '"+FolderName+"'";
		IndependentObjectSet set = FilenetSearch.searchForObjectsInObjectStore(os,select,from,where,null,0);
		Folder f=null;
		if(set.isEmpty()==true)
	 	{
			System.out.println("No Data !!!");
		}
		else
		{
			for ( Iterator i = set.iterator(); i.hasNext(); ) 
			{
				f=(Folder)i.next();
				f=Factory.Folder.fetchInstance(os, f.get_Id(), null);
				System.out.println(f.get_FolderName()+"\t"+f.get_Id().toString());
				AnnotationHandler.DeleteAnnotationsFromFolder(f);
				AnnotationHandler.AssociateEntryTemplate(os, f, EntryXML);
				AnnotationHandler.CustomizeColumns(os, f, ColumnsXML);
			}
		}
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
	
	public static boolean fillInFolder(ObjectStore os, Id docId, String folderPath){
		Document myDoc = Factory.Document.fetchInstance(os, docId, null);
		Folder parentFolder = Factory.Folder.fetchInstance(os, folderPath, null);
		try {
			DynamicReferentialContainmentRelationship drcr = (DynamicReferentialContainmentRelationship) parentFolder.file(myDoc, AutoUniqueName.AUTO_UNIQUE, myDoc.get_Name(),DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
			drcr.save(RefreshMode.REFRESH);
			return true;
		}catch(Exception e){
			try{
				}catch(Exception t){
					System.out.println(t.getMessage());
				}
			return false;
		}
	}
	
	public static void unfileFromAllFolders (ObjectStore os, Id docId){
		try{
			Document myDoc = Factory.Document.fetchInstance(os, docId, null);
			FolderSet folders = myDoc.get_FoldersFiledIn();
			Iterator iterator = folders.iterator();
			while(iterator.hasNext()){
				unfillFromFolder(os,docId,((Folder)iterator.next()).get_PathName());
				}
		}catch(Exception e){
			try{
				}catch(Exception t){
					System.out.println(t.getMessage());
				}
		}
	}
	
	public static boolean unfillFromFolder (ObjectStore os, Id docId, String folderPath){
		Document myDoc = Factory.Document.fetchInstance(os, docId, null);
	    Folder parentFolder = Factory.Folder.fetchInstance(os, folderPath, null);
	    try {
	    	ReferentialContainmentRelationship drcr = (ReferentialContainmentRelationship) parentFolder.unfile(myDoc);
		    drcr.save(RefreshMode.REFRESH);   
	        return true;
	     }catch(Exception e){
	     	try{
				}catch(Exception t){
					System.out.println(t.getMessage());
				}
	    	 return false;
	     }
		
	}
	
	public static void makeAllFoldersinPath(ObjectStore os,String Path)
		{
			String[] foldersArray=Path.substring(1).split("/");
			for (int i = 0; i < foldersArray.length; i++) {
				if(i==0)//add folder
				{
					if(isFolderExist(os,"/".concat(foldersArray[0]))==false)
						addFolder(os,foldersArray[0]);
				}
				else//add subfolder
				{
					String ParentFolderPath="";
					for (int j = 0; j < i; j++) {
						ParentFolderPath+="/";
						ParentFolderPath+=foldersArray[j];
					}
					if(isFolderExist(os,ParentFolderPath.concat("/").concat(foldersArray[i]))==false)
						addSubFolder(os,ParentFolderPath,foldersArray[i]);
				}
			}
		}
	
	public static void DeleteFolderContents(ObjectStore os,Folder folder) throws IOException
	{
		try 
		{
			FolderSet Folders=folder.get_SubFolders();
			Iterator SubFolders=Folders.iterator();
			while(SubFolders.hasNext()) 
			{
				Folder SubFolder =(Folder) SubFolders.next();
				DeleteFolderContents(os, SubFolder);
		    }
			DocumentSet myDocs = folder.get_ContainedDocuments();
			Iterator myDocsIterator=myDocs.iterator();
			while(myDocsIterator.hasNext()) 
			{
				Document Doc =(Document) myDocsIterator.next();
				Doc.delete();
				Doc.save(RefreshMode.NO_REFRESH);
		    }
			folder.delete();
			folder.save(RefreshMode.NO_REFRESH);
		} 
		catch (Exception e) 
		{
			System.out.println("Error While Deleting Request Folder "+e.getMessage());
		}
	}
	
	public static void DownloadFolderContents(ObjectStore os,Folder folder,String DownloadPath) throws IOException
	{
		try 
		{
			FolderSet Folders=folder.get_SubFolders();
			Iterator SubFolders=Folders.iterator();
			while(SubFolders.hasNext()) 
			{
				Folder SubFolder =(Folder) SubFolders.next();
				DownloadFolderContents(os, SubFolder, DownloadPath);
		    }
			DocumentSet myDocs = folder.get_ContainedDocuments();
			Iterator myDocsIterator=myDocs.iterator();
			String FolderPath=folder.get_PathName();
			String[] foldersArray = FolderPath.substring(1).split("/");
			String currentPath= DownloadPath;
			for (int i = 0; i < foldersArray.length; i++) {
				currentPath=currentPath.concat("/").concat(substituteInvalideChars(foldersArray[i]));
			}
			System.out.println(currentPath);
			File folderOnDisk=new File(currentPath);
			folderOnDisk.mkdirs();
			while(myDocsIterator.hasNext()) 
			{
				Document Doc =(Document) myDocsIterator.next();
				downloadDocContent(Doc, currentPath);
		    }
		} 
		catch (Exception e) 
		{
			System.out.println("Error While Deleting Request Folder "+e.getMessage());
		}
	}
	
	public static void downloadDocContent(Document doc, String path)
    {
		ContentTransfer ce=(ContentTransfer)doc.get_ContentElements().listIterator().next();
    	String fileName =ce.get_RetrievalName();
    	File f = new File(path,fileName);
    	InputStream is = doc.accessContentStream(0);
    	int c = 0;
    	try 
        {
        	FileOutputStream out = new FileOutputStream(f);
        	c = is.read();
        	while ( c != -1)
        	{
        		out.write(c);
        		c = is.read();
        	}
			is.close();
			out.close();
		} 
    	catch (IOException e) 
		{
			e.printStackTrace();
		}
    }

	public static String substituteInvalideChars (String prop){
		try{
			if(prop.indexOf('"')!=-1){
				prop = prop.replace('"', '^');
			}
			if(prop.indexOf('|')!=-1){
				prop = prop.replace('|', '^');
			}
			if(prop.indexOf('\\')!=-1){
				prop = prop.replace('\\', '-');
			}
			if(prop.indexOf('/')!=-1){
				prop = prop.replace('/', '-');
			}
			if(prop.indexOf(':')!=-1){
				prop = prop.replace(':', '^');
			}
			if(prop.indexOf('*')!=-1){
				prop = prop.replace('*', '^');
			}
			if(prop.indexOf('?')!=-1){
				prop = prop.replace('?', '^');
			}
			return prop;
		}catch(Exception e){
			
			return "";
		}
		
	}
}
