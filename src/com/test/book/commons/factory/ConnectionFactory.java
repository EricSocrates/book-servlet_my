package com.test.book.commons.factory;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Properties;

public class ConnectionFactory {
    private static BasicDataSource bds = new BasicDataSource();

    static {
        try {
            InputStream is = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties");
            //InputStreamReader类是从字节流到字符流的桥接器:它使用指定的字符集读取字节并将它们解码为字符
            InputStreamReader reader = new InputStreamReader(is);
            Properties properties = new Properties();

            //加载资源，JDK1.6之后可加载字符流
            properties.load(reader);
            is.close();

            bds.setDriverClassName(properties.getProperty("o.driver"));
            bds.setUrl(properties.getProperty("o.url"));
            bds.setUsername(properties.getProperty("o.user"));
            bds.setPassword(properties.getProperty("o.pwd"));

            bds.setMaxActive(Integer.parseInt(properties.getProperty("o.maxActive")));
            bds.setMaxIdle(Integer.parseInt(properties.getProperty("o.maxIdle")));
            bds.setMaxWait(Long.parseLong(properties.getProperty("o.maxWait")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConnectionFactory() {
    }

    public static DataSource getDataSource() {
        return bds;
    }

    public static Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //测试方法
    public static void main(String[] args) {
        System.out.println(ConnectionFactory.getConnection());
    }
}
