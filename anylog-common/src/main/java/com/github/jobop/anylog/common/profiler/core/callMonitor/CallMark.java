package com.github.jobop.anylog.common.profiler.core.callMonitor;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

import com.github.jobop.anylog.common.fastjson.JSON;

/**
 * 访问标记信息
 * 
 * @author fx
 */
public class CallMark implements Serializable {

	private static final long serialVersionUID = 1L;
	private Calendar time;
	private String method;
	private Object[] args;

	public CallMark() {
		super();
	}

	public CallMark(Calendar time, String method, Object[] args) {
		super();
		this.time = time;
		this.method = method;
		this.args = args;
	}

	public CallMark(Calendar time, String method) {
		this(time, method, null);
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 */
	public String toString() {
		DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		StringBuilder sb = new StringBuilder();
		sb.append("callMark:{");
		if (time == null) {
			sb.append("time:" + "null");
		} else {
			sb.append("time:" + df.format(time.getTime()));
		}
		sb.append(",");
		sb.append("method:" + method);
		if (null!=args) {
			sb.append(",");
			sb.append("args:");
			sb.append(JSON.toJSONString(args));
//			sb.append("}");
		}

		sb.append("}");
		return sb.toString();

	}
}
