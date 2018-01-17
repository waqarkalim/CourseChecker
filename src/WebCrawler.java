import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class WebCrawler {

    private static final String POST_URL = "http://studentservices.uwo.ca/secure/timetables/mastertt/ttindex.cfm";

    private static final String POST_PARAMS = "userName=Pankaj";

    private static String courseInput, courseCode, courseSuffix, courseComponent;

    public static final String ACCOUNT_SID = "AC072b5a8bb4f0dd7a39f256b393928fcb";
    public static final String AUTH_TOKEN = "67aa3886bd1ddd0ca58780b6f3ddf69f";

    String[] courseNames = {"Actuarial Science", "American Studies", "Anatomy and Cell Biology", "Anthropology", "Applied Mathematics", "Arabic",
            "Arts and Humanities", "Astronomy", "Biblical Studies", "Biochemistry", "Biology", "Biostatistics", "Broadcasting-Radio Fanshawe",
            "Broadcasting-TV Fanshawe", "Broadcasting-TV-Field Fanshawe", "Business Administration", "Calculus", "Centre for Global Studies",
            "Chem & Biochem Engineering", "Chemical Biology", "Chemistry", "Childhood & Social Institutns", "Chinese", "Church History", "Church Law",
            "Church Music", "Civil & Envrnmntl Engineering", "Classical Studies", "Combined Program Enrollment", "Communication Sci & Disorders",
            "Comparative Lit & Culture", "Computer Science", "Dance", "Digital Communication", "Digital Fanshawe", "Digital Humanities",
            "Dimensions of Leadership", "Disability Studies", "Earth Sciences", "Economics", "Education English Language Cen", "Elect & Computer Engineering",
            "Engineering Science", "English", "Environmental Science", "Epidemiology", "Epidemiology & Biostatistics", "Family Studies",
            "Field Education", "Film - Fanshawe", "Film Studies", "Financial Modelling", "First Nations Studies", "Foods and Nutrition", "French",
            "Geography", "Geology", "Geophysics", "German", "Greek", "Green Process Engineering", "Health Sciences", "Hebrew", "Hindi", "Historical Theology",
            "History", "History of Science", "Homiletics", "Human Ecology", "Integrated Science", "Intercultural Communications", "Interdisciplinary Studies",
            "International Relations", "Italian", "Japanese", "Jewish Studies", "Journalism-Broadcasting Fanshw", "Kinesiology", "Latin", "Law", "Linguistics",
            "Liturgical Studies", "Liturgics", "Management & Organizational St", "Marketing - Fanshawe", "Mathematics", "Mech & Materials Engineering",
            "Mechatronic Systems Engineerin", "Media, Information &Technocult", "Medical Biophysics", "Medical Health Informatics", "Medical Science",
            "Medieval Studies", "Microbiology & Immunology", "Moral Theology", "Multimed Dsgn & Prod Fanshawe", "Music", "Neuroscience", "Nursing",
            "Pastoral Theology", "Pathology", "Persian", "Pharmacology", "Philosophical Studies", "Philosophy", "Physics", "Physiology",
            "Physiology and Pharmacology", "Political Science", "Portuguese", "Psychology", "Rehabilitation Sciences", "Religious Education",
            "Religious Studies", "Sacramental Theology", "Scholars' Elective", "Science", "Social Justice & Peace Studies", "Social Work",
            "Sociology", "Software Engineering", "Spanish", "Speech", "Spiritual Theology", "Statistical Sciences", "Supervised Pastoral Education",
            "Systematic Theology", "Thanatology", "Theatre Studies", "Theological Ethics", "Theological Studies", "Thesis", "Transitional Justice",
            "Visual Arts History", "Visual Arts Studio", "Western Thought & Civilization", "Women's Studies", "World Literatures and Cultures",
            "Writing"};

    static String[] courseNameValue = {"ACTURSCI", "AMERICAN", "ANATCELL", "ANTHRO", "APPLMATH", "ARABIC", "ARTHUM", "ASTRONOM", "BIBLSTUD", "BIOCHEM",
            "BIOLOGY", "BIOSTATS", "MTP-RADO", "MTP-TVSN", "MTP-FLDP", "BUSINESS", "CALCULUS", "CGS", "CBE", "CHEMBIO", "CHEM", "CSI", "CHINESE",
            "CHURCH", "CHURLAW", "CHURMUSI", "CEE", "CLASSICS", "CMBPROG", "COMMSCI", "COMPLIT", "COMPSCI", "DANCE", "DIGICOMM", "MTP-DIGL",
            "DIGIHUM", "DOL", "DISABST", "EARTHSCI", "ECONOMIC", "EELC", "ECE", "ENGSCI", "ENGLISH", "ENVIRSCI", "EPID", "EPIDEMIO", "FAMLYSTU",
            "FLDEDUC", "MTP-FILM", "FILM", "FINMOD", "FRSTNATN", "FOODNUTR", "FRENCH", "GEOGRAPH", "GEOLOGY", "GEOPHYS", "GERMAN", "GREEK", "GPE",
            "HEALTSCI", "HEBREW", "HINDI", "HISTTHEO", "HISTORY", "HISTSCI", "HOMILET", "HUMANECO", "INTEGSCI", "ICC", "INTERDIS", "INTREL",
            "ITALIAN", "JAPANESE", "JEWISH", "MTP-BRJR", "KINESIOL", "LATIN", "LAW", "LINGUIST", "LITURST", "LITURGIC", "MOS", "MTP-MKTG", "MATH",
            "MME", "MSE", "MIT", "MEDBIO", "MEDHINFO", "MEDSCIEN", "MEDIEVAL", "MICROIMM", "MORALTHE", "MTP-MMED", "MUSIC", "NEURO", "NURSING",
            "PASTTHEO", "PATHOL", "PERSIAN", "PHARM", "PHILST", "PHILOSOP", "PHYSICS", "PHYSIOL", "PHYSPHRM", "POLISCI", "PORTUGSE", "PSYCHOL",
            "REHABSCI", "RELEDUC", "RELSTUD", "SACRTHEO", "SCHOLARS", "SCIENCE", "SOCLJUST", "SOCWORK", "SOCIOLOG", "SE", "SPANISH", "SPEECH",
            "SPIRTHEO", "STATS", "SUPPAST", "SYSTHEO", "THANAT", "THEATRE", "THEOETH", "THEOLST", "THESIS", "TJ", "VAHISTRY", "VASTUDIO", "WTC",
            "WOMENST", "WORLDLIT", "WRITING"};

    static String[] courseSuffixList = {"Any", "Full", "E", "A", "B", "F", "G", "Q", "R", "S", "T", "W", "X", "Y", "Z"};



    static ArrayList<CourseSession> seshList;

    public static void main(String[] args) throws IOException {

        Scanner reader = new Scanner(System.in);

        for (String nameValue: courseNameValue) {
            System.out.println(nameValue);
        }

        System.out.print("Enter course name value: ");
        courseInput = reader.next();

        System.out.print("Enter courseCode: ");
        courseCode = reader.next();
//        courseInput = askInput("Enter course name value: ");
//        courseCode = askInput("Enter courseCode: ");
        for (String suffix: courseSuffixList) {
            System.out.print(suffix + " ");
        }
        System.out.print("Enter course suffix from the list: ");
        courseSuffix = reader.next();

        System.out.print("Enter course component (All, LEC, TUT, LAB): ");
        courseComponent = reader.next();

//        courseSuffix = askInput("Enter course suffix from the list: ");
        reader.close();

        System.out.println("The course input is " + courseInput);
        System.out.println("The course code is " + courseCode);
        System.out.println("The course suffix is " + courseSuffix);
        System.out.println("The course component is " + courseComponent);

        int count = 0;
        outerloop:
        while (true) {
            count = count + 1;
            seshList = extract(sendPOST());
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (CourseSession sesh: seshList) {
                if (sesh.getStatus().equals("Not Full")) {
                    System.out.println("Session is available");
                    sendSms("A spot in " + courseInput + " " + courseCode + courseSuffix +  " is available, HURRY!!!");
                    break outerloop;
                }
            }
            System.out.println("None of the courses are available yet " + count);
        }


        print_all_seshes(seshList);
    }

    public static void sendSms(String inputMessage) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber("+12265824878"), // to
                        new PhoneNumber("+18076990021"), // from
                        inputMessage)
                .create();

        System.out.println(message.getSid());
    }

