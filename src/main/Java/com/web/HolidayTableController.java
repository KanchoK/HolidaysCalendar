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

            Gson gson = new Gson();
            response.setContentType("application/json");

            if (action.equals("list")) {
                try {
                    int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
                    int numRecordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
                    String sort = request.getParameter("jtSorting");
                    int holidayCount = -1;

                    if (session.getAttribute("access").equals(1)) {
                        holidays = CrudDao.getAllHolidays(startPageIndex, numRecordsPerPage, sort);
                        holidayCount = CrudDao.getHolidayCount();
                    } else {
                        holidays = CrudDao.getMyHolidays((Integer)session.getAttribute("employeeID"), startPageIndex, numRecordsPerPage, sort);
                        holidayCount = CrudDao.getHolidayCount((Integer)session.getAttribute("employeeID"));
                    }
                    JsonElement element = gson.toJsonTree(holidays, new TypeToken<List<Holiday>>() {}.getType());
                    JsonArray jsonArray = element.getAsJsonArray();
                    String listData = jsonArray.toString();

                    listData = "{\"Result\":\"OK\",\"Records\":" + listData + ",\"TotalRecordCount\":" + holidayCount + "}";
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

                //employeeName not necessary in Holiday's table
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
