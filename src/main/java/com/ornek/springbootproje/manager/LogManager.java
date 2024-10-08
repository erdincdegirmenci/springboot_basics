package com.ornek.springbootproje.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogManager {

    private final Logger logger = LoggerFactory.getLogger(LogManager.class);

    // Bilgilendirme logları
    public void logInfo(String message) {
        logger.info(message);
    }

    // Hata logları, exception dahil
    public void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    // Uyarı logları
    public void logWarning(String message) {
        logger.warn(message);
    }

    // Debug logları (detaylı bilgi)
    public void logDebug(String message) {
        logger.debug(message);
    }

    // İzleme logları (çok detaylı)
    public void logTrace(String message) {
        logger.trace(message);
    }
}
