package org.fun.mgs.server

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.Http
import org.fun.mgs.common.Tool
import org.jboss.netty.handler.codec.http.HttpRequest
import org.jboss.netty.handler.codec.http.HttpResponse

import static org.fun.mgs.common.Tool.getConf

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-4
 */
class HttpServer {
    static def startup() {

        def server = new ServerBuilder()
                .codec(new Http())
                .bindTo(new InetAddress(conf.getInt('msg.server.http.port')))
                .name("httpServer")
                .build(new EntryService<HttpRequest, HttpResponse>())
    }
}
