package ru.netology.banklogin.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static DataHelper.VerificationCode getVerificationCode() {
        var codeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var code = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return new DataHelper.VerificationCode(code);
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var connection = getConn();
        runner.execute(connection, "DELETE FROM auth_codes");
        runner.execute(connection, "DELETE FROM card_transactions");
        runner.execute(connection, "DELETE FROM cards");
        runner.execute(connection, "DELETE FROM users");
    }

    @SneakyThrows
    public static void cleanAuthCodes() {
        var connection = getConn();
        runner.execute(connection, "DELETE FROM auth_codes");
    }



    @SneakyThrows
    public static DataHelper.BalanceCard getFirstCardBalance() {
        var codeSQL = ("SELECT balance_in_kopecks FROM cards WHERE `number` = '5559 0000 0000 0002'");
        var conn = getConn();
        var balance = runner.query(conn, codeSQL, new ScalarHandler<Integer>());
        return new DataHelper.BalanceCard(balance);
    }

    @SneakyThrows
    public static DataHelper.BalanceCard getSecondCardBalance() {
        var codeSQL = ("SELECT balance_in_kopecks FROM cards WHERE `number` = '5559 0000 0000 0008'");
        var conn = getConn();
        var balance = runner.query(conn, codeSQL, new ScalarHandler<Integer>());
        return new DataHelper.BalanceCard(balance);
    }

    @SneakyThrows
    public static void getLastTransaction(){
        var connection = getConn();
        runner.execute(connection, "SELECT source, target, amount_in_kopecks  FROM card_transactions ORDER BY created DESC LIMIT 1");
    }
}
