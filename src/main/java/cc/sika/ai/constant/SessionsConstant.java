package cc.sika.ai.constant;

/**
 * 会话PO常量枚举
 *
 * @author 小吴来哩
 * @since 2025-03
 */
@SuppressWarnings("unused")
public final class SessionsConstant {
	public static final String NEW_SESSION_NAME = "新对话";
	public static final class SessionType {
		public static final Integer SINGLE = 1;
		public static final Integer MULTI = 2;
		
		private SessionType() {
			// do nothing
		}
	}
	
	public static final class SessionStatus {
		public static final Integer RIGHT = 1;
		public static final Integer FROZEN = 2;
		
		private SessionStatus() {
			// do nothing
		}
	}
	
	private SessionsConstant() {
		// do nothing
	}
}
