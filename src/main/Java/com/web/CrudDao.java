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

    public static List<Employee> getAllEmployees(){
        Connection conn = DBConnection.getConnection();
        List<Employee> employees = new ArrayList<Employee>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select * from employees");
            while (rs.next()){
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getInt("employee_id"));
                employee.setEmployeeName(rs.getString("employeeName"));
                employee.setEmail(rs.getString("email"));
                employee.setPassword(rs.getString("password"));
                employee.setAccessLevel(rs.getInt("access_level"));
                employees.add(employee);
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
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.closeConnection();
            conn = null;
            st = null;
            rs = null;
        }
        return employees;
    }

    public static String getPassword(int employeeID){
        String password = "";
        Connection conn = DBConnection.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = conn.prepareStatement("select password from employees where employee_id = ?");
            pst.setInt(1, employeeID);
            rs = pst.executeQuery();
            rs.next();
            password = rs.getString("password");
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
        return password;
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

    public static int addEmployee(Employee employee){
        Connection conn = DBConnection.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        int key = -1;
        try {
            pst = conn.prepareStatement("insert into Employees (employeeName, email, password, access_level) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, employee.getEmployeeName());
            pst.setString(2, employee.getEmail());
            pst.setString(3, employee.getPassword());
            pst.setInt(4, employee.getAccessLevel());
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

    public static void updateEmployee(Employee employee){
        Connection conn = DBConnection.getConnection();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("update employees set employeeName = ?, email = ?, password = ?, access_level = ? where employee_id = ?");
            pst.setString(1, employee.getEmployeeName());
            pst.setString(2, employee.getEmail());
            pst.setString(3, employee.getPassword());
            pst.setInt(4, employee.getAccessLevel());
            pst.setInt(5, employee.getEmployeeID());
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

    public static void updateEmployeePassword(String newPass, int employeeID){
        Connection conn = DBConnection.getConnection();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("update employees set password = ? where employee_id = ?");
            pst.setString(1, newPass);
            pst.setInt(2, employeeID);
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

    public static void cascadeDeleteEmployee(int employeeID){
        Connection conn = DBConnection.getConnection();
        PreparedStatement pstFirst = null;
        PreparedStatement pstSecond = null;
        try {
            pstFirst = conn.prepareStatement("delete from holidays where employee_id = ?");
            pstFirst.setInt(1, employeeID);
            pstFirst.executeUpdate();

            pstSecond = conn.prepareStatement("delete from employees where employee_id = ?");
            pstSecond.setInt(1, employeeID);
            pstSecond.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try{
                if (pstFirst != null)
                    pstFirst.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try{
                if (pstSecond != null)
                    pstSecond.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            DBConnection.closeConnection();
        }
    }
}
