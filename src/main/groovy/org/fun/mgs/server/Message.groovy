package org.fun.mgs.server

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-5
 */
class Message {
    String endpoint
    Type type
    String path

    Object[] params

    def validate() {
        type = endpoint.contains('/srv/') ? Type.service : (endpoint.contains('/stt/') ? Type.state : null)
        if (!type) {
            throw new IllegalArgumentException()
        }
        path = endpoint.substring(5)
    }

    enum Type {
        service, state
    }
}
