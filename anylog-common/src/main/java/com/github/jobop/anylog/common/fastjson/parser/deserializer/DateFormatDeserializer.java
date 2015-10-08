package com.github.jobop.anylog.common.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import com.github.jobop.anylog.common.fastjson.JSONException;
import com.github.jobop.anylog.common.fastjson.parser.DefaultJSONParser;
import com.github.jobop.anylog.common.fastjson.parser.JSONToken;

public class DateFormatDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {

    public final static DateFormatDeserializer instance = new DateFormatDeserializer();

    @SuppressWarnings("unchecked")
    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
        
        if (val == null) {
            return null;
        }

        if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            
            return (T) new SimpleDateFormat(strVal);
        }

        throw new JSONException("parse error");
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
