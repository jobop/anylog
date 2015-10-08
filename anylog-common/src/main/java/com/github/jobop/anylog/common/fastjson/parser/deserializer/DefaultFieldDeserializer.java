package com.github.jobop.anylog.common.fastjson.parser.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import com.github.jobop.anylog.common.fastjson.parser.DefaultJSONParser;
import com.github.jobop.anylog.common.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.github.jobop.anylog.common.fastjson.parser.JSONToken;
import com.github.jobop.anylog.common.fastjson.parser.ParseContext;
import com.github.jobop.anylog.common.fastjson.parser.ParserConfig;
import com.github.jobop.anylog.common.fastjson.util.FieldInfo;

public class DefaultFieldDeserializer extends FieldDeserializer {

    private ObjectDeserializer fieldValueDeserilizer;

    public DefaultFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        if (fieldValueDeserilizer == null) {
            fieldValueDeserilizer = parser.getConfig().getDeserializer(fieldInfo);
        }

        if (objectType instanceof ParameterizedType) {
            ParseContext objContext = parser.getContext();
            objContext.setType(objectType);
        }

        Object value = fieldValueDeserilizer.deserialze(parser, getFieldType(), fieldInfo.getName());
        if (parser.getResolveStatus() == DefaultJSONParser.NeedToResolve) {
            ResolveTask task = parser.getLastResolveTask();
            task.setFieldDeserializer(this);
            task.setOwnerContext(parser.getContext());
            parser.setResolveStatus(DefaultJSONParser.NONE);
        } else {
            if (object == null) {
                fieldValues.put(fieldInfo.getName(), value);
            } else {
                setValue(object, value);
            }
        }
    }

    public int getFastMatchToken() {
        if (fieldValueDeserilizer != null) {
            return fieldValueDeserilizer.getFastMatchToken();
        }

        return JSONToken.LITERAL_INT;
    }
}
