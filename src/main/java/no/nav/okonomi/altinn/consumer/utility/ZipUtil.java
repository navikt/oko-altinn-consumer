package no.nav.okonomi.altinn.consumer.utility;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ZipUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ZipUtil.class);
    private static final byte[] MAGIC_GZIP = new byte[]{0x1F, (byte) 0x8B, 0x08};
    private static final byte[] MAGIC_BZIP = new byte[]{0x42, 0x5A};
    private static final byte[] MAGIC_PKZIP = new byte[]{0x50, 0x4B, 0x03, 0x04};
    private static final byte[] MAGIC_COMPRESS = new byte[]{0x1F, (byte) 0x9D};

    private ZipUtil() {
    }

    /**
     * gzip's a byte array
     *
     * @param input byte array to compress
     * @return byte[] the compressed result
     */
    public static byte[] gzipByteArray(byte[] input) throws IOException {
        return compress(input, null);
    }

    /**
     * zip's a byte array
     *
     * @param input    byte array to compress
     * @param filename
     * @return byte[] the compressed result
     */
    public static byte[] zipByteArray(byte[] input, String filename) throws IOException {
        return compress(input, filename);
    }

    /**
     * compresses a byte array
     *
     * @param uncompressedBytes byte array to compress
     * @param filename
     * @return byte[] the compressed result
     */
    private static byte[] compress(byte[] uncompressedBytes, String filename) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream zout = null;
        try {
            if (filename == null || filename.length() == 0) {
                zout = new GZIPOutputStream(baos);
                IOUtils.write(uncompressedBytes, zout);
                zout.finish();
            } else {
                ZipOutputStream zos = new ZipOutputStream(baos);
                zout = zos;
                ZipEntry entry = new ZipEntry(filename);
                entry.setSize(uncompressedBytes.length);
                zos.putNextEntry(entry);
                zos.write(uncompressedBytes);
                zos.closeEntry();
            }
            byte[] compressedBytes = baos.toByteArray();
            LOG.info("length before compress: {}, after: {}", uncompressedBytes.length, compressedBytes.length);
            return compressedBytes;
        } finally {
            IOUtils.closeQuietly(zout);
            IOUtils.closeQuietly(baos);
        }
    }

    /**
     * ungzip's a byte array
     *
     * @param input byte array to uncompress
     * @return byte[] the uncompressed result
     */
    public static byte[] unGzipByteArray(byte[] input) throws IOException {
        return uncompress(input, true);
    }

    /**
     * unzip's a byte array
     *
     * @param input byte array to uncompress
     * @return byte[] the uncompressed result
     */
    public static byte[] unZipByteArray(byte[] input) throws IOException {
        return uncompress(input, false);
    }

    /**
     * uncompresses a byte array
     *
     * @param compressedBytes byte array to uncompress
     * @param gnu             true for gzip, false for zip
     * @return byte[] the uncompressed result
     */
    private static byte[] uncompress(byte[] compressedBytes, boolean gnu) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(compressedBytes);
        InflaterInputStream zin = gnu ? new GZIPInputStream(bais) : new ZipInputStream(bais);
        byte[] uncompressedBytes = null;
        try {
            if (gnu) {
                uncompressedBytes = IOUtils.toByteArray(zin);

            } else {
                ZipInputStream zis = (ZipInputStream) zin;
                ZipEntry ze = zis.getNextEntry();
                while (ze != null) {
                    uncompressedBytes = IOUtils.toByteArray(zis);
                    ze = zis.getNextEntry();
                }
                zis.closeEntry();
            }
            LOG.info("Lengde før unGzip: {}, lengde etter unGzip: {}", compressedBytes.length, uncompressedBytes != null ? uncompressedBytes.length : 0);
            return uncompressedBytes;
        } finally {
            IOUtils.closeQuietly(zin);
            IOUtils.closeQuietly(bais);
        }
    }

    /**
     * checks if data is gzipped (.gz). can give false positives. if you must be absolutely sure then gunzip data instead.
     *
     * @param data
     * @return
     */
    public static boolean isGzipped(byte[] data) {
        return isZipped(data, MAGIC_GZIP);
    }

    /**
     * checks if data is bzipped (.bz2). can give false positives. if you must be absolutely sure then bunzip2 data instead.
     *
     * @param data
     * @return
     */
    public static boolean isBzipped(byte[] data) {
        return isZipped(data, MAGIC_BZIP);
    }

    /**
     * checks if data is pkzipped (.zip). can give false positives. if you must be absolutely sure then unzip data instead.
     *
     * @param data
     * @return
     */
    public static boolean isPkzipped(byte[] data) {
        return isZipped(data, MAGIC_PKZIP);
    }

    /**
     * checks if data is compressed (.Z) can give false positives. if you must be absolutely sure then uncompress data instead.
     *
     * @param data
     * @return
     */
    public static boolean isCompressed(byte[] data) {
        return isZipped(data, MAGIC_COMPRESS);
    }

    /**
     * adds bytes to a ZipOutputStream
     *
     * @param zos
     * @param bytes
     * @param fname
     * @throws IOException
     */
    public static void addBytesToZipOutputStream(ZipOutputStream zos, byte[] bytes, String fname) throws IOException {
        ZipEntry entry = new ZipEntry(fname);
        CRC32 crc = new CRC32();
        entry.setSize(bytes.length);
        crc.reset();
        crc.update(bytes);
        entry.setCrc(crc.getValue());
        zos.putNextEntry(entry);
        zos.write(bytes);
    }

    private static boolean isZipped(byte[] data, byte[] magicNumbers) {
        if (data != null && data.length > magicNumbers.length) {
            for (int i = 0; i < magicNumbers.length; i++) {
                if (data[i] != magicNumbers[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}