package com.jwcjlu.gateway.common.exception;

/**
 * <p>Description: .</p>
 *
 * @author xiaoyu(Myth)
 */
public class EwayNotSupportedException extends RuntimeException {

    private static final long serialVersionUID = 8068509879445395353L;

    public EwayNotSupportedException(final Throwable e) {
        super(e);
    }

    public EwayNotSupportedException(final String message) {
        super(message);
    }

    public EwayNotSupportedException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
