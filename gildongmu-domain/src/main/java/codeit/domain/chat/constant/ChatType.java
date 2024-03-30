package codeit.domain.chat.constant;

public enum ChatType {
    MESSAGE, IMAGE, INFO;

    public boolean isInfoType() {
        return ChatType.INFO.equals(this);
    }
}
