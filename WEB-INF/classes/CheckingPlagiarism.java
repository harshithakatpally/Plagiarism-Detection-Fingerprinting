import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.*;
import java.lang.*;

//Dispatches the control to file to file UI or local database UI depending on user's selection
public class CheckingPlagiarism extends HttpServlet
{
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pw = response.getWriter();
		String n = request.getParameter("file");
		if(n.equals("Local database"))
		{
			RequestDispatcher rs = request.getRequestDispatcher("/localdb.html");
      rs.forward(request, response);
		}
		else if(n.equals("File to File"))
		{
			RequestDispatcher rd = request.getRequestDispatcher("/filetofile.html");
    	rd.forward(request, response);
		}
	}
}
