package cc.sika.ai.exception;

public class SessionError extends RuntimeException {
    public SessionError() {
        super("会话不存在");
    }

    public SessionError(String message) {
        super(message);
    }
}
