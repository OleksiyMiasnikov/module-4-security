package com.epam.esm.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

@Component
public class RefreshTokenStorage extends TreeMap<Long,String>{
    private Map<Long, String> tokens = new TreeMap<>();

}
