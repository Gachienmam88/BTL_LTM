package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/btl_ltm"; // Thay đổi tên cơ sở dữ liệu
    private static final String USERNAME = "root"; // Tên người dùng
    private static final String PASSWORD = ""; // Mật khẩu

    private static Connection connection;

    // Phương thức để lấy kết nối
    public  Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}