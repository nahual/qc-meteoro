package ar.com.iron.httpclient;

/**
 * Indica un error en las comunicaciones. Generalmente algo que está mal configurado en el request
 * 
 * @author D.Garcia
 * @since 05/10/2009
 */
public class ConnectionClientException extends RuntimeException {
	private static final long serialVersionUID = -3883119664126591256L;

	public ConnectionClientException(Throwable e) {
		super(e);
	}

	public ConnectionClientException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ConnectionClientException(String detailMessage) {
		super(detailMessage);
	}

}
