package com.zufar.icedlatte.payment.api.customer;

import com.stripe.exception.StripeException;
import com.stripe.model.Token;
import com.zufar.icedlatte.openapi.dto.CreateCardDetailsTokenRequest;
import com.zufar.icedlatte.payment.config.StripeConfiguration;
import com.zufar.icedlatte.payment.exception.CardTokenCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CardDetailsProcessor {

    private final StripeConfiguration stripeConfiguration;

    public String processCardDetails(CreateCardDetailsTokenRequest createCardDetailsTokenRequest) {
        StripeConfiguration.setStripeKey(stripeConfiguration.publishableKey());

        Map<String, Object> card = createCardDetails(createCardDetailsTokenRequest);
        Token cardDetailsToken;
        try {
            cardDetailsToken = Token.create(Map.of("card", card));
        } catch (StripeException e) {
            throw new CardTokenCreationException(createCardDetailsTokenRequest.getCardNumber());
        }
        return cardDetailsToken.getId();
    }

    private Map<String, Object> createCardDetails(CreateCardDetailsTokenRequest createCardDetailsTokenRequest) {
        return Map.of("number", createCardDetailsTokenRequest.getCardNumber(),
                "exp_month", Integer.parseInt(createCardDetailsTokenRequest.getExpMonth()),
                "exp_year", Integer.parseInt(createCardDetailsTokenRequest.getExpYear()),
                "cvc", createCardDetailsTokenRequest.getCvc());
    }
}
