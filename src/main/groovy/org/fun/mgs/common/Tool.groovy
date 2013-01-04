package org.fun.mgs.common

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-4
 */
class Tool {
    static Config conf = ConfigFactory.load('msg')
}
