package com.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by R500 on 16.7.2014 г..
 */
public class AccessLevel {
    public static int getAccessLevel(String name, String pass){
        int accessLevel = 2;

        Connection conn = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = conn.prepareStatement("select access_level from employees where username = ? and password = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pass);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            accessLevel = resultSet.getInt("access_level");
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
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnection.closeConnection();
        }

        return accessLevel;
    }
}
