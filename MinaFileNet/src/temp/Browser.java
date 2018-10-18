package temp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;


public class Browser {
	 private static  String Archivingpath="Y:\\Uploaded";
	public static void main(String[] args) throws Exception {
//		String docFolder="/Allegria/Allegria Ph.1/Banks/Arab African Bank/test 66/Incoming Letter";
//		docFolder=docFolder.replace("/", "\\");
//		File nf=new File("C://Users//mina//Desktop//New Text Document (8).txt");
//		MoveFileAfterUploading(docFolder, nf);
		ArrayList<File> docs= GetDocFromScanFolder();
		ArrayList<String> InNumbers=new ArrayList<String>();
		for (int i = 0; i < docs.size(); i++) 
    	{
			File f=docs.get(i);
			String docname=f.getName().substring(0,f.getName().indexOf('.'));
			String invoiceNo=docname.split("#")[0];
			System.out.println("DocNo : "+invoiceNo);
			if (InNumbers.contains(invoiceNo)) 
			{
				System.out.println("Duplicated File Path : "+f.getAbsolutePath());
				f.delete();
			}
			else
			{
				InNumbers.add(invoiceNo);
			}
    	}
		for (int i = 0; i < InNumbers.size(); i++) {
			System.out.println("Invoices Number : "+InNumbers.get(i));
		}
		System.err.println("All Invoices Count : "+InNumbers.size());
    }
	
	public static ArrayList<File> GetDocFromScanFolder()
	{
		ArrayList<File> files=new ArrayList<File>();
		String folderpath="F://scan";
		File folder=new File(folderpath);
		List<File> listOfFiles2 = (List<File>)FileUtils.listFiles(folder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for (int i = 0; i < listOfFiles2.size(); i++) 
		{
			if (listOfFiles2.get(i).isFile()&&listOfFiles2.get(i).getName().contains("#")) 
			{
				System.out.println("File " + listOfFiles2.get(i).getName());
				files.add(listOfFiles2.get(i));
			}
		}
		return files;
	}
	
	public static void MoveFileAfterUploading(String NewPath,File file)
	{
		File archiveFile=new File(Archivingpath.concat(NewPath).concat("\\").concat(file.getName()));
		System.out.println(archiveFile);
		try {
			FileUtils.moveFile(file, archiveFile);
//			UploaderScreen.writeToFile("File Moved To : "+archiveFile.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			UploaderScreen.writeToFile("File Failed to Move To : "+archiveFile.getAbsolutePath());
		}
	}
}