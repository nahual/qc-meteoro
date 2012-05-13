package ar.com.iron.httpclient;

import org.apache.http.HttpResponse;

/**
 * 
 * @author D.Garcia
 * @since 05/10/2009
 */
public class BadStatusCodeException extends RuntimeException {
	private static final long serialVersionUID = 2696836995404448520L;

	private final int httpStatusCode;

	private final HttpResponse response;

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	/**
	 * @param statusCode
	 * @param response
	 */
	public BadStatusCodeException(int statusCode, HttpResponse response) {
		super("Unexpected HTTP status: " + statusCode);
		httpStatusCode = statusCode;
		this.response = response;
	}

	public HttpResponse getResponse() {
		return response;
	}

}
