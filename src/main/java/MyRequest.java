import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

class MyRequest {
    private static String userName = new TextHelper().getText("login.txt");
    private static String password = new TextHelper().getText("pwd.txt");
    private String viewState;
    private String eventValidation;
    private String urlStr = "http://service.triolan.com/Abonent/QuestionsAbon.aspx";

    MyRequest() {
        try {
            Document document = Jsoup.parse(getAuthenticatedResponse(urlStr, userName, password));
            Elements elements = document.getElementsByTag("input");
            viewState = elements.get(1).val();
            eventValidation = elements.get(4).val();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAuthenticatedResponse(final String urlStr,
                                            final String userName,
                                            final String password) throws IOException {
        StringBuilder response = new StringBuilder();
        Authenticator.setDefault(new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password.toCharArray());
            }
        });
        URL urlRequest = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) urlRequest.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        InputStream stream = conn.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String str;
        while ((str = in.readLine()) != null) {
            response.append(str).append("\n");
        }
        in.close();
        return response.toString();
    }

    String getWorkLoad(String employee) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String date = dateFormat.format(new Date());
        Connection connection = HttpConnection.connect(urlStr)
                .timeout(10000)
                .ignoreHttpErrors(true)
                .data("ctl00_ToolkitScriptManager1_HiddenField", ";;AjaxControlToolkit, Version=4.1.51116.0, Culture=neutral, PublicKeyToken=28f01b0e84b6d53e:ru-RU:fd384f95-1b49-47cf-9b47-2fa2a921a36a:f2c8e708:de1feab2:720a52bf:f9cec9bc:589eaa30:698129cf:7a92f56c:fcf0e993:fb9b4c57:ccb96cf9:a67c2700:8613aea7:3202a5a2:ab09e3fe:87104b7c:be6fb298")
                .data("__EVENTTARGET", "ctl00$cph_main$tbEmpl")
                .data("__VIEWSTATE", viewState)
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
        Document doc2 = null;
        if (response != null) {
            doc2 = Jsoup.parse(response.body());
        }
        Element table = null;
        if (doc2 != null) {
            table = doc2.getElementById("ctl00_cph_main_gvQuestions");
        }
        Elements rows = null;
        if (table != null) {
            rows = table.select("tr");
        }
        int zagruz = 0;
        if (rows != null) {
            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");
                if (cols.get(19).text().length() > 0) {
                    zagruz += Integer.parseInt(cols.get(19).text());
                }
            }
        }
        return zagruz / 60 + "ч. : " + zagruz % 60 + " мин.";
    }
}
