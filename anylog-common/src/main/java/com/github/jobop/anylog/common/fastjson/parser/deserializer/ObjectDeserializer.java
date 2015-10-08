package com.github.jobop.anylog.common.fastjson.parser.deserializer;

import java.lang.reflect.Type;

import com.github.jobop.anylog.common.fastjson.parser.DefaultJSONParser;

public interface ObjectDeserializer {
    <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName);
    
    int getFastMatchToken();
}
