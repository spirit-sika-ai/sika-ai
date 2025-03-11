package cc.sika.ai.constant;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
public final class MessageType {
	/**
	 * 用户消息
	 */
	public static final Integer USER_MESSAGE = 1;
	/**
	 * 助理消息
	 */
	public static final Integer ASSISTANT_MESSAGE = 2;
	/**
	 * 系统消息
	 */
	public static final Integer SYSTEM_MESSAGE = 3;
	/**
	 * 工具消息
	 */
	public static final Integer TOOL_MESSAGE = 4;
	
	
	private MessageType() {
		// do nothing
	}
}
