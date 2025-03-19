package org.mobile.base;

import java.util.HashMap;
import java.util.Map;

import static org.mobile.base.ThreadLocalManager.currentPageTL;
import static org.mobile.config.LogConfig.logInfo;
import static org.mobile.config.LogConfig.logDebug;

public class PageManager {

    private static final Map<String, Class<? extends BasePage>> pageRegistry = new HashMap<>();
    private static final Map<String, BasePage> pageInstances = new HashMap<>();

    // This method is used to register the page class like for class path we used to do with the key
    public static void registerPage(String key, Class<? extends BasePage> pageClass) {
        logInfo("\033[33m[Page-Manager]-Registering Page: [%s] -> [%s]\033[0m".formatted(key, pageClass.getSimpleName()));
        pageRegistry.put(key, pageClass);
    }

    public static void setPageInstance(String key) {
        logDebug("\033[36m[Page-Manager]-Setting Page Instance for key: [%s]\033[0m".formatted(key));
        BasePage page = pageInstances.computeIfAbsent(key, PageManager::createPageInstance);
        currentPageTL.set(page);
        logInfo("\033[33m[Page-Manager] Active Page Set: [%s]\033[0m".formatted(key));
    }

    //maybe we can remove compute if absent because the value is not present, it will create an instance for it
    public static BasePage getPageInstance(String key) {
        logDebug("\033[36m[Page-Manager]-Getting the Page Instance for key: [%s]\033[0m".formatted(key));
        return pageInstances.computeIfAbsent(key, PageManager::createPageInstance);
    }

    private static BasePage createPageInstance(String key) {
        logInfo("\033[33m[Page-Manager]-Creating new Page Instance for key: [%s]\033[0m".formatted(key));

        Class<? extends BasePage> pageClass = pageRegistry.get(key);
        if (pageClass == null) {
            throw new IllegalArgumentException("Page class not found for key: " + key);
        }

        try {
            BasePage instance = pageClass.getDeclaredConstructor().newInstance();
            logInfo("\033[33m[Page-Manager]-Page Instance Created: [%s]\033[0m".formatted(pageClass.getSimpleName()));
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create page instance for key: " + key, e);
        }
    }
}