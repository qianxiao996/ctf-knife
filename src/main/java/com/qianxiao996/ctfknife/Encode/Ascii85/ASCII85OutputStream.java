package com.qianxiao996.ctfknife.Encode.Ascii85;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

final class ASCII85OutputStream extends FilterOutputStream {
    private int lineBreak = 72;
    private int count = 0;
    private byte[] indata = new byte[4];
    private byte[] outdata = new byte[5];
    private int maxline = 72;
    private boolean flushed = true;
    private char terminator = '~';
    private static final char OFFSET = '!';
    private static final char NEWLINE = '\n';
    private static final char Z = 'z';

    ASCII85OutputStream(OutputStream out) {
        super(out);
    }

    public void setTerminator(char term) {
        if (term >= 'v' && term <= '~' && term != 'z') {
            this.terminator = term;
        } else {
            throw new IllegalArgumentException("Terminator must be 118-126 excluding z");
        }
    }

    public char getTerminator() {
        return this.terminator;
    }

    public void setLineLength(int l) {
        if (this.lineBreak > l) {
            this.lineBreak = l;
        }

        this.maxline = l;
    }

    public int getLineLength() {
        return this.maxline;
    }

    private void transformASCII85() {
        long word = (long)((this.indata[0] << 8 | this.indata[1] & 255) << 16 | (this.indata[2] & 255) << 8 | this.indata[3] & 255) & 4294967295L;
        if (word == 0L) {
            this.outdata[0] = 122;
            this.outdata[1] = 0;
        } else {
            long x = word / 52200625L;
            this.outdata[0] = (byte)((int)(x + 33L));
            word -= x * 85L * 85L * 85L * 85L;
            x = word / 614125L;
            this.outdata[1] = (byte)((int)(x + 33L));
            word -= x * 85L * 85L * 85L;
            x = word / 7225L;
            this.outdata[2] = (byte)((int)(x + 33L));
            word -= x * 85L * 85L;
            x = word / 85L;
            this.outdata[3] = (byte)((int)(x + 33L));
            this.outdata[4] = (byte)((int)(word % 85L + 33L));
        }
    }

    public void write(int b) throws IOException {
        this.flushed = false;
        this.indata[this.count++] = (byte)b;
        if (this.count >= 4) {
            this.transformASCII85();

            for(int i = 0; i < 5 && this.outdata[i] != 0; ++i) {
                this.out.write(this.outdata[i]);
                if (--this.lineBreak == 0) {
                    this.out.write(10);
                    this.lineBreak = this.maxline;
                }
            }

            this.count = 0;
        }
    }

    public void flush() throws IOException {
        if (!this.flushed) {
            if (this.count > 0) {
                int i;
                for(i = this.count; i < 4; ++i) {
                    this.indata[i] = 0;
                }

                this.transformASCII85();
                if (this.outdata[0] == 122) {
                    for(i = 0; i < 5; ++i) {
                        this.outdata[i] = 33;
                    }
                }

                for(i = 0; i < this.count + 1; ++i) {
                    this.out.write(this.outdata[i]);
                    if (--this.lineBreak == 0) {
                        this.out.write(10);
                        this.lineBreak = this.maxline;
                    }
                }
            }

            if (--this.lineBreak == 0) {
                this.out.write(10);
            }

            this.out.write(this.terminator);
            this.out.write(62);
            this.out.write(10);
            this.count = 0;
            this.lineBreak = this.maxline;
            this.flushed = true;
            super.flush();
        }
    }

    public void close() throws IOException {
        try {
            this.flush();
            super.close();
        } finally {
            this.indata = this.outdata = null;
        }

    }
}
