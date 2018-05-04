import java.time.LocalDateTime;
import java.time.Month;

public class TestJDBC extends Jdbc_Manage {
    public static void main(String[] args) {
        Jdbc_Manage jdbc_manage = new Jdbc_Manage();
//        String tablename = "managebus";
//        jdbc_manage.insertRecord(tablename, 123456,"nan","pop", LocalDateTime.of(2017, Month.FEBRUARY,3,6,30,40),
//                LocalDateTime.of(2017,Month.FEBRUARY,3,6,30,50),"ske",250.0);
////        Jdbc_Manage.removeRecord(tablename,"depart","nan");
//        jdbc_manage.showTable(tablename);
//    LocalDateTime a = LocdalDateTime.of(2017, Month.FEBRUARY, 3, 06, 30, 50);
       String result = jdbc_manage.getTextFromSelectColumn("province_lat", "province_th", "province_name", "Bangkok");
       System.out.println(result.replace(" ",""));
//       double a = Double.parseDouble(result);
//       System.out.println(a);
    }
}
