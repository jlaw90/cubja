package com.sqrt4.cubja.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * Created by James on 29/07/2014.
 */
public class Apploader {
    public String version;
    public int entryPoint;
    public int size;
    public int trailerSize;
    public int unknown1;
    public byte[] apploaderCode;

    public static Apploader read(SeekableByteChannel source) throws IOException {
        Apploader al = new Apploader();
        int pos = 0x2440;
        source.position(pos);
        ByteBuffer bb = ByteBuffer.allocateDirect(0x2000);
        bb.limit(32);
        source.read(bb);
        bb.flip();
        byte[] data = new byte[16];
        bb.get(data);
        al.version = new String(data).trim();
        al.entryPoint = bb.getInt();
        al.size = bb.getInt();
        al.trailerSize = bb.getInt();
        al.unknown1 = bb.getInt();

        al.apploaderCode = new byte[al.size - 32];

        for(int off = 0; off < al.apploaderCode.length;) {
            int remaining = (int) ((0x2440 + al.size) - source.position());
            int read = Math.min(remaining, bb.capacity());
            bb.clear();
            bb.limit(read);
            source.read(bb);
            bb.flip();
            bb.get(al.apploaderCode, off, read);
            off += read;
        }
        return al;
    }
}