package com.example.customeSerializer;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.Deserializer;
import io.micronaut.serde.Decoder;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.time.Instant;

// This is a bit of a bodge until I find a better solution
@Singleton
public class InstantTimestampDeserializer implements Deserializer<Instant> {
    @Override
    public @Nullable Instant deserialize(@NonNull Decoder decoder, DecoderContext context, @NonNull Argument<? super Instant> type) throws IOException {
        long timestamp = decoder.decodeLong();
        return Instant.ofEpochMilli(timestamp);
    }
}
