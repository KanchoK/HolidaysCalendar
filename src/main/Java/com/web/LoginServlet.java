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

        String name = request.getParameter("username");
        String pass = request.getParameter("password");

        if (LoginCheck.validate(name, pass)){
            HttpSession session = request.getSession();
            session.setAttribute("username", name);
            int access = EmployeeAttributes.getEmployeeAccessLevel(name, pass);
            session.setAttribute("access", access);
            int employeeID = EmployeeAttributes.getEmployeeID(name, pass);
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
            out.println("<font color=red>Wrong username or password.</font>");
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
