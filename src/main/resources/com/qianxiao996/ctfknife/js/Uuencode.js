/**
 * Convert a string to an array of bytes using ASCII encoding.
 *
 * @param {String} str The string to convert.
 * @returns {Uint8Array} The byte array.
 */
function stringToByteArray(str) {
    var byteArray = new Uint8Array(str.length);
    for (var i = 0; i < str.length; ++i) {
        byteArray[i] = str.charCodeAt(i) & 0xFF;
    }
    return byteArray;
}

/**
 * Convert an array of bytes to a string using ASCII encoding.
 *
 * @param {Uint8Array} byteArray The byte array to convert.
 * @returns {String} The string.
 */
function byteArrayToString(byteArray) {
    var str = '';
    for (var i = 0; i < byteArray.length; ++i) {
        str += String.fromCharCode(byteArray[i]);
    }
    return str;
}

/**
 * uuencode a value
 *
 * @param {String} The value to be encoded.
 * @returns {String} The encoded value.
 */
function encode(inString) {
    var inBytes = stringToByteArray(inString);
    var outBytes = [];
    var bytesRead = 0;

    function writeByte(byte) {
        outBytes.push(byte);
    }

    function encodeBytes(inIndex, n) {
        for (var i = 0; i < n; i += 3) {
            var c1 = inBytes[inIndex] >>> 2;
            var c2 = ((inBytes[inIndex] & 0x03) << 4) | (inBytes[inIndex + 1] >>> 4);
            var c3 = ((inBytes[inIndex + 1] & 0x0F) << 2) | (inBytes[inIndex + 2] >>> 6);
            var c4 = inBytes[inIndex + 2] & 0x3F;

            writeByte((c1 & 0x3F) + 32);
            writeByte((c2 & 0x3F) + 32);
            writeByte((c3 & 0x3F) + 32);
            writeByte((c4 & 0x3F) + 32);

            inIndex += 3;
        }
    }

    while (bytesRead < inBytes.length) {
        var bytesLeft = inBytes.length - bytesRead;
        var n = Math.min(bytesLeft, 45);

        writeByte((n & 0x3F) + 32);
        encodeBytes(bytesRead, n);
        writeByte(10); // Line feed

        bytesRead += n;
    }

    return byteArrayToString(new Uint8Array(outBytes));
}

/**
 * uudecode a value
 *
 * @param {String} The value to be decoded.
 * @returns {String} The decoded value.
 */
function decode(inString) {
    var inBytes = stringToByteArray(inString);
    var outBytes = [];
    var totalLen = 0;

    function writeByte(byte) {
        outBytes.push(byte);
    }

    function decodeChars(inIndex) {
        var c1 = inBytes[inIndex];
        var c2 = inBytes[inIndex + 1];
        var c3 = inBytes[inIndex + 2];
        var c4 = inBytes[inIndex + 3];

        var b1 = ((c1 - 32) & 0x3F) << 2 | ((c2 - 32) & 0x3F) >> 4;
        var b2 = ((c2 - 32) & 0xF) << 4 | ((c3 - 32) & 0x3F) >> 2;
        var b3 = ((c3 - 32) & 0x3) << 6 | (c4 - 32) & 0x3F;

        writeByte(b1);
        if (b2 !== 0 || totalLen % 3 === 1) writeByte(b2);
        if (b3 !== 0 || totalLen % 3 === 2) writeByte(b3);
    }

    for (var inIndex = 0; inIndex < inBytes.length;) {
        var n = inBytes[inIndex++] - 32 & 0x3F;

        if (n > 45) throw new Error('Invalid Data');

        totalLen += n;

        while (n > 0) {
            decodeChars(inIndex);
            inIndex += 4;
            n -= 3;
        }

        ++inIndex; // Skip line feed
    }

    return byteArrayToString(new Uint8Array(outBytes));
}