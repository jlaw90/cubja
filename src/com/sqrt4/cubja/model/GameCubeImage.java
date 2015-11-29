package com.sqrt4.cubja.model;

import com.sqrt4.cubja.model.fst.DirNode;
import com.sqrt4.cubja.model.fst.FSNode;
import com.sqrt4.cubja.model.fst.FileNode;
import com.sqrt4.cubja.util.TemporaryFileChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

/**
 * Created by James on 28/07/2014.
 */
public final class GameCubeImage {
    public final SeekableByteChannel channel;
    public final DiskHeader header;
    public final DiskHeaderInfo info;
    public final Apploader apploader;
    public final DirNode directoryTree;

    public GameCubeImage(SeekableByteChannel dataChannel) throws IOException {
        this.channel = dataChannel;
        this.header = DiskHeader.read(dataChannel);
        this.info = DiskHeaderInfo.read(dataChannel);
        this.apploader = Apploader.read(dataChannel);

        System.out.println(dataChannel.position());

        // Read file system...
        dataChannel.position(header.offsetToFst);
        ByteBuffer temp = ByteBuffer.allocateDirect(header.sizeOfFst);
        dataChannel.read(temp);
        temp.flip();

        int numEntries = temp.getInt(8);
        final int nameTableOff = numEntries * 12;
        temp.position(12);

        StringBuilder sb = new StringBuilder();
        Stack<DirNode> dirStack = new Stack<DirNode>();

        dirStack.push(directoryTree = new DirNode(this, null, null, numEntries));

        for (int i = 1; i < numEntries; i++) {
            int pos = i * 12;
            temp.position(pos);

            while(i >= dirStack.peek().endId)
                Collections.sort(dirStack.pop().children);

            DirNode parent = dirStack.peek();

            int pre = temp.getInt();
            byte flags = (byte) (pre >>> 24);
            int nameOffset = pre & 0xffffff;
            int off = temp.getInt();
            int lenOrNext = temp.getInt();

            temp.position(nameTableOff + nameOffset);
            byte b;
            sb.setLength(0);
            while ((b = temp.get()) != 0)
                sb.append((char) b);
            String name = sb.toString();

            if(flags == 0) { // file
                parent.children.add(new FileNode(this, name, parent, (long) off, lenOrNext));
            } else { // directory
                DirNode n = new DirNode(this, name, parent, lenOrNext);
                parent.children.add(n);
                dirStack.push(n);
            }
        }
        Collections.sort(directoryTree.children);
    }
}