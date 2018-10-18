package temp;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.security.auth.Subject;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;

public class UMCMigration {

	public final static String DRIVER_NAME ="sun.jdbc.odbc.JdbcOdbcDriver";
	public final static String DATABASE_URL1 = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ=%s;READONLY=TRUE";
	private static final String URI ="http://172.16.10.38:9080/wsi/FNCEWS40MTOM/";
	private static final String USERID = "filenet";
	static ArrayList<PlotFolder> data=null;
	private static final String PASSWORD = "Fn1234567";
	public static final String DATABASE_URL_Write = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ=";
	public static void main(String[] args) 
	{
		File f=new File("Y:\\IBM\\Customers\\SODIC\\UMC\\UMC All Zones Data.xlsx");
		
		try {
			data = getData(f);
			addPlotFolders(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void addPlotFolders(ArrayList<PlotFolder> data2) 
	{
		com.filenet.api.core.Connection conn = Factory.Connection.getConnection(URI);
		Subject subj = UserContext.createSubject(conn, USERID, PASSWORD,null);
		UserContext uc = UserContext.get();
		uc.pushSubject(subj);
		Domain dom = Factory.Domain.getInstance(conn, null);
		ObjectStore os = Factory.ObjectStore.fetchInstance(dom,"UMC", null);
		for (int i = 0; i < data2.size(); i++) 
		{
			try {
				Folder contractFolder = Factory.Folder.createInstance(os,"PlotNoFolder");
				contractFolder.set_FolderName(data2.get(i).PlotNo.trim());
				contractFolder.getProperties().putValue("Zone",data2.get(i).Zone.trim());
				contractFolder.getProperties().putValue("PlotNo",data2.get(i).PlotNo.trim());
				if (data2.get(i).PlotType!=null&&data2.get(i).PlotType.length()>0) {
					contractFolder.getProperties().putValue("PlotType",data2.get(i).PlotType.trim());
				}
				if (data2.get(i).ProtoType!=null&&data2.get(i).ProtoType.length()>0) {
					contractFolder.getProperties().putValue("Prototype",data2.get(i).ProtoType.trim());
				}
				if (data2.get(i).ProtoTypeDesc!=null&&data2.get(i).ProtoTypeDesc.length()>0) {
					contractFolder.getProperties().putValue("PrototypeDescription",data2.get(i).ProtoTypeDesc.trim());
				}
				// specify the path of \Allegria\<Zone>\Folder
				contractFolder.set_Parent(Factory.Folder.fetchInstance(os,"/".concat("Allegria").concat("/").concat(data2.get(i).Zone), null));
				contractFolder.getProperties().putValue("LastRequestNo",0);
				contractFolder.save(RefreshMode.REFRESH);
				System.out.println("Successfully Added "+data2.get(i).PlotNo);
				updateFileNetColumn("Uploaded", "Y:\\IBM\\Customers\\SODIC\\UMC\\UMC All Zones Data.xlsx", "Row", data2.get(i).RowNumber);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static ArrayList<PlotFolder> getData(File file) throws SQLException {
		

		ArrayList<PlotFolder> list = new ArrayList<PlotFolder>();
		try
	  {
			String filename="Sheet1";
			String filepath=String.format(DATABASE_URL1, file.getAbsolutePath());
			Class.forName(DRIVER_NAME);
	    Connection conn=DriverManager.getConnection(filepath);
	    Statement stment = conn.createStatement();
	    String qry = "SELECT * FROM ["+filename+"$] WHERE [Filenet] IS NULL";
	    ResultSet rs = stment.executeQuery(qry);
	    ResultSetMetaData rsmd = rs.getMetaData();
	    System.out.println(rsmd.getColumnCount());
	    
	    while(rs.next())
	    {
	    	 PlotFolder r=new PlotFolder();
					r.Zone=rs.getString("Zone");
					r.ProtoTypeDesc=rs.getString("Prototypes Description");
					r.ProtoType=rs.getString("Prototype");
					r.PlotType=rs.getString("Plot Type");
					r.PlotNo=rs.getString("Plot No");
					r.RowNumber=rs.getString("Row");
					list.add(r);
	    }
	    rs.close();
	  }
	  catch(Exception err)
	  {
	    System.out.println(err);
	  }
		finally {
	    return list;
	   }
	 }

	public static void updateFileNetColumn(String FileNetValue, String ExcelPath,String RowUniqueColumn,String RowUniqueValue)throws ClassNotFoundException, SQLException, Exception{
    	String sheetName = "Sheet1";
    	
    	
    	Class.forName(DRIVER_NAME);
        java.sql.Connection con = null;
        Statement stmt=null;
          try {
             con = DriverManager.getConnection(DATABASE_URL_Write.concat(ExcelPath+";READONLY=FALSE"));
             stmt = con.createStatement();
             stmt.executeUpdate("UPDATE ["+sheetName+"$] SET [Filenet] = '"+FileNetValue+"' Where ["+RowUniqueColumn+"] = '"+RowUniqueValue+"' ;");
             stmt.close();
             }catch(Exception e){
             	e.printStackTrace();
             }finally {
            	 if(stmt!=null)
            		 stmt.close();
    	         if (con != null)
    	            con.close();
    	      }
    }


}



