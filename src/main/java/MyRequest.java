import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyRequest {
    private static String viewstate = new TextHelper().getText("viewstate.txt");
    private static String eventValidation = new TextHelper().getText("eventValidation.txt");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static String date = dateFormat.format(new Date());
    private static String userName = new TextHelper().getText("login.txt");
    private static String password = new TextHelper().getText("pwd.txt");

    public String getWorkLoad(String employee) {
        Authenticator.setDefault(new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password.toCharArray());
            }
        });
        Connection connection = HttpConnection.connect("http://service.triolan.com/Abonent/QuestionsAbon.aspx")
                .timeout(10000)
                .ignoreHttpErrors(true)
                .data("ctl00_ToolkitScriptManager1_HiddenField", ";;AjaxControlToolkit, Version=4.1.51116.0, Culture=neutral, PublicKeyToken=28f01b0e84b6d53e:ru-RU:fd384f95-1b49-47cf-9b47-2fa2a921a36a:f2c8e708:de1feab2:720a52bf:f9cec9bc:589eaa30:698129cf:7a92f56c:fcf0e993:fb9b4c57:ccb96cf9:a67c2700:8613aea7:3202a5a2:ab09e3fe:87104b7c:be6fb298")
                .data("__EVENTTARGET", "ctl00$cph_main$tbEmpl")
                .data("__VIEWSTATE", viewstate)
                .data("__VIEWSTATEGENERATOR", "5AC7BC9A")
                .data("__VIEWSTATEENCRYPTED", "")
                .data("__EVENTVALIDATION", eventValidation)
                .data("ctl00$cph_main$tbEmpl", employee)
                .data("ctl00$cph_main$ddlDateType", "1")
                .data("ctl00$cph_main$tbDateFrom", date)
                .data("ctl00$cph_main$tbDateTo", date)
                .data("ctl00$cph_main$ddl_source", "0")
                .data("ctl00$cph_main$ddl_provider", "0")
                .data("ctl00$cph_main$ddl_qTypes", "1")
                .data("hiddenInputToUpdateATBuffer_CommonToolkitScripts", "1")
                .data("ctl00$cph_main$ddlCountOnPage", "100")
                .data("ctl00$cph_main$btnSearch", "Поиск")
                .method(Connection.Method.POST)
                .followRedirects(true);

        Connection.Response response = null;
        try {
            response = connection.execute();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("тут ошибка");
        }
        Document doc2 = Jsoup.parse(response.body());
        Element table = doc2.getElementById("ctl00_cph_main_gvQuestions");
        Elements rows = table.select("tr");
        int zagruz = 0;
        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");
            if (cols.get(19).text().length() > 0){
                zagruz += Integer.parseInt(cols.get(19).text());
            }
        }
        return zagruz / 60 + "ч. : " + zagruz % 60 + " мин.";
    }
}
