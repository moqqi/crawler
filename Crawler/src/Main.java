import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by iMoqii on 16-02-23.
 */
public class Main {
    public static DB db = new DB();

    public static void main(String[] args) throws SQLException, IOException{
        db.runSql2("Truncate Record;");
        processPage("http://www.bookstore.dal.ca/Course/campus");
    }
    public static void processPage(String URL) throws SQLException, IOException {
        //check if the URL is in DB.
        String sql = "select * from Record where URL = '"+URL+"'";
        ResultSet rs = db.runSql(sql);
        if (rs.next()){

        }else{
            //store url to DB to avoid multiple parsing.
            sql = "INSERT INTO  `Crawler`.`Record` " + "(`URL`) VALUES " + "(?);";
            PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, URL);
            stmt.execute();

            //get useful information
            Document doc = Jsoup.connect(URL).get();
            //put wanted parameter for contains for term wanted within the search e.g "books".
            if (doc.text().contains("")){
                System.out.println(URL);
            }

            //get all links and recursively call the processPage method
            Elements questions = doc.select("a[href]");
            for (Element link: questions){
                //put wanted parameter in contains for specific links wanted e.g "dal.ca"
                if(link.attr("href").contains(""))
                    processPage(link.attr("abs:href"));

            }


        }
    }
}
