import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class TestJDBC extends Jdbc_Manage {

    public static void main(String[] args) {
        Jdbc_Manage.Connect();
        Jdbc_Manage jdbc_manage = new Jdbc_Manage();



//        String tablename = "managebus";
//        jdbc_manage.insertRecordManage(tablename, 123456,"nan","pop", LocalDateTime.of(2017, Month.FEBRUARY,3,6,30,40),
//                LocalDateTime.of(2017,Month.FEBRUARY,3,6,30,50),"ske",250.0);
////        Jdbc_Manage.removeRecord(tablename,"depart","nan");
//        jdbc_manage.showTable(tablename);
//    LocalDateTime a = LocdalDateTime.of(2017, Month.FEBRUARY, 3, 06, 30, 50);
        System.out.println(jdbc_manage.loadCompanyName());
//       double a = Double.parseDouble(result);
//       System.out.println(a);
    }


}
