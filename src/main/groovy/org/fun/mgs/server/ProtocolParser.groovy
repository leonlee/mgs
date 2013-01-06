package org.fun.mgs.server

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-5
 */

class ProtocolParser {
    static final Logger logger = LoggerFactory.getLogger(ProtocolParser.class)

    static Result process(Message message) {
        logger.debug('processing message {}', message)

        String actorPath = createActorPath(message)
    }

    static String createActorPath(Message message) {
        switch (message.type) {
            case Message.Type.service:
                "akka://mgs/user/root/service/${message.path}"
                break
            case Message.Type.state:
                "akka://mgs/user/root/state/${message.path}"
                break
            default:
                throw new IllegalArgumentException()
        }
    }
}
