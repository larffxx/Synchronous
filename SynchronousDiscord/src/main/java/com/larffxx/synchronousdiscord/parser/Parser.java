package com.larffxx.synchronousdiscord.parser;

public interface Parser <T, L> {
    L parse(T t);
}
