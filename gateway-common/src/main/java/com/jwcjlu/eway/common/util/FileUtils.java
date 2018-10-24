package com.jwcjlu.gateway.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public final class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    public static String readFile(String fileName) {
        StringBuffer builder = new StringBuffer();
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + "file is not found");
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                // stringBuilder.append(line);
                builder.append(line).append(" ");
            }
        } catch (IOException e) {
            logger.error("read file failure ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("close reader stream failure", e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("close input stream failure", e);
                }
            }
        }
        return builder.toString();
    }
}
