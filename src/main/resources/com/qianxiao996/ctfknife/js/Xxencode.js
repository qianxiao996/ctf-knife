var base = '+-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';

/**
 * Convert a string to an array of bytes using the specified encoding.
 *
 * @param {String} str The string to convert.
 * @param {String} encoding The encoding to use.
 * @returns {Uint8Array} The byte array.
 */
function stringToByteArray(str, encoding) {
    if (encoding === 'utf8') {
        // If TextEncoder is not available, fallback to manual conversion for ASCII
        if (typeof TextEncoder !== 'undefined') {
            return new TextEncoder().encode(str);
        } else {
            var byteArray = new Uint8Array(str.length);
            for (var i = 0; i < str.length; ++i) {
                byteArray[i] = str.charCodeAt(i) & 0xFF;
            }
            return byteArray;
        }
    } else {
        var byteArray = new Uint8Array(str.length);
        for (var i = 0; i < str.length; ++i) {
            byteArray[i] = str.charCodeAt(i) & 0xFF;
        }
        return byteArray;
    }
}

/**
 * Convert an array of bytes to a string using the specified encoding.
 *
 * @param {Uint8Array} byteArray The byte array to convert.
 * @param {String} encoding The encoding to use.
 * @returns {String} The string.
 */
function byteArrayToString(byteArray, encoding) {
    if (encoding === 'utf8') {
        // If TextDecoder is not available, fallback to manual conversion for ASCII
        if (typeof TextDecoder !== 'undefined') {
            return new TextDecoder().decode(byteArray);
        } else {
            var str = '';
            for (var i = 0; i < byteArray.length; ++i) {
                str += String.fromCharCode(byteArray[i]);
            }
            return str;
        }
    } else {
        var str = '';
        for (var i = 0; i < byteArray.length; ++i) {
            str += String.fromCharCode(byteArray[i]);
        }
        return str;
    }
}

/**
 * Encode a value using custom uuencode algorithm.
 *
 * @param {String} inString The value to be encoded.
 * @param {String} encoding The encoding to use.
 * @returns {String} The encoded value.
 */
function encode(inString, encoding) {
    encoding = encoding || 'utf8';

    var inBytes = stringToByteArray(inString, encoding);
    var buffLen = inBytes.length;
    var outBytes = [];
    var outLen = 0;

    function encodeBytes(inIndex) {
        var c1 = inBytes[inIndex] >>> 2;
        var c2 = ((inBytes[inIndex] & 0x03) << 4) | (inBytes[inIndex + 1] >>> 4);
        var c3 = ((inBytes[inIndex + 1] & 0x0F) << 2) | (inBytes[inIndex + 2] >>> 6);
        var c4 = inBytes[inIndex + 2] & 0x3F;

        outBytes.push((c1 & 0x3F), (c2 & 0x3F), (c3 & 0x3F), (c4 & 0x3F));
    }

    for (var i = 0; i < buffLen; i += 3) {
        encodeBytes(i);
    }

    var raw_result = '';
    for (var j = 0; j < outBytes.length; j++) {
        raw_result += base[outBytes[j]];
    }

    var result_array = [];
    var lnum = Math.floor(buffLen / 45);
    for (var k = 0; k < lnum; k++) {
        result_array.push(base[45] + raw_result.substr(k * 60, 60) + "\r\n");
    }

    var left = buffLen % 45;
    if (left != 0) {
        result_array.push(base[left] + raw_result.substr(lnum * 60) + "\r\n");
    }

    var final_result = result_array.join('');
    return final_result.substring(0, final_result.length - 2);
}

/**
 * Decode a value using custom uudecode algorithm.
 *
 * @param {String} inString The value to be decoded.
 * @param {String} encoding The encoding to use.
 * @returns {String} The decoded value.
 */
function decode(inString, encoding) {
    encoding = encoding || 'utf8';

    var in_array = inString.split(/\r?\n/);
    var raw_string = '';
    var total_len = 0;

    for (var k = 0; k < in_array.length; k++) {
        var str = in_array[k];
        var first = str.charAt(0);
        var line_ascii_num = base.indexOf(first);

        total_len += line_ascii_num;

        var content = str.slice(1);
        raw_string += content;
    }

    var inBytes = new Uint8Array(raw_string.length);
    for (var cn = 0; cn < raw_string.length; cn++) {
        inBytes[cn] = base.indexOf(raw_string[cn]);
    }

    var outBytes = [];
    function decodeChars(inIndex) {
        var c1 = inBytes[inIndex];
        var c2 = inBytes[inIndex + 1];
        var c3 = inBytes[inIndex + 2];
        var c4 = inBytes[inIndex + 3];

        var b1 = ((c1 & 0x3F) << 2) | ((c2 & 0x3F) >> 4);
        var b2 = ((c2 & 0xF) << 4) | ((c3 & 0x3F) >> 2);
        var b3 = ((c3 & 0x3) << 6) | (c4 & 0x3F);

        outBytes.push(b1 & 0xFF);
        if (b2 !== 0 || outBytes.length % 3 === 1) outBytes.push(b2 & 0xFF);
        if (b3 !== 0 || outBytes.length % 3 === 2) outBytes.push(b3 & 0xFF);
    }

    for (var i = 0; i < inBytes.length; i += 4) {
        decodeChars(i);
    }

    // Create a Uint8Array with only the necessary bytes
    var finalOutBytes = new Uint8Array(total_len);
    for (var idx = 0; idx < total_len; idx++) {
        finalOutBytes[idx] = outBytes[idx];
    }

    return byteArrayToString(finalOutBytes, encoding);
}