import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CourseNames {

    private static final String POST_URL = "http://studentservices.uwo.ca/secure/timetables/mastertt/ttindex.cfm";

    private static final String POST_PARAMS = "userName=Pankaj";


    static String[] courseNames = new String[140];
    public static void main(String[] args) throws IOException {
        extractCourseNames(sendPOST());

        System.out.println(courseNames.length);
    }

    public static void extractCourseNames(Document doc) {

//        Elements selectTag = doc.getElementsByTag("SELECT");

        Elements selectTag = doc.getElementsByClass("span6");

        Elements namesList = (selectTag.first()).select("option");


//        namesList.remove(0);

        int index = 0;
        for (Element el: namesList) {
//            System.out.println(el.getElementsByAttribute("value"));
            courseNames[index] = el.text();
            System.out.print("\"" + el.text() + "\", ");
            index++;
        }
        System.out.println(index);

        for (Element el: namesList) {
            System.out.print("\"" + el.attr("value") + "\", ");
        }

    }

    private static Document sendPOST() throws IOException {
        List<String> days = new ArrayList<String>();

        days.add("m");
        days.add("tu");
        days.add("w");
        days.add("th");
        days.add("f");
//                List.of("m", "tu", "w", "th", "f");
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("subject", "WRITING");
        con.setRequestProperty("Designation", "Any");
        con.setRequestProperty("catalognbr", "2111");
        con.setRequestProperty("CourseTime", "All");
        con.setRequestProperty("Component", "All");
        con.setRequestProperty("time", "");
        con.setRequestProperty("end_time", "");
        con.setRequestProperty("Component", "All");
        con.setRequestProperty("days", days.toString());
        con.setRequestProperty("Campus", "Any");
        con.setRequestProperty("command", "search");

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("Results <br><br><br>")) {
                    System.out.println(inputLine.replace("<br>", "").replace("   ", " "));
                    System.out.println("______________________________________");
                }
                response.append(inputLine);
            }
            in.close();

            response.trimToSize();

            String htmlString = response.toString();

            Document doc = Jsoup.parse(htmlString);


            return doc;
        } else {
            System.out.println("POST request not worked");
            return null;
        }
    }

}
