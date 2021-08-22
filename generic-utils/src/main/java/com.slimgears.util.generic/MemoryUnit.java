package com.slimgears.util.generic;

public enum MemoryUnit {
    BYTES(1L),
    KILOBYTES(1L << 10),
    MEGABYTES(1L << 20),
    GIGABYTES(1L << 30),
    TERABYTES(1L << 40);

    private final long scale;

    MemoryUnit(long scale) {
        this.scale = scale;
    }

    public long convert(long size, MemoryUnit unit) {
        return scale == unit.scale ? size : size * scale / unit.scale;
    }

    public long toBytes(long size) {
        return convert(size, MemoryUnit.BYTES);
    }

    public long toKiloBytes(long size) {
        return convert(size, MemoryUnit.KILOBYTES);
    }

    public long toMegaBytes(long size) {
        return convert(size, MemoryUnit.MEGABYTES);
    }

    public long toGigaBytes(long size) {
        return convert(size, MemoryUnit.GIGABYTES);
    }

    public long toTeraBytes(long size) {
        return convert(size, MemoryUnit.TERABYTES);
    }
}
