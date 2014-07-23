package com.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by R500 on 21.7.2014 г..
 */
public class SignUpServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String fName = request.getParameter("fName");
        String lName = request.getParameter("lName");
        String email = request.getParameter("email") + "@novarto.com";
        String pass = request.getParameter("password");
        String confirmPass = request.getParameter("confirmPassword");
        String convertedPass = Utility.toSHA1(Utility.salt(pass).getBytes());

        if (fName.trim().equals("") || lName.trim().equals("") || email.trim().equals("") || pass.trim().equals("")){
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/signUp.html");
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
                    "    color:#fff;\">You must fill all fields.</p>");
            try {
                rd.include(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!(pass.equals(confirmPass))) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/signUp.html");
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
                    "    color:#fff;\">The Password must be the same as the Confirm Password field.</p>");
            try {
                rd.include(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!(email.matches("[a-zA-Z][a-zA-Z0-9_]*[\\.]?[a-zA-Z0-9_]*[a-zA-Z0-9]@novarto\\.com"))) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/signUp.html");
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
                    "    color:#fff;\">Your e-mail must start with a letter and can only have letters, numbers, unrepeatable underscores(_) and only one dot(.)</p>");
            try {
                rd.include(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Employee employee = new Employee();

            employee.setEmployeeName(fName + " " + lName);
            employee.setEmail(email);
            employee.setPassword(convertedPass);
            employee.setAccess_level(0);
            CrudDao.addEmployee(employee);

            try {
                response.sendRedirect("index.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
