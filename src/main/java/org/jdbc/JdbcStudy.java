package org.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
/*
    SQL数据类型	Java数据类型
    BIT, BOOL	boolean
    INTEGER	    int
    BIGINT	    long
    REAL	    float
    FLOAT,      DOUBLE	double
    CHAR,       VARCHAR	String
    DECIMAL	    BigDecimal
    DATE	    java.sql.Date, LocalDate
    TIME	    java.sql.Time, LocalTime
 */
//这一部分只是简单介绍，详细看sql部分
public class JdbcStudy {

    //查询数据库步骤
        /*
        1. 创建数据库连接
        2. 使用Connection提供的createStatement()创建Statement对象,用于执行一个查询
        3. 执行Statement的executeQuery方法，传入sql语句，执行查询，使用ResultSet引用结果集
        4.反复用用ResultSet的next方法，获取结果集的下一行数据
         */
    @Test
    public void test1() throws SQLException {
        String JDBC_URL = "jdbc:mysql://localhost:3306/learnjdbc";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "123456";
        String sql = "SELECT * FROM students WHERE gender=1";
        //获取连接
        try(Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){
            try(Statement state = conn.createStatement()){
                try(ResultSet set = state.executeQuery(sql)){
                    System.out.println("查询结果1：");
                    while(set.next()){//判断下一行有无数据，如果有自动移动到下一行
                        long id = set.getLong(1);//索引从1开始
                        String name = set.getString(2);
                        long grade = set.getLong(3);
                        int gender = set.getInt(4);
                        System.out.println("id: " + id + " name: " + name + " gender: " + gender + " grade: " + grade);
                    }
                }
            }
        }
    }
    //什么是sql注入？
    /*
    这是我们写的
    "SELECT * FROM user WHERE login='" + name + "' AND pass='" + pass + "'"
    通过精心构造字符串可以使这个sql语句脱离本身的设计意图
    name = "bob' OR pass=", pass = " OR pass='"
    得到的结果就是
    SELECT * FROM user WHERE login='bob' OR pass=' AND pass=' OR pass=''
    完全和原来不是一个东西，为了避免sql注入攻击就需要转义，但这样很麻烦，所以应该使用PreparedStatement
    使用PreparedStatement可以完全避免SQL注入的问题，因为PreparedStatement始终使用?作为占位符，并且把数据连同SQL本身传给数据库，这样可以保证每次传给数据库的SQL语句是相同的
     */
    @Test
    public void test2(){
        String JDBC_URL = "jdbc:mysql://localhost:3306/learnjdbc";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "123456";
        String gender_query = "1";
        String grade_query = "3";
        String sql = "SELECT * FROM students WHERE gender=? AND grade=?";
        //获取连接
        try(Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)){
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                //设置占用符处的值
                ps.setObject(1, gender_query);
                ps.setObject(2, grade_query);
                try(ResultSet set = ps.executeQuery()){
                    System.out.println("查询结果2：");
                    while(set.next()){
                        long id = set.getLong(1);//索引从1开始
                        String name = set.getString(2);
                        long grade = set.getLong(3);
                        int gender = set.getInt(4);
                        System.out.println("id: " + id + " name: " + name + " gender: " + gender + " grade: " + grade);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //事务
    /*
    数据库事务（Transaction）是由若干个SQL语句构成的一个操作序列，有点类似于Java的synchronized同步。
    数据库系统保证在一个事务中的所有SQL要么全部执行成功，要么全部不执行，即数据库事务具有ACID特性：
        Atomicity：原子性
        Consistency：一致性
        Isolation：隔离性
        Durability：持久性
     */
    @Test
    public void test3(){
/*
jdbc里实现，只要关闭自动提交即可
如果事务执行失败了则需要rollback回滚事务,然后在finally里将Connection对象的状态恢复初始值
        Connection conn = openConnection();
        try {
            // 关闭自动提交:
            conn.setAutoCommit(false);
            // 执行多条SQL语句:
            insert(); update(); delete();
            // 提交事务:
            conn.commit();
        } catch (SQLException e) {
            // 回滚事务:
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
 */
    }


    //数据库连接池,最常用的是HikariCP
    @Test
    public void test4(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/learnjdbc");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("connectionTimeout", "1000"); // 连接超时：1秒
        config.addDataSourceProperty("idleTimeout", "60000"); // 空闲超时：60秒
        config.addDataSourceProperty("maximumPoolSize", "10"); // 最大连接数：10
        //创建DataSource实例，这个实例就是连接池
        DataSource ds = new HikariDataSource(config);
        //后面和前面的用法一样
        try(Connection conn = ds.getConnection()){//它的close也只是将连接放到了连接池里

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
