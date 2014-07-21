package com.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by R500 on 21.7.2014 г..
 */
public class SignUpServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String fName = request.getParameter("fName");
        String lName = request.getParameter("lName");
        String email = request.getParameter("email");
        String pass = request.getParameter("password");
        String convertedPass = Utility.toSHA1(Utility.salt(pass).getBytes());
        
        Employee employee = new Employee();

        employee.setEmployeeName(fName + " " + lName);
        employee.setEmail(email);
        employee.setPassword(pass);
        employee.setAccessLevel(0);
        CrudDao.addEmployee(employee);

        try {
            response.sendRedirect("index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
