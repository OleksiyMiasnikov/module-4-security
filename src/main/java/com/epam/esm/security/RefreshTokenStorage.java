package com.epam.esm.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RefreshTokenStorage extends ConcurrentHashMap<Long,String>{
    private Map<Long, String> tokens = new TreeMap<>();

}
