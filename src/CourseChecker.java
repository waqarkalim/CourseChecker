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

public class CourseChecker {

    private static final String POST_URL = "http://studentservices.uwo.ca/secure/timetables/mastertt/ttindex.cfm";

    private static final String POST_PARAMS = "userName=Pankaj";

    private static String[] courseNames;

//    public CourseChecker() {
//
//
//        System.out.println("In here");
//        courseNames = new String[140];
//
//    }

    public static void main(String[] args) throws IOException {

        ArrayList<CourseSession> seshList = extract(sendPOST());
        print_all_seshes(seshList);
    }

    public String[] getCourseNames() {
        return courseNames;
    }

    public void extractCourseNames(Document doc) {

        Elements selectTag = doc.getElementsByTag("select");

        Elements namesList = (selectTag.first()).select("option");

        namesList.remove(0);

        int index = 0;
        for (Element el: namesList) {
            courseNames[index] = el.text();
            System.out.println(el.text());
            index++;
        }
        System.out.println(index);

    }

    private static void print_all_seshes(ArrayList<CourseSession> seshList) {
        for (CourseSession sesh: seshList) {
            if (sesh.getSection().equals("ClassHeader")) {
                System.out.println(sesh.getComponent());
            } else {
                System.out.println(sesh.toString());
            }
        }
    }

    private static ArrayList<CourseSession> extract(Document doc) {
        Elements h4Tag = doc.getElementsByTag("h4");
        Elements tdTag = doc.getElementsByTag("td");
        Elements tableTag = doc.getElementsByClass("table table-striped");
        ArrayList<CourseSession> seshList = new ArrayList<CourseSession>();
        String info;
        String section, Component, ClassNbr, Days, time, end_time, location, instructor, courseType, status;
        section = Component = ClassNbr = Days = time = end_time = location = instructor = courseType = status = "";
        int index = 0;


        // Weird stuff in this for loop just so the header would be in the seshList so it can be printed in order
        for (Element table: tableTag) {
            seshList.add(new CourseSession("ClassHeader", h4Tag.eq(index).text(), "", "", "", "", "", "", "", ""));
//            System.out.println(h4Tag.eq(index).text());
//            System.out.println(table);
            index++;
            int count = 0;
            for (Element el : table.getElementsByTag("td")) {
                info = el.text();
                count++;

                if (count == 1) {
                    section = info;
                } else if (count == 2) {
                    Component = info;
                } else if (count == 3) {
                    ClassNbr = info;
                } else if (count == 4) {
                    Days = info;
                } else if ((count == 5) || (count == 6) || (count == 7) || (count == 8) || (count == 9)) {
                    continue;
                } else if (count == 10) {
                    time = info;
                } else if (count == 11) {
                    end_time = info;
                } else if (count == 12) {
                    location = info;
                } else if (count == 13) {
                    instructor = info;
                } else if (count == 14) {
                    courseType = info;
                } else if (count == 15) {
                    status = info;
                }

                if (count == 15) {
                    CourseSession courseSesh = new CourseSession(section, Component, ClassNbr, Days, time, end_time, location, instructor, courseType, status);
                    seshList.add(courseSesh);
//                    System.out.println(courseSesh.toString());
                    count = 0;
                    section = Component = ClassNbr = Days = time = end_time = location = instructor = courseType = status = "";
                }
            }
        }
        return seshList;
    }

    public static Document sendPOST() throws IOException {
        System.out.println("1");
        List<String> days = new ArrayList<String>();
        System.out.println("2");
        days.add("m");
        days.add("tu");
        days.add("w");
        days.add("th");
        days.add("f");

        System.out.println("3");
        URL obj = new URL(POST_URL);
        System.out.println("4");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        System.out.println("5");
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

        System.out.println("6");
        // For POST only - START
//        con.setDoOutput(true);
//        OutputStream os = con.getOutputStream();
//        os.write(POST_PARAMS.getBytes());
//        os.flush();
//        os.close();
        // For POST only - END

        System.out.println("7");
        int responseCode = con.getResponseCode();
//        System.out.println("POST Response Code :: " + responseCode);

        System.out.println("8");
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
