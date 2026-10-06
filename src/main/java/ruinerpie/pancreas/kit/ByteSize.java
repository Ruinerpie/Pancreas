package ruinerpie.pancreas.kit;

import ruinerpie.pancreas.*;
import ruinerpie.pancreas.mixin.*;
import ruinerpie.pancreas.mixin.accessor.*;
import ruinerpie.pancreas.values.*;
import ruinerpie.pancreas.signals.*;
import ruinerpie.pancreas.kit.*;
import ruinerpie.pancreas.draw.*;

import java.io.DataOutput;

public class ByteSize implements DataOutput {
    public long count = 0;

    @Override public void write(int b) { count++; }
    @Override public void write(byte[] b) { count += b.length; }
    @Override public void write(byte[] b, int off, int len) { count += len; }
    @Override public void writeBoolean(boolean v) { count += 1; }
    @Override public void writeByte(int v) { count += 1; }
    @Override public void writeShort(int v) { count += 2; }
    @Override public void writeChar(int v) { count += 2; }
    @Override public void writeInt(int v) { count += 4; }
    @Override public void writeLong(long v) { count += 8; }
    @Override public void writeFloat(float v) { count += 4; }
    @Override public void writeDouble(double v) { count += 8; }
    @Override public void writeBytes(String s) { count += s.length(); }
    @Override public void writeChars(String s) { count += s.length() * 2L; }
    @Override public void writeUTF(String s) { count += s.length() + 2; }
}
