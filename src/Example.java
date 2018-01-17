// Install the Java helper library from twilio.com/docs/java/install

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Example {
    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "ACca4b341b28132a00477fe3505f247ad6";
    public static final String AUTH_TOKEN =  "9e3295fd87e576a026e206f78d7be572";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message sms = Message.creator(new PhoneNumber("+12264011196"), new PhoneNumber("15005550006"),
                "All in the game, yo").create();

        System.out.println(sms.getSid());
    }
}