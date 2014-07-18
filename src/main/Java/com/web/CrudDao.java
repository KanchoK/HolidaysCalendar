package com.web;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by R500 on 17.7.2014 г..
 */
public class CrudDao {

    public static List<Holiday> getAllHolidays(){
        Connection conn = DBConnection.getConnection();
        List<Holiday> holidays = new ArrayList<Holiday>();
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rsFirst = null;
        ResultSet rsSecond = null;
        try {
            st = conn.createStatement();
            rsFirst = st.executeQuery("select holiday_id, beginDate, endDate, holidayStatus, employee_id from holidays");

            while (rsFirst.next()){
                Holiday holiday = new Holiday();
                pst = conn.prepareStatement("select employeeName from employees where employee_id = ?");
                pst.setInt(1, rsFirst.getInt("employee_id"));
                rsSecond = pst.executeQuery();
                if (rsSecond.next())
                    holiday.setEmployeeName(rsSecond.getString("employeeName"));
//                holiday.setEmployeeID();
                holiday.setHolidayID(rsFirst.getInt("holiday_id"));
                holiday.setBeginDate(rsFirst.getString("beginDate"));
                holiday.setEndDate(rsFirst.getString("endDate"));
                holiday.setHolidayStatus(rsFirst.getString("holidayStatus"));
                holidays.add(holiday);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try{
                if (st != null)
                    st.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try{
                if (pst != null)
                    pst.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try {
                if (rsFirst != null)
                    rsFirst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rsSecond != null)
                    rsSecond.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.closeConnection();
            conn = null;
            st = null;
            pst = null;
            rsFirst = null;
            rsSecond = null;
        }
        return holidays;
    }

    public static List<Holiday> getMyHolidays(int employeeID){
        Connection conn = DBConnection.getConnection();
        List<Holiday> holidays = new ArrayList<Holiday>();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = conn.prepareStatement("select holiday_id, beginDate, endDate, holidayStatus from holidays where employee_id = ?");
            preparedStatement.setInt(1, employeeID);
            rs = preparedStatement.executeQuery();

            while (rs.next()){
                Holiday holiday = new Holiday();
                holiday.setHolidayID(rs.getInt("holiday_id"));
                holiday.setBeginDate(rs.getString("beginDate"));
                holiday.setEndDate(rs.getString("endDate"));
                holiday.setHolidayStatus(rs.getString("holidayStatus"));
                holidays.add(holiday);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try{
                if (preparedStatement != null)
                    preparedStatement.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.closeConnection();
            conn = null;
            preparedStatement = null;
            rs = null;
        }
        return holidays;
    }

    public static int addHoliday(Holiday holiday){
        Connection conn = DBConnection.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        int key = -1;
        try {
            pst = conn.prepareStatement("insert into Holidays (beginDate, endDate, employee_id, holidayStatus) values (?,?,?,1)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, holiday.getBeginDate());
            pst.setString(2, holiday.getEndDate());
            pst.setInt(3, holiday.getEmployeeID());
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs.next())
                key = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try{
                if (pst != null)
                    pst.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.closeConnection();
        }
        return key;
    }

    public static void updateHolidayDates(Holiday holiday){
        Connection conn = DBConnection.getConnection();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("update holidays set beginDate = ?, endDate = ? where holiday_id = ?");
            pst.setString(1, holiday.getBeginDate());
            pst.setString(2, holiday.getEndDate());
            pst.setInt(3, holiday.getHolidayID());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try{
                if (pst != null)
                    pst.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            DBConnection.closeConnection();
        }
    }

    public static void updateHolidayStatus(Holiday holiday){
        Connection conn = DBConnection.getConnection();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("update holidays set holidayStatus = ? where holiday_id = ?");
            pst.setString(1, holiday.getHolidayStatus());
            pst.setInt(2, holiday.getHolidayID());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try{
                if (pst != null)
                    pst.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            DBConnection.closeConnection();
        }
    }

    public static void deleteHoliday(int holidayID){
        Connection conn = DBConnection.getConnection();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("delete from holidays where holiday_id = ?");
            pst.setInt(1, holidayID);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try{
                if (pst != null)
                    pst.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            DBConnection.closeConnection();
        }
    }
}
