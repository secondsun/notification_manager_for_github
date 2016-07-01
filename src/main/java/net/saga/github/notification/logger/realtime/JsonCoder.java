/*
 * Copyright (C) 2016 summers.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package net.saga.github.notification.logger.realtime;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author summers and
 * http://kingsfleet.blogspot.com/2013/04/using-java-websockets-jsr-356-and-json.html
 */
public abstract class JsonCoder<T> implements Encoder.TextStream<T>, Decoder.TextStream<T> {

    private Class<T> _type;

    // When configured my read in that ObjectMapper is not thread safe
    //
    private ThreadLocal<ObjectMapper> _mapper = new ThreadLocal<ObjectMapper>() {

        @Override
        protected ObjectMapper initialValue() {
            return new ObjectMapper();
        }
    };

    @Override
    public void init(EndpointConfig endpointConfig) {

        ParameterizedType $thisClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type $T = $thisClass.getActualTypeArguments()[0];
        if ($T instanceof Class) {
            _type = (Class<T>) $T;
        } else if ($T instanceof ParameterizedType) {
            _type = (Class<T>) ((ParameterizedType) $T).getRawType();
        }
    }

    @Override
    public void encode(T object, Writer writer) throws EncodeException, IOException {
        _mapper.get().writeValue(writer, object);
    }

    @Override
    public T decode(Reader reader) throws DecodeException, IOException {
        return _mapper.get().readValue(reader, _type);
    }

    @Override
    public void destroy() {

    }

}
