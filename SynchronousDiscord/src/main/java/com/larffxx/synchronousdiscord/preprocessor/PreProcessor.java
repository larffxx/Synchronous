package com.larffxx.synchronousdiscord.preprocessor;


public interface PreProcessor<T>{
    T getCommand(String command);
}
