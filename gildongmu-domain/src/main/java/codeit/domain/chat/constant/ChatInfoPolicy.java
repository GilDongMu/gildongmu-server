package codeit.domain.chat.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChatInfoPolicy {
    PARTICIPANT_ENTER_INFO("%s님이 참가했습니다.");
    public final String message;

    public String getInfoMessage(String nickname){
        return String.format(message, nickname);
    }

}
