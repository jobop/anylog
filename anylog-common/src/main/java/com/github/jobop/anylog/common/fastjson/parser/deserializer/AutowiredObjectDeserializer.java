package com.github.jobop.anylog.common.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Set;


public interface AutowiredObjectDeserializer extends ObjectDeserializer{
	Set<Type> getAutowiredFor();
}
