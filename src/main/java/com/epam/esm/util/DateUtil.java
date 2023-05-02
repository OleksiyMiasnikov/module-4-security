package com.epam.esm.util;

import java.time.Clock;
import java.time.Instant;


public class DateUtil {

    public static Instant getDate(){
        return Instant.now(Clock.systemUTC());
    }

}
