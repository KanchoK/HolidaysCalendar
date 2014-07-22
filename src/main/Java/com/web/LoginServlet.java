package com.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by R500 on 16.7.2014 г..
 */
public class LoginServlet extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response){

        String email = request.getParameter("email")  + "@novarto.com";
        String pass = request.getParameter("password");
        String convertedPass = Utility.toSHA1(Utility.salt(pass).getBytes());

        if (LoginCheck.validate(email, convertedPass)){
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            int access = EmployeeAttributes.getEmployeeAccessLevel(email, convertedPass);
            session.setAttribute("access", access);
            int employeeID = EmployeeAttributes.getEmployeeID(email, convertedPass);
            session.setAttribute("employeeID", employeeID);

            if (session.getAttribute("access").equals(1)){
                try {
                    response.sendRedirect("Admin.html");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    response.sendRedirect("NormalUser.html");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
            PrintWriter out = null;
            try {
                out = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.println("<p style=\"background:#3399cc; display:block; margin:0 auto;\n" +
                    "    margin-top:1%;\n" +
                    "    padding:10px;\n" +
                    "    text-align:center;\n" +
                    "    text-decoration:none;\n" +
                    "    color:#fff;\">Wrong email or password.</p>");
            try {
                rd.include(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
