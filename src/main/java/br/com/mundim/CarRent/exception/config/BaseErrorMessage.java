package br.com.mundim.CarRent.exception.config;

import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BaseErrorMessage {

    private final String DEFAULT_RESOURCE = "messages";

    public static final BaseErrorMessage NULL_FIELD = new BaseErrorMessage("nullField");
    public static final BaseErrorMessage CUSTOMER_NOT_FOUND_BY_ID = new BaseErrorMessage("customerNotFoundById");
    public static final BaseErrorMessage CUSTOMER_NOT_FOUND_BY_EMAIL = new BaseErrorMessage("customerNotFoundByEmail");

    private final String key;
    private String[] params;

    public BaseErrorMessage params(final String... params) {
        this.params = Arrays.copyOf(params, params.length);
        return this;
    }

    public String getMessage() {
        String message = tryGetMessageFromBundle();
        if (params != null && params.length > 0) {
            MessageFormat fmt = new MessageFormat(message);
            message = fmt.format(params);
        }
        return message;
    }

    private String tryGetMessageFromBundle(){
        return getResource().getString(key);
    }

    public ResourceBundle getResource(){
        return ResourceBundle.getBundle(DEFAULT_RESOURCE);
    }

}

