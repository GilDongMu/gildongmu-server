package codeit.domain.post.constant;

public enum Status {
    OPEN("모집 중"),
    CLOSED("모집 완료");

    private final String code;

    Status(final String code) {
        this.code = code;
    }

    public String getCode() { return code; }
}


