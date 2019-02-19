package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.*;

public class TestClass {



    public static void main(String[] args) {
        //		System.out.println("TempMain");
        //		System.out.println(628331966747D);
        //throw new UnsupportedOperationException();
        Logger logger = getLogger("consoleLogger");
        //levels of logging in order from most to least
        System.out.println("Trace enabled? " + logger.isTraceEnabled());
        System.out.println("Debug enabled? " + logger.isDebugEnabled());
        System.out.println("Info enabled? " + logger.isInfoEnabled());
        System.out.println("Warn enabled? " + logger.isWarnEnabled());
        System.out.println("Error enabled? " + logger.isErrorEnabled());

        Marker marker = MarkerFactory.getMarker(Marker.ANY_MARKER);


        logger.info("Hello World");
        int i = 20;
        logger.debug("test {} go", i);
        logger.error("error type");
        logger.error(marker, "custom marker--?? (how do I actually see the marker?)");
        logger.warn("warn");
        logger.trace("trace");
        //System.out.printf();
        logger.info(String.format("%-20s%-20s%-20s","Test name","Status","Duration,ms"));
        logger.info("------------------------------------------------------------");
        logger.info(String.format("%-20s%-20s%-20d","updatenotfound", "success", 2222222));
//        logger.info(String.format("Test %-10s %-10s spent %d microseconds", "updatenotfound", "success", 2222222));

    }
}
