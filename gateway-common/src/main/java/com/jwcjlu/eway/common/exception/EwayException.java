package com.jwcjlu.gateway.common.exception;

/**
 * <p>Description: .</p>
 *
 * @author xiaoyu(Myth)
 */
public class EwayException extends RuntimeException {

    private static final long serialVersionUID = 8068509879445395353L;

    public EwayException(final Throwable e) {
        super(e);
    }

    public EwayException(final String message) {
        super(message);
    }

    public EwayException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
