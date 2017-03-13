package io.gxu.anymall.annotation;

public @interface ServiceEntry {
	String value();

	HttpMethod[] type() default { HttpMethod.GET, HttpMethod.POST };

	public static enum HttpMethod {
		GET, POST, PUT, DELETE
	}
}
