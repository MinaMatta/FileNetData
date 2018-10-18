package temp;

import javax.mail.*;
import javax.mail.internet.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class SendMail extends javax.mail.Authenticator
{


	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_AUTH_USER = "xeroxjavatest@gmail.com";
	private static final String SMTP_AUTH_PWD  = "P@ssw0rd12345678";
	private static final String from = "xeroxjavatest@gmail.com";
	private static String logFile="";
    
	Date today = Calendar.getInstance().getTime();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");

  public void SendMail( String[] recipients, String subject, String body) throws MessagingException
  {
    
    	try {
        	GetLogFilePath();
        	writeToFile(logFile,true,"----------------------- SendMailUsingAuthentication.PostMail Triggered ------------------");
            boolean debug = false;

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
    		props.put("mail.smtp.socketFactory.port", "465");
    		props.put("mail.smtp.socketFactory.class",
    				"javax.net.ssl.SSLSocketFactory");
    		props.put("mail.smtp.auth", "true");
    		props.put("mail.smtp.port", "465");
            Session session = Session.getInstance(props, this);
            session.setDebug(debug);

            MimeMessage msg = new MimeMessage(session);
          
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);
            InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i],true);
            }
            msg.addRecipients(Message.RecipientType.TO, addressTo);

            msg.setSubject(subject, "UTF-8");
           
    	    msg.setText(body, "UTF-8", "html");
            Transport.send(msg);
        } 
        catch (Throwable e) 
        {
        	try
        	{	
        		e.printStackTrace();
    			writeToFile(logFile,true,formatter.format(today)+"---Exception--------SendMailUsingAuthentication.postMail------------");
    			PrintWriter out = new PrintWriter(new FileWriter(logFile,true), true);
    			e.printStackTrace(out);
    			out.close();
    		}catch(Exception t){}
        }
    }
  
  
  
  public static void writeToFile(String file_path , boolean append_value,String textLine ) throws IOException {
	
	FileWriter write = new FileWriter( file_path , append_value);
	PrintWriter print_line = new PrintWriter( write );
	String[] textArray = {textLine};
	print_line.printf( "%s" + "%n" , textArray);
	print_line.close();
	}

 
private void GetLogFilePath() throws IOException
{
SimpleDateFormat Logformatter = new SimpleDateFormat("yyyy-MM-dd");
String FileName=Logformatter.format(today).concat(".txt");
String ClogFile = "C:/IBM/Development/Log"; //OnVmware
String DlogFile = "D:/IBM/Development/Log";  //On Site
File f = new File(DlogFile);
if(f.exists() && f.isDirectory()){ 
	File f2=new File(DlogFile.concat("/").concat(FileName));
	if(f2.exists() && !f2.isDirectory())
	{
		logFile=DlogFile.concat("/").concat(FileName);
	}
	else
	{
		f2.createNewFile();
		logFile=DlogFile.concat("/").concat(FileName);
	}
}
else{
	File f2=new File(ClogFile.concat("/").concat(FileName));
	if(f2.exists() && !f2.isDirectory())
	{
		logFile=ClogFile.concat("/").concat(FileName);
	}
	else
	{
		f2.createNewFile();
		logFile=ClogFile.concat("/").concat(FileName);
	}
}
}	

public PasswordAuthentication getPasswordAuthentication()
{
    String username = SMTP_AUTH_USER;
    String password = SMTP_AUTH_PWD;
    return new PasswordAuthentication(username, password);
}
}


