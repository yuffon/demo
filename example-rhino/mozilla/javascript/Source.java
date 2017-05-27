package mozilla.javascript;

public class Source {
    public Source() {
        // OPT the default 16 is probably too small, but it's not
        // clear just what size would work best for most javascript.
        // It'd be nice to know what characterizes the javascript
        // that's out there.
        buf = new StringBuffer(64);
    }

    void append(char c) {
        buf.append(c);
    }

    void addString(int type, String str) {
        buf.append((char)type);
        // java string length < 2^16?
        buf.append((char)str.length());
        buf.append(str);
    }

    void addNumber(Number n) {
        buf.append((char)TokenStream.NUMBER);

        /* encode the number in the source stream.
         * Save as NUMBER type (char | char char char char)
         * where type is
         * 'D' - double, 'S' - short, 'J' - long.

         * We need to retain float vs. integer type info to keep the
         * behavior of liveconnect type-guessing the same after
         * decompilation.  (Liveconnect tries to present 1.0 to Java
         * as a float/double)
         * OPT: This is no longer true. We could compress the format.

         * This may not be the most space-efficient encoding;
         * the chars created below may take up to 3 bytes in
         * constant pool UTF-8 encoding, so a Double could take
         * up to 12 bytes.
         */

        if (n instanceof Double || n instanceof Float) {
            // if it's floating point, save as a Double bit pattern.
            // (12/15/97 our scanner only returns Double for f.p.)
            buf.append('D');
            long lbits = Double.doubleToLongBits(n.doubleValue());

            buf.append((char)((lbits >> 48) & 0xFFFF));
            buf.append((char)((lbits >> 32) & 0xFFFF));
            buf.append((char)((lbits >> 16) & 0xFFFF));
            buf.append((char)(lbits & 0xFFFF));
        } else {
            long lbits = n.longValue();
            // will it fit in a char?
            // (we can ignore negative values, bc they're already prefixed
            //  by UNARYOP SUB)
            // this gives a short encoding for integer values up to 2^16.
            if (lbits <= Character.MAX_VALUE) {
                buf.append('S');
                buf.append((char)lbits);
            } else { // Integral, but won't fit in a char. Store as a long.
                buf.append('J');
                buf.append((char)((lbits >> 48) & 0xFFFF));
                buf.append((char)((lbits >> 32) & 0xFFFF));
                buf.append((char)((lbits >> 16) & 0xFFFF));
                buf.append((char)(lbits & 0xFFFF));
            }
        }
    }

    char functionNumber;
    StringBuffer buf;
}

