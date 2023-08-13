package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/dbchat?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false";
        String username = "root";
        String password = "w112112808";

        try {
            // 加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");

            // 建立连接
            Connection connection = DriverManager.getConnection(url, username, password);

            // 如果连接成功，打印成功消息
            if (connection != null) {
                System.out.println("成功连接到数据库！");
                // 可以进行一些数据库操作...

                // 关闭连接
                connection.close();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("找不到MySQL驱动程序！");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("连接数据库时出现异常！");
            e.printStackTrace();
        }
    }
}