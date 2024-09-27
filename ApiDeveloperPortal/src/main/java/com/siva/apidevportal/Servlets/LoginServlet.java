package com.siva.apidevportal.Servlets;

import com.siva.apidevportal.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final UserRepository userRepository = new UserRepository();
    private String inputUserName, inputPassword;

    // GET: Display login form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("LoginForm.html").forward(request, response);
    }

    // POST: Handle login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        inputUserName = request.getParameter("UserName");
        inputPassword = request.getParameter("Password");

        StringBuilder loginResponse = new StringBuilder();
        loginResponse.append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")
                .append("<head><meta charset=\"UTF-8\"><title>Login Status</title></head>")
                .append("<body>")
                .append("<h2>Login Status</h2>");

        try {
            // Check user authentication via UserRepository
            boolean isAuthenticated = userRepository.authenticateUser(inputUserName, inputPassword);

            if (isAuthenticated) {
                // Get user ID
                int userId = userRepository.getUserId(inputUserName);

                // Store the user data in the session
                HttpSession session = request.getSession();
                session.setAttribute("username", inputUserName);
                session.setAttribute("user_id", userId);

                loginResponse.append("<p>Login successful! Redirecting to Dashboard...</p>");
                response.sendRedirect("apiKeys");
            } else {
                loginResponse.append("<p>Login failed. Please try again.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginResponse.append("<p>Cannot login because of: ").append(e.getMessage()).append("</p>");
        }

        loginResponse.append("<a href=\"Dashboard.html\">Click here to navigate to dashboard...</a>")
                .append("</body>")
                .append("</html>");

        // Sending the HTML response
        response.getWriter().write(loginResponse.toString());
    }
}
