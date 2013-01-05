package org.fun.mgs.server

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-5
 */
class Result {
    enum Status {
        Ok, Error
    }

    String status
    String error
    Object data
}
