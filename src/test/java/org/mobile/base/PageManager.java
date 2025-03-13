package org.mobile.base;

import java.util.HashMap;
import java.util.Map;

import static org.mobile.base.ThreadLocalManager.currentPageTL;

public class PageManager {

    // 🔹 Sayfa isimleri ve sınıfları tutan HashMap
    private static final Map<String, Class<? extends BasePage>> pageRegistry = new HashMap<>();

    // 🔹 Sayfa nesnelerini cachelemek için bir HashMap
    private static final Map<String, BasePage> pageInstances = new HashMap<>();

    // 🔹 Sayfa kaydı (Sadece class bilgisi tutuluyor)
    public static void registerPage(String key, Class<? extends BasePage> pageClass) {
        pageRegistry.put(key, pageClass);
    }

    // 🔹 Lazy Load ile Sayfa Yaratma ve ThreadLocal'a Set Etme
    public static void setPageInstance(String key) {
        BasePage page = pageInstances.computeIfAbsent(key, PageManager::createPageInstance);
        currentPageTL.set(page);
    }

    // 🔹 Sayfa Nesnesi Getirme
    public static BasePage getPageInstance(String key) {
        return pageInstances.computeIfAbsent(key, PageManager::createPageInstance);
    }

    // 🔹 Sayfa Nesnesi Dinamik Olarak Oluşturma
    private static BasePage createPageInstance(String key) {
        Class<? extends BasePage> pageClass = pageRegistry.get(key);
        if (pageClass == null) {
            throw new IllegalArgumentException("Page class not found for key: " + key);
        }
        try {
            return pageClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create page instance for key: " + key, e);
        }
    }
}
