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
 * Created by R500 on 22.7.2014 г..
 */
public class ChangePasswordServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        String oldPass = request.getParameter("oldPassword");
        HttpSession session = request.getSession();
        String oldPassFromDB = CrudDao.getPassword((Integer)session.getAttribute("employeeID"));
        String oldConvertedPass = Utility.toSHA1(Utility.salt(oldPass).getBytes());
        String newPass = request.getParameter("newPassword");
        String confirmNewPass = request.getParameter("confirmNewPassword");

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/changePassword.jsp");
        PrintWriter out = null;

        if (oldPass.trim().equals("") || newPass.trim().equals("") || confirmNewPass.trim().equals("")){
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
        } else if (!(newPass.equals(confirmNewPass))) {
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
                    "    color:#fff;\">The New password must be the same as the Confirm new password field.</p>");
            try {
                rd.include(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!(oldPassFromDB.equals(oldConvertedPass))) {
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
                    "    color:#fff;\">Wrong Old password.</p>");
            try {
                rd.include(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String newConvertedPass = Utility.toSHA1(Utility.salt(newPass).getBytes());
            CrudDao.updateEmployeePassword(newConvertedPass, (Integer)session.getAttribute("employeeID"));

            if (session.getAttribute("accountStatus").equals(0)){
                CrudDao.updateEmployeeAccountStatus((Integer)session.getAttribute("employeeID"));
                session.setAttribute("accountStatus", 1);
            }

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
        }
    }
}
