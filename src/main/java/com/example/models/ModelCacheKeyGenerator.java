package com.example.models;

import io.micronaut.cache.interceptor.CacheKeyGenerator;
import io.micronaut.cache.interceptor.ParametersKey;
import io.micronaut.core.annotation.AnnotationMetadata;


public class ModelCacheKeyGenerator implements CacheKeyGenerator {
    @Override
    public Object generateKey(AnnotationMetadata annotationMetadata, Object... params) {
        if (params.length != 1 || params[0] == null || !(params[0] instanceof Model model)) {
            throw new IllegalArgumentException("ModelCacheKeyGenerator::generateKey can only take a none null Model as a parameter.");
        }
        return new ParametersKey(model.getPrimaryKey(), model.getSortKey());
    }
}
