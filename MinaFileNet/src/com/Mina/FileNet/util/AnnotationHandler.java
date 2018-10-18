package com.Mina.FileNet.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import com.filenet.api.collection.AnnotationSet;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Annotation;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;

public class AnnotationHandler {
	public static String CustomizeColumnsFileName=" Columns.xml";
	public static String EntryTemplateFileName=" Entry Templates.xml";
	public static void ChangeEntryTemplateandColumnsonFolderByName(ObjectStore os,String FolderName,String RootPath,File EntryXML,File ColumnsXML) throws Exception
	{
		String select = "d.Id";
		String from = "Folder";
		String where = "d.FolderName = '"+FolderName +"' AND d.This INSUBFOLDER '"+RootPath+"'";
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
				DeleteAnnotationsFromFolder(f);
				AssociateEntryTemplate(os, f, EntryXML);
				CustomizeColumns(os, f, ColumnsXML);
			}
		}
	}
	
	public static void UpdateFolderColumnsAndTemplates(ObjectStore os,Folder folder,String TemplatesFolderPath) throws IOException
	{
		
		
//		String title = folder.get_FolderName();
		
		String path = folder.get_PathName();
		String[] foldersArray = path.substring(1).split("/");
		String title = foldersArray[1];
		
			
				try {
					String filepath=TemplatesFolderPath+title+CustomizeColumnsFileName;
					File xml=new File(filepath);
					DeleteAnnotationsFromFolder(folder);
					CustomizeColumns(os, folder,xml);
				} catch (IOException e) {
					
				}
				try {
					String filepath2=TemplatesFolderPath+title+EntryTemplateFileName;
					File xml2=new File(filepath2);
					AssociateEntryTemplate(os, folder, xml2);
				} catch (IOException e) {
				}
			
		
	}
	
	public static void DeleteAnnotationsFromFolder(Folder f)
	{
		AnnotationSet anotations=f.get_Annotations();
		Iterator iter = anotations.iterator();
		while (iter.hasNext()) 
		{
			Annotation a=(Annotation) iter.next();
			a.delete();
			a.save(RefreshMode.REFRESH);
		}
	}
		
	public static void CustomizeColumns(ObjectStore os,Folder f,File xml) throws IOException{
		try
		{
			Annotation annObject = Factory.Annotation.createInstance(os, "Annotation");
			ContentTransfer ctObject = Factory.ContentTransfer.createInstance();
			ctObject.setCaptureSource(new FileInputStream(xml));
			ctObject.set_ContentType("application/x-filenet-folderprefs-columns");
			ContentElementList contentList = Factory.ContentElement.createList();
			annObject.set_MimeType("application/x-filenet-folderprefs-columns");
			annObject.set_DescriptiveText("Annotation applied to folder via CE Java API");
			annObject.set_AnnotatedObject(f);	
			contentList.add(ctObject);
			annObject.set_ContentElements(contentList);
			annObject.save(RefreshMode.REFRESH);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void AssociateEntryTemplate(ObjectStore os,Folder f,File xml) throws IOException{
		try
		{
			Annotation annObject = Factory.Annotation.createInstance(os, "CmXTFolderPreferences");
			ContentTransfer ctObject = Factory.ContentTransfer.createInstance();
			ctObject.setCaptureSource(new FileInputStream(xml));
			ctObject.set_ContentType("application/xml");
			ContentElementList contentList = Factory.ContentElement.createList();
			annObject.set_MimeType("application/x-filenet-folderprefs-templates");
			annObject.set_DescriptiveText("Annotation applied to folder via CE Java API");
			annObject.set_AnnotatedObject(f);	
			contentList.add(ctObject);
			annObject.set_ContentElements(contentList);
			annObject.save(RefreshMode.REFRESH);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
		
}
