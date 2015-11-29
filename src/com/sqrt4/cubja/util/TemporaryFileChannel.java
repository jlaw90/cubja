package com.sqrt4.cubja.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

/**
 * Created by James on 28/07/2014.
 */
public class TemporaryFileChannel implements SeekableByteChannel {
    private final SeekableByteChannel delegate;

    public TemporaryFileChannel() throws IOException {
        delegate = Files.newByteChannel(File.createTempFile("temp", ".bin").toPath(), StandardOpenOption.DELETE_ON_CLOSE, StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.SPARSE);
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return delegate.read(dst);
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        return delegate.write(src);
    }

    @Override
    public long position() throws IOException {
        return delegate.position();
    }

    @Override
    public TemporaryFileChannel position(long newPosition) throws IOException {
        delegate.position(newPosition);
        return this;
    }

    @Override
    public long size() throws IOException {
        return delegate.size();
    }

    @Override
    public TemporaryFileChannel truncate(long size) throws IOException {
        delegate.truncate(size);
        return this;
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}