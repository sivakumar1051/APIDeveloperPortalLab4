package com.siva.apidevportal.Servlets;

import com.siva.apidevportal.Repository.ApiRepository;
import com.siva.apidevportal.Models.ApiKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

public class ApiKeyServlet extends HttpServlet {

    private final ApiRepository apiRepository = new ApiRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = (int) session.getAttribute("user_id");
        List<ApiKeys> apiKeys = apiRepository.getApiKeysForUser(userId);

        
        if (request.getParameter("manage") != null) {
            // redirecting to api management page
            displayApiKeyManagement(out, apiKeys);
        } else {
            // redirecting to dashboard page
            displayAvailableApiKeys(out, apiKeys);
        }
    }

    // displaying details in management page
    private void displayApiKeyManagement(PrintWriter out, List<ApiKeys> apiKeys) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>API Key Management</title>");
        out.println("<link rel='stylesheet' type='text/css' href='DashboardStyle.css'>");
        out.println("</head>");
        
        out.println("<body>");
        out.println("<h2>API Key Management</h2>");
        out.println("<div class='center'>");
        out.println("<form action='apiKeys' method='GET'>");
        out.println("<button type='submit' name='action' value='generate'>Regenerate API Key</button>");
        out.println("</form>");
        out.println("</div>");

        if (apiKeys.isEmpty()) {
            out.println("<p>No API keys found.</p>");
        } else {
            out.println("<table border='1'><tr><th>API Key</th><th>Status</th><th>Created At</th><th>Action</th></tr>");
            for (ApiKeys apiKey : apiKeys) {
                out.println("<tr>");
                out.println("<td>" + apiKey.getApi_Key() + "</td>");
                out.println("<td>" + apiKey.getStatus() + "</td>");
                out.println("<td>" + apiKey.getCreatedAt() + "</td>"); // Assuming getCreatedAt() exists
                out.println("<td><form action='apiKeys' method='POST'>");
                out.println("<input type='hidden' name='apiKey' value='" + apiKey.getApi_Key() + "'>");
                out.println("<button type='submit' name='action' value='deactivate'>Deactivate</button>");
                out.println("</form></td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        

        out.println("</body></html>");
    }

    // displaying details in dashboard page
    private void displayAvailableApiKeys(PrintWriter out, List<ApiKeys> apiKeys) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Available API Keys</title>");
        out.println("<link rel='stylesheet' type='text/css' href='DashboardStyle.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Available API Keys</h2>");

        out.println("<div class='center'>");
        out.println("<form action='apiKeys' method='POST'>");
        out.println("<button type='submit' name='action' value='generate'>Generate New API Key</button>");
        out.println("</form>");
        out.println("</div>");
        out.println("<div class='center'>");
        out.println("<form action='apiKeys' method='GET'>");
        out.println("<button type='submit' name='manage' value='true'>Manage APIs</button>");
        out.println("</form>");
        out.println("</div>");

        if (apiKeys.isEmpty()) {
            out.println("<p>No API keys found.</p>");
        } else {
            out.println("<table border='1'><tr><th>API Key</th><th>Status</th><th>Action</th></tr>");
            for (ApiKeys apiKey : apiKeys) {
                out.println("<tr>");
                out.println("<td>" + apiKey.getApi_Key() + "</td>");
                out.println("<td>" + apiKey.getStatus() + "</td>");
                out.println("<td><form action='apiKeys' method='POST'>");
                out.println("<input type='hidden' name='apiKey' value='" + apiKey.getApi_Key() + "'>");
                out.println("<button type='submit' name='action' value='deactivate'>Deactivate</button>");
                out.println("</form></td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        

        out.println("</body></html>");
    }

    // this method handles generate and deactivate post requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessionObj = request.getSession(false);
        if (sessionObj == null || sessionObj.getAttribute("user_id") == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if ("generate".equals(action)) {
            int userId = (int) sessionObj.getAttribute("user_id");
            String newApiKey = generateApiKey();
            Boolean isTrue = apiRepository.addApiKey(userId, newApiKey);
            if (isTrue) {
                response.sendRedirect("apiKeys");
            } else {
                response.getWriter().println("Error generating API key.");
            }
        } else if ("deactivate".equals(action)) {
            String apiKey = request.getParameter("apiKey");
            if (apiKey != null && !apiKey.isEmpty()) {
                Boolean isTrue = apiRepository.deactivateApiKey(apiKey);
                if (isTrue) response.sendRedirect("apiKeys?manage=true"); 
                else response.getWriter().println("Error deactivating API key.");
            } else {
                response.getWriter().println("Invalid API key provided.");
            }
        }
    }

    // API key generation
    private String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
