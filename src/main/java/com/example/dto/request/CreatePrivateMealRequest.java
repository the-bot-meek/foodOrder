package com.example.dto.request;

import io.micronaut.core.annotation.Introspected;
import java.time.Instant;
import java.util.Set;

@Introspected
public class CreatePrivateMealRequest extends CreateMealRequest {
    private Set<String> participants;
    public CreatePrivateMealRequest(String name, Instant dateOfMeal, String location, String venueName, Boolean draft, Set<String> participants) {
        super(name, dateOfMeal, location, venueName, draft);
        this.participants = participants;
    }

    public Set<String> getParticipant() {
        return participants;
    }

    public void setParticipant(Set<String> participants) {
        this.participants = participants;
    }
}
