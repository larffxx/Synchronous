package com.larffxx.synchronousdiscord.infrastructure.parser;

public interface Parser <T, L> {
    L parse(T t);
}
