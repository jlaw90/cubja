package com.sqrt4.cubja.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public final class DiskHeader {
    public byte consoleId;
    public short gameCode;
    public byte countryCode;
    public short makerCode;
    public byte diskId;
    public byte version;
    public byte audioStreaming;
    public byte streamBufferSize;
    public int magic = 0xc2339f3d;
    public String name;
    public int offfsetToDebugMonitor;
    public int debugMonitorAddr;
    public int offsetToExe;
    public int offsetToFst;
    public int sizeOfFst;
    public int maxFstSize;
    public int userPosition;
    public int userLength;
    public int unknown1;

    public DiskHeader() {}

    public void write(SeekableByteChannel source) throws IOException {
        source.position(0);
        ByteBuffer buf = ByteBuffer.allocateDirect(992);
        buf.put(consoleId);
        buf.putShort(gameCode);
        buf.put(countryCode);
        buf.putShort(makerCode);
        buf.put(diskId);
        buf.put(version);
        buf.put(audioStreaming);
        buf.put(streamBufferSize);
        for(int i = 0; i < 18; i++) // Skip 18 bytes
            buf.put((byte) 0);
        buf.putInt(magic);
        buf.flip();
        source.write(buf);
        buf.clear();
        buf.put(name.getBytes());
        while(buf.hasRemaining())
            buf.put((byte) 0);
        buf.flip();
        source.write(buf);
        buf.clear();
        buf.putInt(offfsetToDebugMonitor);
        buf.putInt(debugMonitorAddr);
        for(int i = 0; i < 6; i++) // Skip 24 bytes
            buf.putInt(0);
        buf.putInt(offsetToExe);
        buf.putInt(offsetToFst);
        buf.putInt(sizeOfFst);
        buf.putInt(maxFstSize);
        buf.putInt(userPosition);
        buf.putInt(userLength);
        buf.putInt(unknown1);
        buf.putInt(0); // Skip 4 bytes
        buf.flip();
        source.write(buf);
    }

    public static DiskHeader read(SeekableByteChannel source) throws IOException {
        ByteBuffer buf = ByteBuffer.allocateDirect(992);
        DiskHeader hd = new DiskHeader();

        source.position(0);
        buf.limit(32);
        source.read(buf);
        buf.flip();

        hd.consoleId = buf.get();
        hd.gameCode = buf.getShort();
        hd.countryCode = buf.get();
        hd.makerCode = buf.getShort();
        hd.diskId = buf.get();
        hd.version = buf.get();
        hd.audioStreaming = buf.get();
        hd.streamBufferSize = buf.get();
        buf.position(buf.position() + 18);
        hd.magic = buf.getInt();
        if(hd.magic != 0xc2339f3d)
            throw new RuntimeException("Invalid magic: 0x" + String.format("%06X", hd.magic));

        buf.clear();
        source.read(buf);
        buf.flip();

        byte[] t = new byte[992];
        buf.get(t);
        String name = new String(t).trim();
        hd.name = name;

        buf.clear();
        buf.limit(64);
        source.read(buf);
        buf.flip();

        hd.offfsetToDebugMonitor = buf.getInt();
        hd.debugMonitorAddr = buf.getInt();
        buf.position(buf.position() + 24);
        hd.offsetToExe = buf.getInt();
        hd.offsetToFst = buf.getInt();
        hd.sizeOfFst = buf.getInt();
        hd.maxFstSize = buf.getInt();
        hd.userPosition = buf.getInt();
        hd.userLength = buf.getInt();
        hd.unknown1 = buf.getInt();

        return hd;
    }
}