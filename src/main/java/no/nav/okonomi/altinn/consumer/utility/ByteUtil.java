package no.nav.okonomi.altinn.consumer.utility;

import com.google.common.primitives.Bytes;
import org.apache.commons.io.IOUtils;

import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

public final class ByteUtil {

    private static final String APPLICATION_XML = "application/xml";
    private static final String APPLICATION_PKCS7_SIGNATURE = "application/pkcs7-signature";
    public static final String APPLICATION_PKCS7_MIME = "application/pkcs7-mime";
    public static final String APPLICATION_X_GZIP = "application/x-gzip";
    public static final String APPLICATION_X_COMPRESSED = "application/x-compressed";
    public static final String APPLICATION_ZIP = "application/zip";
    public static final String APPLICATION_X_BZIP2 = "application/x-bzip2";

    private ByteUtil() {
    }

    private static final byte[] HEX_CHAR_TABLE = {
            (byte) '0', (byte) '1', (byte) '2', (byte) '3',
            (byte) '4', (byte) '5', (byte) '6', (byte) '7',
            (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
            (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };

    private static final int UNSIGNED_SHIFT_RIGHT_LENGTH = 4;
    private static final int SHIFT_LEFT_LENGTH = 4;
    private static final int BYTES_PR_LINE = 16;
    private static final byte HEX_F = 0xF;
    private static final int HEX_FF = 0xFF;
    private static final byte HEX_UTF8_BYTE = (byte) 0xc3;

    // PKCS7 enveloped with different lengths
    private static final byte[] PKCS7_MIME = { 0x06, 0x09, 0x2A, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xF7, 0x0D, 0x01, 0x07, 0x03 };
    private static final Magic PKCS7_MIME_1 = new Magic(APPLICATION_PKCS7_MIME, 2, PKCS7_MIME);
    private static final Magic PKCS7_MIME_2 = new Magic(APPLICATION_PKCS7_MIME, 3, PKCS7_MIME);
    private static final Magic PKCS7_MIME_3 = new Magic(APPLICATION_PKCS7_MIME, 4, PKCS7_MIME);
    private static final Magic PKCS7_MIME_4 = new Magic(APPLICATION_PKCS7_MIME, 5, PKCS7_MIME);
    private static final Magic PKCS7_MIME_5 = new Magic(APPLICATION_PKCS7_MIME, 6, PKCS7_MIME);
    private static final Magic PKCS7_MIME_6 = new Magic(APPLICATION_PKCS7_MIME, 7, PKCS7_MIME);
    private static final Magic PKCS7_MIME_7 = new Magic(APPLICATION_PKCS7_MIME, 8, PKCS7_MIME);
    private static final Magic PKCS7_MIME_8 = new Magic(APPLICATION_PKCS7_MIME, 9, PKCS7_MIME);
    private static final Magic PKCS7_MIME_9 = new Magic(APPLICATION_PKCS7_MIME, 10, PKCS7_MIME);
    // PKCS7 signature with different lengths
    private static final byte[] PKCS7_SIGNATURE = { 0x06, 0x09, 0x2A, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xF7, 0x0D, 0x01, 0x07, 0x02 };
    private static final Magic PKCS7_SIGNATURE_1 = new Magic(APPLICATION_PKCS7_SIGNATURE, 2, PKCS7_SIGNATURE);
    private static final Magic PKCS7_SIGNATURE_2 = new Magic(APPLICATION_PKCS7_SIGNATURE, 3, PKCS7_SIGNATURE);
    private static final Magic PKCS7_SIGNATURE_3 = new Magic(APPLICATION_PKCS7_SIGNATURE, 4, PKCS7_SIGNATURE);
    private static final Magic PKCS7_SIGNATURE_4 = new Magic(APPLICATION_PKCS7_SIGNATURE, 5, PKCS7_SIGNATURE);
    private static final Magic PKCS7_SIGNATURE_5 = new Magic(APPLICATION_PKCS7_SIGNATURE, 6, PKCS7_SIGNATURE);
    private static final Magic PKCS7_SIGNATURE_6 = new Magic(APPLICATION_PKCS7_SIGNATURE, 7, PKCS7_SIGNATURE);
    private static final Magic PKCS7_SIGNATURE_7 = new Magic(APPLICATION_PKCS7_SIGNATURE, 8, PKCS7_SIGNATURE);
    private static final Magic PKCS7_SIGNATURE_8 = new Magic(APPLICATION_PKCS7_SIGNATURE, 9, PKCS7_SIGNATURE);
    private static final Magic PKCS7_SIGNATURE_9 = new Magic(APPLICATION_PKCS7_SIGNATURE, 10, PKCS7_SIGNATURE);
    // compressed data
    private static final Magic GZIP = new Magic(APPLICATION_X_GZIP, 0, new byte[] { 0x1F, (byte) 0x8B, 0x08 });
    private static final Magic BZIP2 = new Magic(APPLICATION_X_BZIP2, 0, new byte[] { 0x42, 0x5A });
    private static final Magic ZIP = new Magic(APPLICATION_ZIP, 0, new byte[] { 0x50, 0x4B, 0x03, 0x04 }); // PKZIP (.zip)
    private static final Magic COMPRESSED = new Magic(APPLICATION_X_COMPRESSED, 0, new byte[] { 0x1F, (byte) 0x9D }); // COMPRESS
                                                                                                                      // (.Z)
    // xml without byte order mark
    private static final Magic XML = new Magic(APPLICATION_XML, 0, new byte[] { 0x3C, 0x3F, 0x78, 0x6D, 0x6C });
    private static final Magic XML_UTF16BE = new Magic(APPLICATION_XML, 0, new byte[] { 0x00, 0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x6D, 0x00, 0x6C });
    private static final Magic XML_UTF16LE = new Magic(APPLICATION_XML, 0, new byte[] { 0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x6D, 0x00, 0x6C, 0x00 });
    private static final Magic XML_UTF32BE = new Magic(APPLICATION_XML, 0, new byte[] { 0x00, 0x00, 0x00, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00, 0x00, 0x00, 0x6D, 0x00, 0x00, 0x00, 0x6C });
    private static final Magic XML_UTF32LE = new Magic(APPLICATION_XML, 0, new byte[] { 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00, 0x00, 0x00, 0x6D, 0x00, 0x00, 0x00, 0x6C, 0x00, 0x00, 0x00 });
    // xml with bom
    private static final Magic XML_BOM = new Magic(APPLICATION_XML, 3, new byte[] { 0x3C, 0x3F, 0x78, 0x6D, 0x6C });
    private static final Magic XML_BOM_UTF16BE = new Magic(APPLICATION_XML, 2, new byte[] { 0x00, 0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x6D, 0x00, 0x6C });
    private static final Magic XML_BOM_UTF16LE = new Magic(APPLICATION_XML, 2, new byte[] { 0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x6D, 0x00, 0x6C, 0x00 });
    private static final Magic XML_BOM_UTF32BE = new Magic(APPLICATION_XML, 4, new byte[] { 0x00, 0x00, 0x00, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00, 0x00, 0x00, 0x6D, 0x00, 0x00, 0x00, 0x6C });
    private static final Magic XML_BOM_UTF32LE = new Magic(APPLICATION_XML, 4, new byte[] { 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00, 0x00, 0x00, 0x6D, 0x00, 0x00, 0x00, 0x6C, 0x00, 0x00, 0x00 });
    // xml uppercase without bom
    private static final Magic XML_UPPER = new Magic(APPLICATION_XML, 0, new byte[] { 0x3C, 0x3F, 0x58, 0x4D, 0x4C });
    private static final Magic XML_UPPER_UTF16BE = new Magic(APPLICATION_XML, 0, new byte[] { 0x00, 0x3C, 0x00, 0x3F, 0x00, 0x58, 0x00, 0x4D, 0x00, 0x4C });
    private static final Magic XML_UPPER_UTF16LE = new Magic(APPLICATION_XML, 0, new byte[] { 0x3C, 0x00, 0x3F, 0x00, 0x58, 0x00, 0x4D, 0x00, 0x4C, 0x00 });
    private static final Magic XML_UPPER_UTF32BE = new Magic(APPLICATION_XML, 0, new byte[] { 0x00, 0x00, 0x00, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x58, 0x00, 0x00, 0x00, 0x4D, 0x00, 0x00, 0x00, 0x4C });
    private static final Magic XML_UPPER_UTF32LE = new Magic(APPLICATION_XML, 0, new byte[] { 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x58, 0x00, 0x00, 0x00, 0x4D, 0x00, 0x00, 0x00, 0x4C, 0x00, 0x00, 0x00 });
    // xml uppercase with bom
    private static final Magic XML_UPPER_BOM = new Magic(APPLICATION_XML, 3, new byte[] { 0x3C, 0x3F, 0x58, 0x4D, 0x4C });
    private static final Magic XML_UPPER_BOM_UTF16BE = new Magic(APPLICATION_XML, 2, new byte[] { 0x00, 0x3C, 0x00, 0x3F, 0x00, 0x58, 0x00, 0x4D, 0x00, 0x4C });
    private static final Magic XML_UPPER_BOM_UTF16LE = new Magic(APPLICATION_XML, 2, new byte[] { 0x3C, 0x00, 0x3F, 0x00, 0x48, 0x00, 0x4D, 0x00, 0x4C, 0x00 });
    private static final Magic XML_UPPER_BOM_UTF32BE = new Magic(APPLICATION_XML, 4,
            new byte[] { 0x00, 0x00, 0x00, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x58, 0x00, 0x00, 0x00, 0x4D, 0x00, 0x00, 0x00, 0x4C });
    private static final Magic XML_UPPER_BOM_UTF32LE = new Magic(APPLICATION_XML, 4,
            new byte[] { 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x58, 0x00, 0x00, 0x00, 0x4D, 0x00, 0x00, 0x00, 0x4C, 0x00, 0x00, 0x00 });

    // pdf
    private static final Magic PDF = new Magic("application/pdf", 0, new byte[] { 0x25, 0x50, 0x44, 0x46 });

    // array of filetypes we care about arranged so we get a match early
    private static final Magic[] FILETYPES = {
            PKCS7_MIME_1, PKCS7_MIME_2, PKCS7_MIME_3,
            PKCS7_MIME_4, PKCS7_MIME_5, PKCS7_MIME_6,
            PKCS7_MIME_7, PKCS7_MIME_8, PKCS7_MIME_9,
            GZIP,
            XML,
            XML_BOM,
            XML_UPPER,
            XML_UPPER_BOM,
            XML_UTF16BE, XML_BOM_UTF16BE, XML_UPPER_UTF16BE, XML_UPPER_BOM_UTF16BE,
            XML_UTF16LE, XML_BOM_UTF16LE, XML_UPPER_UTF16LE, XML_UPPER_BOM_UTF16LE,
            XML_UTF32BE, XML_BOM_UTF32BE, XML_UPPER_UTF32BE, XML_UPPER_BOM_UTF32BE,
            XML_UTF32LE, XML_BOM_UTF32LE, XML_UPPER_UTF32LE, XML_UPPER_BOM_UTF32LE,
            BZIP2, ZIP, COMPRESSED,
            PKCS7_SIGNATURE_1, PKCS7_SIGNATURE_2, PKCS7_SIGNATURE_3,
            PKCS7_SIGNATURE_4, PKCS7_SIGNATURE_5, PKCS7_SIGNATURE_6,
            PKCS7_SIGNATURE_7, PKCS7_SIGNATURE_8, PKCS7_SIGNATURE_9,
            PDF
    };

    /**
     * Convert a byte[] array to readable string format. This makes the "hex" readable!
     *
     * @param raw
     *            byte[] buffer to convert to string format
     * @return result String buffer in String format
     */
    public static String byteArrayToHexString(byte[] raw, String charset) throws UnsupportedEncodingException {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;

        for (byte b : raw) {
            int v = b & HEX_FF;
            hex[index++] = HEX_CHAR_TABLE[v >>> UNSIGNED_SHIFT_RIGHT_LENGTH];
            hex[index++] = HEX_CHAR_TABLE[v & HEX_F];
        }
        return new String(hex, charset);
    }

    /**
     * Convert a readable hex string to a byte array
     *
     * @param str
     *            the hex string
     * @return a byte array
     */
    public static byte[] hexStringToByteArray(String str) {
        int numberOfChars = str.length();
        byte[] raw = new byte[numberOfChars / 2];
        for (int i = 0; i < numberOfChars; i += 2) {
            raw[i / 2] = (byte) ((Character.digit(str.charAt(i), BYTES_PR_LINE) << SHIFT_LEFT_LENGTH)
                    + Character.digit(str.charAt(i + 1), BYTES_PR_LINE));
        }
        return raw;
    }

    /**
     * returns a hex view of a byte array example output:
     *
     * 00000000 - 31 32 33 34 35 36 37 38 31 32 33 34 35 36 37 38 - 1234567812345678 00000016 - 31 32 33 34 35 36 37 38 38 37 36
     * 35 34 33 32 31 - 1234567887654321 00000032 - F8 E6 E5 F6 - ����
     *
     * unprintable bytes, including \n etc, are shown as (char) 0.
     *
     * @param bytes
     * @return
     */
    public static String hexView(byte[] bytes, String charset) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        int remaining = bytes.length;
        int start = 0;
        while (remaining > 0) {
            sb.append(String.format("%08d", start)).append(" - ");
            appendHexLine(bytes, sb, start, charset);

            // now print printable string
            int j = appendPrintableLine(bytes, sb, remaining, start);
            remaining -= j;
            start += j;
            // line break after 16 bytes if we have more left
            if (remaining > 0) {
                sb.append('\n');
            }

        }
        return sb.toString();
    }

    private static final class Magic {
        private byte[] magi;
        private String mime;
        private int offset;

        private Magic(String mime, int offset, byte[] magi) {
            this.mime = mime;
            this.offset = offset;
            this.magi = magi == null ? null : magi.clone();
        }

        private boolean match(byte[] input) {
            int length = offset + magi.length;
            if (input.length < length) {
                return false;
            }
            byte[] a = Arrays.copyOfRange(input, offset, length);
            return Arrays.equals(a, magi);
        }
    }

    /**
     * gets the mime content-type
     *
     * @param input
     * @return
     */
    public static String getMimeContentType(byte[] input) {
        if (input != null && input.length > 0) {
            for (Magic m : FILETYPES) {
                if (m.match(input)) {
                    return m.mime;
                }
            }
        }
        return "application/octet-stream";
    }

    /**
     * returns true if the byte array has UTF-8 encoding
     *
     * @param bytes
     * @return
     */
    public static boolean isUtf8(byte[] bytes) {
        return Bytes.indexOf(bytes, HEX_UTF8_BYTE) >= 0;
    }

    /**
     * converts from one charset to another
     *
     * @param bytes
     * @param fromCharset
     * @param toCharset
     * @return
     * @throws IOException
     */
    public static byte[] convert(byte[] bytes, Charset fromCharset, Charset toCharset) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStreamReader input = new InputStreamReader(new ByteArrayInputStream(bytes), fromCharset);
        IOUtils.copy(input, baos, toCharset);
        return baos.toByteArray();
    }

