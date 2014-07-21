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
 * Created by R500 on 17.7.2014 г..
 */
public class HolidayTableController extends HttpServlet{

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("action") != null) {
            String action = (String) request.getParameter("action");
            HttpSession session = request.getSession();
            List<Holiday> holidays = new ArrayList<Holiday>();
            if (session.getAttribute("access").equals(1)) {
                holidays = CrudDao.getAllHolidays();
            } else {
                holidays = CrudDao.getMyHolidays((Integer)session.getAttribute("employeeID"));
            }
            Gson gson = new Gson();
            response.setContentType("application/json");

            if (action.equals("list")) {
                try {
                    JsonElement element = gson.toJsonTree(holidays, new TypeToken<List<Holiday>>() {}.getType());
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
                Holiday holiday = new Holiday();

                holiday.setBeginDate(request.getParameter("beginDate"));
                holiday.setEndDate(request.getParameter("endDate"));
                holiday.setEmployeeID((Integer)(session.getAttribute("employeeID")));
                holiday.setEmployeeName((String)(session.getAttribute("employeeName")));

                try {
                    holiday.setHolidayID(CrudDao.addHoliday(holiday));
                    holidays.add(holiday);
                    String json = gson.toJson(holiday);
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
                Holiday holiday = new Holiday();
                holiday.setHolidayID(Integer.parseInt(request.getParameter("holidayID")));
                if (session.getAttribute("access").equals(1)){
                    holiday.setHolidayStatus(request.getParameter("holidayStatus"));
                    CrudDao.updateHolidayStatus(holiday);
                } else {
                    holiday.setBeginDate(request.getParameter("beginDate"));
                    holiday.setEndDate(request.getParameter("endDate"));

                    CrudDao.updateHolidayDates(holiday);
                }

                String listData="{\"Result\":\"OK\"}";
                try {
                    response.getWriter().print(listData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else if (action.equals("delete")){
                try{
                    if(request.getParameter("holidayID") != null){
                        String holidayID = (String)request.getParameter("holidayID");
                        CrudDao.deleteHoliday(Integer.parseInt(holidayID));
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
