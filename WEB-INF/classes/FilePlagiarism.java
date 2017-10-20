import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*; 
@WebServlet("/FileUpload")
@MultipartConfig
public class FilePlagiarism extends HttpServlet 
{
    private static final long serialVersionUID = 1L;
    protected void doPost(HttpServletRequest request,  HttpServletResponse response)       throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
	final Part filePart1 = request.getPart("file1");
	final Part filePart2 = request.getPart("file2");
	String topic=request.getParameter("nam");
	Random randomGenerator = new Random();
	int randomInt = randomGenerator.nextInt(1000);
	String bookId = Integer.toString(randomInt);
	int randomInt1 = randomGenerator.nextInt(1000);
	String bookid = Integer.toString(randomInt1);
        InputStream pdfFileBytes1 = null;
	InputStream pdfFileBytes2 = null;
        final PrintWriter writer = response.getWriter();
 	Connection  con=null;
        Statement stmt=null;
	String cont;
	String content;
	String w="/home/yash/Desktop/bookId.pdf";
	int lastDot = w.lastIndexOf('.');
	String s = w.substring(0,lastDot) + bookId + w.substring(lastDot);
	String v = w.substring(0,lastDot) + bookid + w.substring(lastDot);
	String p="/home/yash/Desktop/bookId.txt";
	int lastdot = p.lastIndexOf('.');
	String q = p.substring(0,lastdot) + bookId + p.substring(lastdot);
	String u = p.substring(0,lastdot) + bookid + p.substring(lastdot);
	File file11=new File(s);
	FileOutputStream output1=new FileOutputStream(file11);
	File file22=new File(v);
	FileOutputStream output2=new FileOutputStream(file22);
        try 
	{
 
          if (!filePart1.getContentType().equals("binary/octet-stream"))
            {
                       writer.println("<br/> Invalid File");
                       return;
            }
 
           else if (filePart1.getSize()>1048576 ) 
	    { 
              writer.println("<br/> File size too big");
              return;
           }
 
            pdfFileBytes1 = filePart1.getInputStream(); 
 
            final byte[] byts = new byte[pdfFileBytes1.available()];
             pdfFileBytes1.read(byts); 
		try {
                     Class.forName("com.mysql.jdbc.Driver");
                     con = DriverManager.getConnection("jdbc:mysql://localhost:3306/yash","root","hpmania");
                  } catch (Exception e) {
                        System.out.println(e);
                        System.exit(0);
                              }
 
 
                int success=0;
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO Book153 VALUES(?,?)");
                pstmt.setString(1, bookId);
		//pstmt.setString(2,topic);
                pstmt.setBytes(2,byts);    
                success = pstmt.executeUpdate();
 
        } catch (FileNotFoundException fnf) {
            writer.println("You  did not specify a file to upload");
            writer.println("<br/> ERROR: " + fnf.getMessage());
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
	try
	{
            	Statement state = con.createStatement();
	    	ResultSet rs = state.executeQuery("select BookContent from Book153 where BookId=bookId");
	    	while(rs.next()) 
	    	{
			InputStream input = rs.getBinaryStream("BookContent");
			byte[] buff=new byte[102458];
			while(input.read(buff)>0)
			{
				output1.write(buff);
			}
	    	}
		output1.close();
		try 
		{
	      	      Document document = new Document();
	      		PdfDictionary dictionary=null;
	      		PRStream stream=null;
		        document.open();
	      		FileOutputStream fos=new FileOutputStream(q);
		      	PdfReader reader = new PdfReader(s);
	      		int ret = reader.getNumberOfPages();
		     	for(int i=1;i<=ret;i++)
			{
      		      		dictionary = reader.getPageN(i);
		      		PRIndirectReference reference = (PRIndirectReference) 
	  		        dictionary.get(PdfName.CONTENTS);
        	      		stream = (PRStream) PdfReader.getPdfObject(reference);
        	      		byte[] byts = PdfReader.getStreamBytes(stream);
        	      		PRTokeniser tokenizer = new PRTokeniser(byts);
        	      		StringBuffer buffer = new StringBuffer();
	        	      	while (tokenizer.nextToken()) 
		      		{
    		          		if (tokenizer.getTokenType() == PRTokeniser.TK_STRING) 
			  		{
        	          			buffer.append(tokenizer.getStringValue());
	
        	          		}
        	        	}
      	              		String test=buffer.toString();
       		     		StringReader stReader = new StringReader(test);
            	      		int t;
	              		while((t=stReader.read())>0)
        	        		fos.write(t);
			}
        	      document.add(new Paragraph(".."));
              	      document.close();
      		}
        	catch (Exception e) 
		{
		}
		
		try
		{
			String line;
			StringBuffer sb = new StringBuffer();
			FileInputStream fis = new FileInputStream(q);
			BufferedReader reads=new BufferedReader ( new InputStreamReader(fis));
			while((line = reads.readLine()) != null) 
			{
		
				line = line.replaceAll("[^a-zA-Z0-9]","");
				sb.append(line+"\n");
			}
			reads.close();
			BufferedWriter out=new BufferedWriter ( new FileWriter(q));
			out.write(sb.toString());
			out.close();
		}
		catch (Throwable e) 
		{
			System.err.println("*** exception ***" + e);
		}
 
	      }
	     catch(SQLException e)
	     {
             }
	cont = new Scanner(new File(q)).useDelimiter("\\Z").next();
	try 
	{
 
          if (!filePart2.getContentType().equals("binary/octet-stream"))
            {
                       writer.println("<br/> Invalid File");
                       return;
            }
 
           else if (filePart2.getSize()>1048576 ) 
	    {
              writer.println("<br/> File size too big");
              return;
           }
	    pdfFileBytes2 = filePart2.getInputStream(); 	
	    final byte[] byt = new byte[pdfFileBytes2.available()];
             pdfFileBytes2.read(byt);  
                int success=0;
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO Book153 VALUES(?,?)");
                pstmt.setString(1, bookid);
		//pstmt.setString(2,topic);
                pstmt.setBytes(2,byt);   
                success = pstmt.executeUpdate();
        } catch (FileNotFoundException fnf) {
            writer.println("You  did not specify a file to upload");
            writer.println("<br/> ERROR: " + fnf.getMessage());
 	 } catch (SQLException e) {
            e.printStackTrace();
        }
	try
	{
            	Statement state = con.createStatement();
	    	ResultSet rs = state.executeQuery("select BookContent from Book153 where BookId=bookid");
	    	while(rs.next()) 
	    	{
			InputStream input = rs.getBinaryStream("BookContent");
			byte[] buff=new byte[102458];
			while(input.read(buff)>0)
			{
				output2.write(buff);
			}
	    	}
		output2.close();
		try 
		{
	      	      Document document = new Document();
	      		PdfDictionary dictionary=null;
	      		PRStream stream=null;
		        document.open();
	      		FileOutputStream fos=new FileOutputStream(u);
		      	PdfReader reader = new PdfReader(v);
	      		int ret = reader.getNumberOfPages();
		     	for(int i=1;i<=ret;i++)
			{
      		      		dictionary = reader.getPageN(i);
		      		PRIndirectReference reference = (PRIndirectReference) 
	  		        dictionary.get(PdfName.CONTENTS);
        	      		stream = (PRStream) PdfReader.getPdfObject(reference);
        	      		byte[] byts = PdfReader.getStreamBytes(stream);
        	      		PRTokeniser tokenizer = new PRTokeniser(byts);
        	      		StringBuffer buffer = new StringBuffer();
	        	      	while (tokenizer.nextToken()) 
		      		{
    		          		if (tokenizer.getTokenType() == PRTokeniser.TK_STRING) 
			  		{
        	          			buffer.append(tokenizer.getStringValue());
	
        	          		}
        	        	}
      	              		String test=buffer.toString();
       		     		StringReader stReader = new StringReader(test);
            	      		int t;
	              		while((t=stReader.read())>0)
        	        		fos.write(t);
			}
        	      document.add(new Paragraph(".."));
              	      document.close();
      		}
        	catch (Exception e) 
		{
		}
		
		try
		{
			String line;
			StringBuffer sb = new StringBuffer();
			FileInputStream fis = new FileInputStream(u);
			BufferedReader reads=new BufferedReader ( new InputStreamReader(fis));
			while((line = reads.readLine()) != null) 
			{
		
				line = line.replaceAll("[^a-zA-Z0-9]","");
				sb.append(line+"\n");
			}
			reads.close();
			BufferedWriter out=new BufferedWriter ( new FileWriter(u));
			out.write(sb.toString());
			out.close();
		}
		catch (Throwable e) 
		{
			System.err.println("*** exception ***" + e);
		}
 
	      }
	     catch(SQLException e)
	     {
             }
	content = new Scanner(new File(u)).useDelimiter("\\Z").next();
		try
		{
                	PreparedStatement ps = con.prepareStatement("INSERT INTO tringS VALUES(?,?,?)");
                	ps.setString(1, bookId);
			ps.setString(2,topic);
                	ps.setString(3,cont);    
               		ps.executeUpdate();
		}
		catch(SQLException e)
	     	{
             	}
		try
		{
                	PreparedStatement ps = con.prepareStatement("INSERT INTO tringS VALUES(?,?,?)");
                	ps.setString(1, bookid);
			ps.setString(2,topic);
                	ps.setString(3,content);    
               		ps.executeUpdate();
		}
		catch(SQLException e)
	     	{
             	}
   int x,y,z=0,m=0,t=0;
	int[] a=new int[200000];
	int[] b=new int[200000];
	for(x=0;x<=content.length()-2;x++)
	{
		
		y=x+2;
		String st=content.substring(x,y);
		a[m]=createHash(st);
		m++;
		z++;
	}
	for(x=0;x<=cont.length()-2;x++)
	{
		
		y=x+2;
		String st=cont.substring(x,y);
		b[t]=createHash(st);
		t++;
	}
	int i, minpos=0,k=0,prevpos=0,g=0;
		int fingers[]=new int[200000];
		int fingers2[]=new int[200000];
		for(i=0;i<(m-3);i++)
		{
			minpos=findMin(a, i, i+3);
			fingers[k]=a[minpos];
			if(i!=0 && fingers[k]==fingers[k-1] && minpos==prevpos)
			{
				fingers[k]=0;
				k--;
			}
			else if(i!=0 && fingers[k]==fingers[k-1] && minpos>prevpos)
			{
				fingers[k]=a[minpos];
			}          
			prevpos=minpos;	
			k++;
		}
		for(i=0;i<(t-3);i++)
		{
			minpos=findMin(b, i, i+3);
			fingers2[g]=b[minpos];
			if(i!=0 && fingers2[g]==fingers2[g-1] && minpos==prevpos)
			{
				fingers2[g]=0;
				g--;
			}
			else if(i!=0 && fingers2[g]==fingers2[g-1] && minpos>prevpos)
			{
				fingers2[g]=b[minpos];
			}          
			prevpos=minpos;	
			g++;
		}
		int h=0;
		int f=0;
		int d=0;
		Arrays.sort(fingers,0,k);
		for(i=0;i<k;i++)
		{
			int r=Arrays.binarySearch(fingers,0,k,fingers2[i]);
			if(r>=0)
				d++;
		}
		float e=(d*100)/(k+g-d);
		writer.println("Percentage Of Plagiarism is:"+ e);
    }
    static int createHash(String s)
    {
	int prime=101;
	char[] a= s. toCharArray();
	int hash=0;
	for(int i=0;i<s.length();i++)
	{
		hash+=a[i]*Math.pow(prime,i);
	}	
	return hash;
    }
    static int findMin(int[] a, int i, int j) 
    {
	int minValue = i;
	int k;
	for (k=i+1; k<=j; k++) 
	{
        	if (a[k] <= a[minValue]) 
		{
            		minValue = k;
        	}
    	}
    	return minValue;
    }
}
