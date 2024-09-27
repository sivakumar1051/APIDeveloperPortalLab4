package com.siva.apidevportal.Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.siva.apidevportal.Repository.UserRepository;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private UserRepository userRepository;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
        super();
        userRepository = new UserRepository(); // Initialize UserRepository
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("RegisterForm.html").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
	    response.setCharacterEncoding("UTF-8");
	    
		String inputUserName = request.getParameter("UserName");
		String inputPassword = request.getParameter("Password");
		String inputEmail = request.getParameter("email");
		
		StringBuilder postResponse = new StringBuilder();
		postResponse.append("<!DOCTYPE html>")
                    .append("<html lang=\"en\">")
                    .append("<head><meta charset=\"UTF-8\"><title>Registration Status</title></head>")
                    .append("<body>")
                    .append("<h2>Registration Status</h2>");
		
		boolean isRegistered = userRepository.registerUser(inputUserName, inputPassword, inputEmail);
		
		if (isRegistered) {
			postResponse.append("<p>Registration successful! You can now log in.</p>");
		} else {
			postResponse.append("<p>Registration failed. Please try again.</p>");
		}

		postResponse.append("<a href=\"LoginForm.html\">Click here to login...</a>")
         .append("</body>")
         .append("</html>");

        // Sending the HTML response
        response.getWriter().write(postResponse.toString());
    }
}
