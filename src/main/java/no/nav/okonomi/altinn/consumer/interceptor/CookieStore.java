package no.nav.okonomi.altinn.consumer.interceptor;

/**
 * Lagrer en <i>Cookie</i> i trådens minne.
 */
public class CookieStore {

    private static ThreadLocal<Object> requestCookie = new ThreadLocal<>();

    private CookieStore() {
        throw new AssertionError("Instantiating cookie class");
    }

    public static void setCookie(Object cookie) {
        requestCookie.set(cookie);
    }

    public static Object getCookie() {
        return requestCookie.get();
    }

}