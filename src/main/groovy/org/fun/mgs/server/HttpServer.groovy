package org.fun.mgs.server

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.Http
import org.jboss.netty.handler.codec.http.HttpRequest
import org.jboss.netty.handler.codec.http.HttpResponse
import org.slf4j.LoggerFactory

import java.util.logging.Logger

import static org.fun.mgs.common.Tool.conf

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-4
 */
class HttpServer {
    static final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpServer.class)

    static def startup() {
        logger.info("starting http server...")

        def server = new ServerBuilder()
                .codec(new Http())
                .bindTo(new InetAddress(conf.getInt('mgs.server.http.port')))
                .name(conf.getString('mgs.server.http.name'))
                .logger(Logger.getLogger(HttpServer.class))
                .build(new EntryService<HttpRequest, HttpResponse>())

        logger.info("http server was started")

    }
}
