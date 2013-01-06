package org.fun.mgs.server;

import com.google.gson.Gson;
import com.twitter.finagle.Service;
import com.twitter.util.Future;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.fun.mgs.common.Tool;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryService extends Service<HttpRequest, HttpResponse> {
    @Override
    public Future<HttpResponse> apply(HttpRequest request) {
        if (!request.getMethod().getName().equals("POST")) {
            logger.warn("received invalid request {}", DefaultGroovyMethods.getProperties(request));
            return Future.value((HttpResponse) new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
        }

        if (!request.getUri().equals(Tool.getConf().getString("mgs.server.http.uri"))) {
            logger.warn("received wrong uri request {}", request.getUri());
            return Future.value((HttpResponse) new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
        }

        HttpPostRequestDecoder postRequestDecoder = null;
        InterfaceHttpData data = null;
        try {
            postRequestDecoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
            data = postRequestDecoder.getBodyHttpData("message");


            String text = null;
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                text = ((Attribute) data).getValue();
            }

            Message message = null;
            try {
                message = new Gson().fromJson(text, Message.class);
                message.validate();
            } catch (Exception e) {
                logger.warn("invalid message format {}", text);
                return Future.value((HttpResponse) new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            }

            Result result = null;
            try {
                result = ProtocolParser.process(message);
            } catch (Exception e) {
                logger.error("can't process message " + String.valueOf(message), (Throwable) e);
                return Future.value((HttpResponse) new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR));
            }

            DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.setContent(ChannelBuffers.copiedBuffer(new Gson().toJson(result).getBytes()));
            return Future.value((HttpResponse) response);

        } catch (Exception e) {
            return Future.value((HttpResponse) new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(EntryService.class);
}
