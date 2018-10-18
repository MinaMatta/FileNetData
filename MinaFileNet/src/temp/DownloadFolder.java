package temp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.security.auth.Subject;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;

public class DownloadFolder {
	public static final String URI ="http://172.16.10.65:9080/wsi/FNCEWS40MTOM/";					//On SODIC Site
	public static final String USERID = "filenet";													//On SODIC Site
	public static final String PASSWORD = "Fn1234567";												//On SODIC Site
	private static String DownloadPath="C:/Users/mina/Desktop";
	public static void main(String[] args) throws Exception 
	{
		Connection conn = Factory.Connection.getConnection(URI);
		Subject subj = UserContext.createSubject(conn, USERID, PASSWORD,null);
		UserContext uc = UserContext.get();
		uc.pushSubject(subj);
		Domain dom = Factory.Domain.getInstance(conn, null);
		ObjectStore os = Factory.ObjectStore.fetchInstance(dom,"FNOBS1", null);
		DownloadFolderContents(os, Factory.Folder.fetchInstance(os, new Id("{DA92D12A-EC1A-4C07-8C2A-8AB77A8D4B83}"), null));
	}
	
	public static void DownloadFolderContents(ObjectStore os,Folder folder) throws IOException
	{
		try 
		{
			FolderSet Folders=folder.get_SubFolders();
			Iterator SubFolders=Folders.iterator();
			while(SubFolders.hasNext()) 
			{
				Folder SubFolder =(Folder) SubFolders.next();
				DownloadFolderContents(os, SubFolder);
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
