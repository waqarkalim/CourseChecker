// Install the Java helper library from twilio.com/docs/libraries/java
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC072b5a8bb4f0dd7a39f256b393928fcb";
    public static final String AUTH_TOKEN = "67aa3886bd1ddd0ca58780b6f3ddf69f";

    public static String inputMessage = "";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber("+12265824878"), // to
                        new PhoneNumber("+18076990021"), // from
                        inputMessage)
                .create();

        System.out.println(message.getSid());
    }
}