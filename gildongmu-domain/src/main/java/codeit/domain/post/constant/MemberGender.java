package codeit.domain.post.constant;

public enum MemberGender {
    FEMALE("F"),
    MALE("M"),
    NONE("N");

    private final String code;

    MemberGender(final String code) {
        this.code = code;
    }

    public String getCode() { return code; }
}
