package com.thebotmeek.api.converters;

import com.foodorder.server.models.meal.PrivateMealConfig;
import com.thebotmeek.api.request.CreatePrivateMealConfig;
import jakarta.inject.Singleton;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class CreatePrivateMealConfigConverter {
    public PrivateMealConfig convertCreatePrivateMealConfigToPrivateMealConfig(CreatePrivateMealConfig createPrivateMealConfig) {
        if (createPrivateMealConfig == null) {
            return null;
        }

        Set<String> recipientIds = Stream.generate(UUID::randomUUID)
                .map(Object::toString).limit(createPrivateMealConfig.getNumberOfRecipients()).collect(Collectors.toSet());
        return new PrivateMealConfig(recipientIds);
    }
}