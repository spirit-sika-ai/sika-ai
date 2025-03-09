package cc.sika.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.sika.ai.entity.po.Message;
import cc.sika.ai.service.MessageService;
import cc.sika.ai.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
 * @author 22355
 * @since 2025-03-08 22:44:53
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}




