package codeit.api.alert.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseNotificationService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendNotification(String token, String title, String content) throws FirebaseMessagingException {
        Message message = Message.builder()
            .setToken(token)
            .putData("title", title)
            .putData("body", content)
            .build();

        firebaseMessaging.send(message);
    }
}