    private static void appendHexLine(byte[] bytes, StringBuilder sb, int start, String charset) throws UnsupportedEncodingException {
        int j = start;
        int i = 0;
        while (i < BYTES_PR_LINE && j < bytes.length) {
            byte[] hex = new byte[2];
            int v = bytes[j] & HEX_FF;
            hex[0] = HEX_CHAR_TABLE[v >>> UNSIGNED_SHIFT_RIGHT_LENGTH];
            hex[1] = HEX_CHAR_TABLE[v & HEX_F];
            sb.append(new String(hex, charset)).append(' ');
            i++;
            j++;
        }
        while (i < BYTES_PR_LINE) {
            sb.append("   ");
            i++;
        }
    }

    private static int appendPrintableLine(byte[] bytes, StringBuilder sb,
            int remaining, int start) {
        int j;
        sb.append(" - ");
        String tmp = new String(bytes, start, remaining > BYTES_PR_LINE ? BYTES_PR_LINE : remaining);
        for (j = 0; j < tmp.length(); j++) {
            char c = tmp.charAt(j);
            if (!isPrintable(c)) {
                c = (char) 0;
            }
            sb.append(c);

        }
        return j;
    }

    private static boolean isPrintable(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c) && block != null && block != Character.UnicodeBlock.SPECIALS && c != KeyEvent.CHAR_UNDEFINED);
    }
}