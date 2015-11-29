package com.sqrt4.cubja.model.fst;

import com.sqrt4.cubja.model.GameCubeImage;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by James on 29/07/2014.
 */
public class FileNode extends FSNode {
    public long offset;
    public int length;


    public FileNode(GameCubeImage container, String name, DirNode parent, long offfset, int length) {
        super(container, name, parent);
        this.offset = offfset;
        this.length = length;
    }

    public int compareTo(FSNode o) {
        if(o instanceof DirNode)
            return 1;
        return name.compareTo(o.name);
    }

    public ByteBuffer readData() throws IOException {
        ByteBuffer buf = ByteBuffer.allocateDirect(length);
        container.channel.position(offset);
        while(buf.hasRemaining()) {
            int read = container.channel.read(buf);
            if(read == -1)
                throw new IOException("End of stream reached!");
        }
        buf.flip();
        return buf;
    }
}