//    public static String askInput(String message) {
//        Scanner reader = new Scanner(System.in);
//        System.out.println(message);
//        String input = reader.next();
//        reader.close();
//        return input;
//    }



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

    private static Document sendPOST() throws IOException {
        List<String> days = new ArrayList<String>();

        days.add("m");
        days.add("tu");
        days.add("w");
        days.add("th");
        days.add("f");

        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("subject", courseInput);
        con.setRequestProperty("Designation", courseSuffix);
        con.setRequestProperty("catalognbr", Integer.toString(Integer.parseInt(courseCode)));
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
//        System.out.println("POST Response Code :: " + responseCode);

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


//            int count = 0;
//            for (Element el : tdTag) {
//                System.out.println(el);
//                count++;
//                if (count == 15) {
//                    System.out.println("______________________________________");
//                    count = 0;
//                }
//            }

            return doc;
        } else {
            System.out.println("POST request not worked");
            return null;
        }
    }

}

/*
/Library/Java/JavaVirtualMachines/jdk-9.0.1.jdk/Contents/Home/bin/java "-javaagent:/Applications/IntelliJ IDEA CE.app/Contents/lib/idea_rt.jar=49210:/Applications/IntelliJ IDEA CE.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath /Users/waqaarkalim/IdeaProjects/Chess1/WebCrawler/out/production/WebCrawler:/Users/waqaarkalim/IdeaProjects/Chess1/WebCrawler/jsoup-1.11.2.jar com.journaldev.utils.HttpURLConnectionExample
POST Response Code :: 200
19   Results <br><br><br>
______________________________________
<td>001</td>
<td>LEC</td>
<td>2580</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">M</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">M</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td>9:30 AM</td>
<td>12:30 PM</td>
<td>AHB-3R07</td>
<td> Barrick </td>
<td> </td>
<td> Full </td>
______________________________________
<td>002</td>
<td>LEC</td>
<td>4807</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">Tu</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">Tu</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td>1:30 PM</td>
<td>4:30 PM</td>
<td>TC-341</td>
<td> Chambers </td>
<td> </td>
<td> Full </td>
______________________________________
<td>003</td>
<td>LEC</td>
<td>2990</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">Tu</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">Tu</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td>9:30 AM</td>
<td>11:30 AM</td>
<td>SH-3307</td>
<td> Aylen </td>
<td> </td>
<td> Full </td>
______________________________________
<td>003</td>
<td>LEC</td>
<td>2990</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">Th</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">Th</td>
<td width="10%">&nbsp;</td>
<td>9:30 AM</td>
<td>10:30 AM</td>
<td>SH-3307</td>
<td> Aylen </td>
<td> </td>
<td> Full </td>
______________________________________
<td>650</td>
<td>LEC</td>
<td>3839</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td></td>
<td></td>
<td></td>
<td> Chambers </td>
<td> ONLINE COURSE. </td>
<td> Full </td>
______________________________________
<td>651</td>
<td>LEC</td>
<td>4293</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td></td>
<td></td>
<td></td>
<td> Chambers </td>
<td> ONLINE COURSE. </td>
<td> Not Full </td>
______________________________________
<td>652</td>
<td>LEC</td>
<td>4586</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td></td>
<td></td>
<td></td>
<td> Freeborn </td>
<td> ONLINE COURSE. </td>
<td> Full </td>
______________________________________
<td>653</td>
<td>LEC</td>
<td>5707</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td></td>
<td></td>
<td></td>
<td> Cull </td>
<td> ONLINE COURSE. </td>
<td> Full </td>
______________________________________
<td>654</td>
<td>LEC</td>
<td>6510</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td></td>
<td></td>
<td></td>
<td> Barrick </td>
<td> ONLINE COURSE. </td>
<td> Full </td>
______________________________________
<td>001</td>
<td>LEC</td>
<td>2582</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">M</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">M</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td>9:30 AM</td>
<td>12:30 PM</td>
<td>AHB-3R07</td>
<td> Aylen </td>
<td> </td>
<td> Full </td>
______________________________________
<td>002</td>
<td>LEC</td>
<td>2583</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">M</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">M</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td>6:30 PM</td>
<td>9:30 PM</td>
<td>AHB-3R07</td>
<td> Wenaus </td>
<td> </td>
<td> Full </td>
______________________________________
<td>003</td>
<td>LEC</td>
<td>2762</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">Tu</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">Tu</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td>1:30 PM</td>
<td>4:30 PM</td>
<td>TC-341</td>
<td> Chambers </td>
<td> </td>
<td> Full </td>
______________________________________
<td>004</td>
<td>LEC</td>
<td>2763</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">Tu</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">Tu</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td>9:30 AM</td>
<td>11:30 AM</td>
<td>MC-17</td>
<td> Halpern </td>
<td> </td>
<td> Full </td>
______________________________________
<td>004</td>
<td>LEC</td>
<td>2763</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">Th</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">Th</td>
<td width="10%">&nbsp;</td>
<td>9:30 AM</td>
<td>10:30 AM</td>
<td>MC-17</td>
<td> Halpern </td>
<td> </td>
<td> Full </td>
______________________________________
<td>650</td>
<td>LEC</td>
<td>2991</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td></td>
<td></td>
<td></td>
<td> Lee </td>
<td> ONLINE COURSE. </td>
<td> Full </td>
______________________________________
<td>651</td>
<td>LEC</td>
<td>4139</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td></td>
<td></td>
<td></td>
<td> Lee </td>
<td> ONLINE COURSE. </td>
<td> Full </td>
______________________________________
<td>652</td>
<td>LEC</td>
<td>5144</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td></td>
<td></td>
<td></td>
<td> Freeborn </td>
<td> ONLINE COURSE. </td>
<td> Full </td>
______________________________________
<td>653</td>
<td>LEC</td>
<td>5708</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td></td>
<td></td>
<td></td>
<td> Cull </td>
<td> ONLINE COURSE. </td>
<td> Full </td>
______________________________________
<td>654</td>
<td>LEC</td>
<td>6511</td>
<td>
 <table class="daysTable">
  <tbody>
   <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
    <td width="10%">&nbsp;</td>
   </tr>
  </tbody>
 </table> </td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td width="10%">&nbsp;</td>
<td></td>
<td></td>
<td></td>
<td> Aylen </td>
<td> ONLINE COURSE. </td>
<td> Full </td>
______________________________________
POST DONE

Process finished with exit code 0

*
* */