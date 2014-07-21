package com.web;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by R500 on 21.7.2014 г..
 */
public class EmployeeTableController extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("action") != null) {
            String action = (String) request.getParameter("action");
            List<Employee> employees = new ArrayList<Employee>();
            employees = CrudDao.getAllEmployees();
            Gson gson = new Gson();
            response.setContentType("application/json");

            if (action.equals("list")) {
                try {
                    JsonElement element = gson.toJsonTree(employees, new TypeToken<List<Employee>>() {}.getType());
                    JsonArray jsonArray = element.getAsJsonArray();
                    String listData = jsonArray.toString();

                    listData = "{\"Result\":\"OK\",\"Records\":" + listData + "}";
                    response.setContentType("application/json");
                    response.getWriter().print(listData);
                    System.out.println(listData);
                } catch (Exception ex) {
                    String error = "{\"Result\":\"ERROR\",\"Message\":" + ex.getStackTrace() + "}";
                    try {
                        response.getWriter().print(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            else if (action.equals("create")){
                Employee employee = new Employee();
                String convertedPass = Utility.toSHA1(Utility.salt(request.getParameter("password")).getBytes());

                employee.setEmployeeName(request.getParameter("employeeName"));
                employee.setEmail(request.getParameter("email"));
                employee.setPassword(convertedPass);
                employee.setAccessLevel(Integer.parseInt(request.getParameter("accessLevel")));

                try {
                    employee.setEmployeeID(CrudDao.addEmployee(employee));
                    employees.add(employee);
                    String json = gson.toJson(employee);
                    String listData = "{\"Result\":\"OK\",\"Record\":" + json + "}";
                    try {
                        response.getWriter().print(listData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }catch (Exception ex){
                    String error = "{\"Result\":\"ERROR\",\"Message\":"+ex.getStackTrace()+"}";
                    try {
                        response.getWriter().print(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            else if (action.equals("update")){
                Employee employee = new Employee();
                String convertedPass = Utility.toSHA1(Utility.salt(request.getParameter("password")).getBytes());
                employee.setEmployeeID(Integer.parseInt(request.getParameter("employeeID")));
                employee.setEmployeeName(request.getParameter("employeeName"));
                employee.setEmail(request.getParameter("email"));
                employee.setPassword(convertedPass);
                employee.setAccessLevel(Integer.parseInt(request.getParameter("accessLevel")));
                CrudDao.updateEmployee(employee);

                String listData="{\"Result\":\"OK\"}";
                try {
                    response.getWriter().print(listData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else if (action.equals("delete")){
                try{
                    if(request.getParameter("employeeID") != null){
                        String employeeID = (String)request.getParameter("employeeID");
                        CrudDao.deleteEmployee(Integer.parseInt(employeeID));
                        String listData="{\"Result\":\"OK\"}";
                        response.getWriter().print(listData);
                    }
                }catch(Exception ex){
                    String error="{\"Result\":\"ERROR\",\"Message\":"+ex.getStackTrace().toString()+"}";
                    try {
                        response.getWriter().print(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

