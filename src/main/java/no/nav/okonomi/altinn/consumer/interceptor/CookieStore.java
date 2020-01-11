package no.nav.okonomi.altinn.consumer.interceptor;

/**
 * Lagrer en <i>Cookie</i>;
 */
public class CookieStore {

    private static Object requestCookie;

    private CookieStore() {
        throw new AssertionError("Instantiating cookie class");
    }

    public static void setCookie(Object cookie) {
        requestCookie = cookie;
    }

    public static Object getCookie() {
        return requestCookie;
    }

}