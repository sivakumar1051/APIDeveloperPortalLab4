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

	//creating object of apiRepository.
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

        // filtering thr rquest based on onclick 
        if (request.getParameter("manage") != null) {
            displayApiKeyManagement(out, apiKeys);
        } else {
            displayAvailableApiKeys(out, apiKeys);
        }
    }

    // this method displays the apikey details in management page
    private void displayApiKeyManagement(PrintWriter out, List<ApiKeys> apiKeys) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>API Key Management</title>");
        out.println("<link rel='stylesheet' type='text/css' href='DashboardStyle.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>API Key Management</h2>");
        out.println("<div class='center'>");
        out.println("<form action='apiKeys' method='POST'>");
        out.println("<input type='hidden' name='manage' value='true'/>"); // Added hidden input to keep management context
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
                out.println("<td>" + apiKey.getCreatedAt() + "</td>");
                out.println("<td><form action='apiKeys' method='POST'>");
                out.println("<input type='hidden' name='manage' value='true'/>"); // Added hidden input to keep management context
                out.println("<input type='hidden' name='apiKey' value='" + apiKey.getApi_Key() + "'>");
                out.println("<button type='submit' name='action' value='deactivate'>Deactivate</button>");
                out.println("</form></td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }
        out.println("</body></html>");
    }

    // Method to display the the details of apikeys in dashboard  page
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

    // Handling POST requests for generating and deactivating API keys
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessionObj = request.getSession(false);
        if (sessionObj == null || sessionObj.getAttribute("user_id") == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        int userId = (int) sessionObj.getAttribute("user_id");

        if ("generate".equals(action)) {
            // generating the new apikey
            String newApiKey = generateApiKey();
            apiRepository.addApiKey(userId, newApiKey);

            // filtering the requests from management page
            if (request.getParameter("manage") != null) {
                
                response.sendRedirect("apiKeys?manage=true");
            } else {
                // Otherwise, redirect to the dashboard
                response.sendRedirect("apiKeys");
            }
        } else if ("deactivate".equals(action)) {
            // updating the status of apikey
            String apiKey = request.getParameter("apiKey");
            if (apiKey != null && !apiKey.isEmpty()) {
                apiRepository.deactivateApiKey(apiKey);
                String referer = request.getHeader("Referer");
                response.sendRedirect(referer != null ? referer : "apiKeys");
            }
        }
    }

    // API key generation using random
    private String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
