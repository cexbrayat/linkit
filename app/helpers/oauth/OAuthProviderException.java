package helpers.oauth;

import models.ProviderType;

/**
 * Exception when invoking a provider : wraps an unexpected WS response
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class OAuthProviderException extends RuntimeException {
    private int code;
    private String body;
    private ProviderType provider;

    public OAuthProviderException(int code, String body, ProviderType provider) {
	super(getMessage(code, body, provider));
	this.code = code;
	this.body = body;
        this.provider = provider;
    }

    public static String getMessage(int code, String body, ProviderType provider) {
	return "Bad response code from " + provider + " : " + code + "\n" + body;
    }

    @Override
    public String getMessage() {
	return getMessage(code, body, provider);
    }
}
