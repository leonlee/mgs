package org.fun.mgs.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.twitter.finagle.Service
import com.twitter.util.Future
import org.jboss.netty.buffer.ByteBufferBackedChannelBuffer
import org.jboss.netty.handler.codec.http.*
import org.jboss.netty.handler.codec.http.multipart.Attribute
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.ByteBuffer

import static org.fun.mgs.common.Tool.getConf
import static org.jboss.netty.buffer.ChannelBuffers.copiedBuffer

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-5
 */
class EntryService extends Service<HttpRequest, HttpResponse> {
    Logger logger = LoggerFactory.getLogger(EntryService.class)

    @Override
    Future<HttpResponse> apply(HttpRequest request) {
        if (request.method.name != 'POST') {
            logger.warn('received invalid request {}', request.properties)
            return Future.value(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND))
        }

        if (request.uri != conf.getString('mgs.server.http.uri')) {
            logger.warn('received wrong uri request {}', request.uri)
            return Future.value(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN))
        }

        HttpPostRequestDecoder postRequestDecoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request)
        def data = postRequestDecoder.getBodyHttpData('message')

        String text = null
        if (data.httpDataType == InterfaceHttpData.HttpDataType.Attribute) {
            text = (data as Attribute).value
        }

        Message message = null
        try {
            message = new Gson().fromJson(text, Message.class)
        } catch (e) {
            logger.warn('invalid message format {}', text)
            return Future.value(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST))
        }

        try{
            def result = ProtocolParser.process(message)
        } catch (e) {
            logger.error("can't process message $message", e)
            return Future.value(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR))
        }

        def response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
        response.setContent(copiedBuffer(result))

        return Future.value(response)
    }
}
