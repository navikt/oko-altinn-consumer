package no.nav.okonomi.altinn.consumer.interceptor;

import java.time.ZonedDateTime;

/**
 * Lagrer en <i>Cookie</i> i trådens minne.
 */
public class CookieStore {

    private static ThreadLocal<Object> requestCookie = new ThreadLocal<>();
    private static ThreadLocal<ZonedDateTime> requestCookieTimeout = new ThreadLocal<>();

    private CookieStore() {
        throw new AssertionError("Instantiating cookie class");
    }

    public static void setCookie(Object cookie) {
        requestCookie.set(cookie);
    }

    public static Object getCookie() {
        return requestCookie.get();
    }

    public static ZonedDateTime getRequestCookieTimeout() {
        return requestCookieTimeout.get();
    }

    public static void setRequestCookieTimeout(ZonedDateTime timeout) {
        requestCookieTimeout.set(timeout);
    }
}