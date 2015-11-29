package com.sqrt4.cubja.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * Created by James on 29/07/2014.
 */
public class DiskHeaderInfo {
    public int debugMonitorSize;
    public int simulatedMemorySize;
    public int argumentOffset;
    public int debugFlag;
    public int trackLocation;
    public int trackSize;
    public int countryCode;
    public int unknown1;
    public byte[] unknown2 = new byte[0x1fe0];

    public void write(SeekableByteChannel channel) throws IOException {
        channel.position(0x400);
        ByteBuffer buf = ByteBuffer.allocateDirect(0x2000);
        buf.putInt(debugMonitorSize);
        buf.putInt(simulatedMemorySize);
        buf.putInt(argumentOffset);
        buf.putInt(debugFlag);
        buf.putInt(trackLocation);
        buf.putInt(trackSize);
        buf.putInt(countryCode);
        buf.putInt(unknown1);
        buf.put(unknown2);
        buf.flip();
        channel.write(buf);
    }

    public static DiskHeaderInfo read(SeekableByteChannel channel) throws IOException {
        channel.position(0x440);
        ByteBuffer buf = ByteBuffer.allocateDirect(0x2000);
        channel.read(buf);
        buf.flip();

        DiskHeaderInfo dhi = new DiskHeaderInfo();
        dhi.debugMonitorSize = buf.getInt();
        dhi.simulatedMemorySize = buf.getInt();
        dhi.argumentOffset = buf.getInt();
        dhi.debugFlag = buf.getInt();
        dhi.trackLocation = buf.getInt();
        dhi.trackSize = buf.getInt();
        dhi.countryCode = buf.getInt();
        dhi.unknown1 = buf.getInt();
        buf.get(dhi.unknown2);
        return dhi;
    }
}