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
@WebServlet("/LocalP")
@MultipartConfig
public class LocalP extends HttpServlet 
{
    private static final long serialVersionUID = 1L;
    public void doPost(HttpServletRequest request,  HttpServletResponse response)       throws ServletException, IOException 
    {
	//Part alpha=null;
        response.setContentType("text/html;charset=UTF-8");
	final Part filePart = request.getPart("fileid");
	//final Part filePart2 = request.getPart("file2");
	String topic=request.getParameter("nams");
	Random randomGenerator = new Random();
	int randomInt = randomGenerator.nextInt(1000);
	String bookId = Integer.toString(randomInt);
	//int randomInt1 = randomGenerator.nextInt(1000);
	//String bookid = Integer.toString(randomInt1);
        InputStream pdfFileBytes = null;
	//InputStream pdfFileBytes2 = null;
        final PrintWriter writer = response.getWriter();
 	Connection  con=null;
        Statement stmt=null;
	String cont;
	//writer.println("topic is" + topic);
	//writer.println("content type is :" + filePart.getContentType());
	//writer.println("bookid is " + bookId);
        try 
	{
 
          if (!filePart.getContentType().equals("binary/octet-stream"))
            {
                       writer.println("<br/> Invalid File");
                       return;
            }
 
           else if (filePart.getSize()>1048576 ) 
	    { //2mb
              writer.println("<br/> File size too big");
              return;
           }
 
            pdfFileBytes = filePart.getInputStream();  // to get the body of the request as binary data
 
            final byte[] byts = new byte[pdfFileBytes.available()];
             pdfFileBytes.read(byts);  //Storing the binary data in bytes array.
 
 
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
                pstmt.setBytes(2,byts);    //Storing binary data in blob field.
                success = pstmt.executeUpdate();
                //if(success>=1)  //System.out.println("Book Stored");
                 //con.close(); 
 
                 //writer.println("<br/> Book Successfully Stored");
 
        } catch (FileNotFoundException fnf) {
            writer.println("You  did not specify a file to upload");
            writer.println("<br/> ERROR: " + fnf.getMessage());
 
        } catch (SQLException e) {
            e.printStackTrace();
        } /*finally {
 
            if (pdfFileBytes != null) {
                pdfFileBytes.close();
            }
            if (writer != null) {
                writer.close();
            }
        }*/
	String w="/home/yash/Desktop/bookId.pdf";
	int lastDot = w.lastIndexOf('.');
	String s = w.substring(0,lastDot) + bookId + w.substring(lastDot);
	File file=new File(s);
	String p="/home/yash/Desktop/bookId.txt";
	int lastdot = p.lastIndexOf('.');
	String q = p.substring(0,lastdot) + bookId + p.substring(lastdot);
		FileOutputStream output=new FileOutputStream(file);
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
				output.write(buff);
			}
	    	}
		output.close();
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
        	      		//FileOutputStream fos=new FileOutputStream("/home/yash/Desktop/sop26.txt");
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
        	catch (Exception e) {}
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
			//cont = new Scanner(new File("/home/yash/Desktop/sop28.txt")).useDelimiter("\\Z").next();
			//writer.println(cont);
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
                //int success=0;
		try
		{
                	PreparedStatement ps = con.prepareStatement("INSERT INTO tringS VALUES(?,?,?)");
                	ps.setString(1, bookId);
			ps.setString(2,topic);
                	ps.setString(3,cont);    //Storing binary data in blob field.
               		ps.executeUpdate();
		}
		catch(SQLException e)
	     	{
             	}
		int u,v,t=0;
		int[] a=new int[200000];
		for(u=0;u<=cont.length()-2;u++)
		{
		
			v=u+2;
			String st=cont.substring(u,v);
			a[t]=createHash(st);
			t++;
		}
		int i, min=0,k=0,prev=0,c=0;
		int fingers[]=new int[200000];
		//int fingers2[]=new int[200000];
		for(i=0;i<(t-3);i++)
		{
			min=findMin(a, i, i+3);
			fingers[k]=a[min];
			if(i!=0 && fingers[k]==fingers[k-1] && min==prev)
			{
				fingers[k]=0;
				k--;
			}
			else if(i!=0 && fingers[k]==fingers[k-1] && min>prev)
			{
				fingers[k]=a[min];
			}          
			prev=min;	
			k++;
		}
		//Arrays.sort(fingers,0,k);
		//writer.println("hii"+"\n");
		try
		{
			PreparedStatement st=con.prepareStatement("select contents from tringS where topicname=?");
			st.setString(1,topic);
			//writer.println("hello");
			//writer.println(topic);
			ResultSet rst=st.executeQuery();
			while(rst.next())
			{
				String str=rst.getString(1);
				//String n7=rst.getString("topicname");
				//writer.println(str);
				//writer.println(n7);
				int x,y,m=0;
				int[] b=new int[200000];
				for(x=0;x<=str.length()-2;x++)
				{
					y=x+2;
					String sts=str.substring(x,y);
					//writer.println(sts);
					b[m]=createHash(sts);
					m++;
				}	
				int minpos=0,prevpos=0,g=0;
				//int fingers[]=new int[200000];
				int fingers2[]=new int[200000];
				for(i=0;i<(m-3);i++)
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
				//Arrays.sort(fingers,0,k);
				if(k>=g)
				{
					Arrays.sort(fingers,0,k);
					for(i=0;i<k;i++)
					{
						int r=Arrays.binarySearch(fingers,0,k,fingers2[i]);
						if(r>=0)
							d++;
					}
					//writer.println(d);
					//writer.println(g);
					//writer.println(k);
					//float num1=k;
					//float num2=g;	
					float e=(d*100)/(k+g-d);
					writer.println("Percentage Of Plagiarism from file"+ c + "is:"+ e);	
					c++;
				}
				else if(k<g)
				{
					Arrays.sort(fingers2,0,g);
					for(i=0;i<g;i++)
					{
						int r=Arrays.binarySearch(fingers2,0,g,fingers[i]);
						if(r>=0)
							d++;
					}
					//writer.println(d);
					//writer.println(g);
					//writer.println(k);
					//float num1=k;
					//float num2=g;	
					float e=(d*100)/(k+g-d);
					writer.println("Percentage Of Plagiarism from file"+ c + "is:"+ e);	
					c++;
				}
				/*else if(k==g)
				{
					writer.println("Percentage Of Plagiarism from file"+ c + "is:"+ 100.0);	
					c++;
				}*/
			}								
		}
		catch(SQLException e)
		{
		}
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
