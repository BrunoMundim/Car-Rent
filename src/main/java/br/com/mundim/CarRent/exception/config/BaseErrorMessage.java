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
    public static final BaseErrorMessage CAR_NOT_FOUND_BY_ID = new BaseErrorMessage("carNotFoundById");
    public static final BaseErrorMessage CAR_NOT_FOUND_BY_PLATE = new BaseErrorMessage("carNotFoundByPlate");
    public static final BaseErrorMessage RENT_NOT_FOUND_BY_ID = new BaseErrorMessage("rentNotFoundById");
    public static final BaseErrorMessage UNAVAILABLE_CAR = new BaseErrorMessage("unavailableCar");
    public static final BaseErrorMessage RENT_CANNOT_CHANGE_BECAUSE_CAR_ALREADY_RETURNED = new BaseErrorMessage("rentCannotChangeBecauseCarAlreadyReturned");
    public static final BaseErrorMessage CANNOT_DELETE_RENTED_CAR = new BaseErrorMessage("cannotDeleteRentedCar");

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

