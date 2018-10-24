package com.jwcjlu.gateway.metrics;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsService{
    private static Logger logger = LoggerFactory.getLogger(MetricsService.class);

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        System.out.println(4096&15);
        System.out.println(((16384*2+17)&~15)+16);
        System.out.println(16384*2);
        DOMConfigurator.configureAndWatch("log4j.xml", 5000L);
       /* while (true)
        {
            try
            {
                logger.info("hello-Info");
                logger.debug("hello-Debug");
                logger.error("hello-Error");
                Thread.sleep(5000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }*/
    }


}
