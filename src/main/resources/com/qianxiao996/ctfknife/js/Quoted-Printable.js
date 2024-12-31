var EQUAL_SIGN = 61;
var QUESTION_MARK = 63;

function convertStringToUnicodeCodePoints(e) {
    var g = 0;
    var c = [];
    for (var d = 0; d < e.length; ++d) {
        var b = e.charCodeAt(d);
        if (g != 0) {
            if (b >= 56320 && b <= 57343) {
                var a = b;
                var f = (g - 55296) * (1 << 10) + (1 << 16) + (a - 56320);
                c.push(f)
            } else {
            }
            g = 0
        } else {
            if (b >= 55296 && b <= 56319) {
                g = b
            } else {
                c.push(b)
            }
        }
    }
    return c
}

function convertUtf8BytesToUnicodeCodePoints(f) {
    var a = [];
    var e = 0;
    var c = 0;
    for (var b = 0; b < f.length; ++b) {
        var d = f[b];
        if (d >= 256) {
        } else {
            if ((d & 192) == 128) {
                if (c > 0) {
                    e = (e << 6) | (d & 63);
                    c -= 1
                } else {
                }
            } else {
                if (c == 0) {
                    a.push(e)
                } else {
                }
                if (d < 128) {
                    e = d;
                    c = 0
                } else {
                    if ((d & 224) == 192) {
                        e = d & 31;
                        c = 1
                    } else {
                        if ((d & 240) == 224) {
                            e = d & 15;
                            c = 2
                        } else {
                            if ((d & 248) == 240) {
                                e = d & 7;
                                c = 3
                            } else {
                            }
                        }
                    }
                }
            }
        }
    }
    if (c == 0) {
        a.push(e)
    } else {
    }
    a.shift();
    return a
}

function convertEscapedCodesToCodes(g, f, c, j) {
    var d = g.split(f);
    d.shift();
    var a = [];
    var h = Math.pow(2, j);
    for (var e = 0; e < d.length; ++e) {
        var b = parseInt(d[e], c);
        if (b >= 0 && b < h) {
            a.push(b)
        } else {
        }
    }
    return a
}

function convertEscapedUtf16CodesToUtf16Codes(a) {
    return convertEscapedCodesToCodes(a, "\\u", 16, 16)
}

function convertEscapedUtf32CodesToUnicodeCodePoints(a) {
    return convertEscapedCodesToCodes(a, "\\U", 16, 32)
}

function convertEscapedBytesToBytes(c, b) {
    var a = (b == 16 ? "\\x" : "\\");
    return convertEscapedCodesToCodes(c, a, b, 8)
}

function convertNumRefToUnicodeCodePoints(f, e) {
    var d = f.split(";");
    d.pop();
    var b = [];
    for (var c = 0; c < d.length; ++c) {
        var a = d[c].replace(/^&#x?/, "");
        var g = parseInt(a, e);
        b.push(g)
    }
    return b
}

function convertUnicodeCodePointsToUtf16Codes(a) {
    var d = [];
    for (var c = 0; c < a.length; ++c) {
        var f = a[c];
        if (f < (1 << 16)) {
            d.push(f)
        } else {
            var e = ((f - (1 << 16)) / (1 << 10)) + 55296;
            var b = (f % (1 << 10)) + 56320;
            d.push(e);
            d.push(b)
        }
    }
    return d
}

function convertUnicodeCodePointToUtf8Bytes(c, a) {
    var b = [];
    if (c < 128) {
        b.push(c)
    } else {
        if (c < (1 << 11)) {
            b.push((c >>> 6) | 192);
            b.push((c & 63) | 128)
        } else {
            if (c < (1 << 16)) {
                b.push((c >>> 12) | 224);
                b.push(((c >> 6) & 63) | 128);
                b.push((c & 63) | 128)
            } else {
                if (c < (1 << 21)) {
                    b.push((c >>> 18) | 240);
                    b.push(((c >> 12) & 63) | 128);
                    b.push(((c >> 6) & 63) | 128);
                    b.push((c & 63) | 128)
                }
            }
        }
    }
    return b
}

function convertUnicodeCodePointsToUtf8Bytes(b) {
    var d = [];
    for (var c = 0; c < b.length; ++c) {
        var a = convertUnicodeCodePointToUtf8Bytes(b[c]);
        d = d.concat(a)
    }
    return d
}

function formatNumber(c, d, a) {
    var e = c.toString(d).toUpperCase();
    for (var b = e.length; b < a; ++b) {
        e = "0" + e
    }
    return e
}

var BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

function encodeBase64Helper(a) {
    var b = [];
    if (a.length == 1) {
        b.push(BASE64.charAt(a[0] >> 2));
        b.push(BASE64.charAt(((a[0] & 3) << 4)));
        b.push("=");
        b.push("=")
    } else {
        if (a.length == 2) {
            b.push(BASE64.charAt(a[0] >> 2));
            b.push(BASE64.charAt(((a[0] & 3) << 4) | (a[1] >> 4)));
            b.push(BASE64.charAt(((a[1] & 15) << 2)));
            b.push("=")
        } else {
            if (a.length == 3) {
                b.push(BASE64.charAt(a[0] >> 2));
                b.push(BASE64.charAt(((a[0] & 3) << 4) | (a[1] >> 4)));
                b.push(BASE64.charAt(((a[1] & 15) << 2) | (a[2] >> 6)));
                b.push(BASE64.charAt(a[2] & 63))
            }
        }
    }
    return b.join("")
}

function decodeBase64(f) {
    var e = [];
    var g = [];
    for (var b = 0; b < f.length; b += 4) {
        g.length = 0;
        for (var a = b; a < b + 4; ++a) {
            var d = f.charAt(a);
            if (d == "=" || d == "") {
                break
            }
            var c = BASE64.indexOf(d);
            if (c >= 64) {
                break
            }
            g.push(c)
        }
        if (g.length == 1) {
        } else {
            if (g.length == 2) {
                e.push((g[0] << 2) | (g[1] >> 4))
            } else {
                if (g.length == 3) {
                    e.push((g[0] << 2) | (g[1] >> 4));
                    e.push(((g[1] & 15) << 4) | (g[2] >> 2))
                } else {
                    if (g.length == 4) {
                        e.push((g[0] << 2) | (g[1] >> 4));
                        e.push(((g[1] & 15) << 4) | (g[2] >> 2));
                        e.push(((g[2] & 3) << 6) | (g[3]))
                    }
                }
            }
        }
    }
    return e
}

function encodeBase64(d) {
    var c = "";
    for (var a = 0; a < d.length; a += 3) {
        var b = d.slice(a, a + 3);
        c += encodeBase64Helper(b)
    }
    return c
}

function decodeQuotedPrintableHelper(d, b) {
    var c = [];
    for (var a = 0; a < d.length;) {
        if (d.charAt(a) == b) {
            c.push(parseInt(d.substr(a + 1, 2), 16));
            a += 3
        } else {
            c.push(d.charCodeAt(a));
            ++a
        }
    }
    return c
}

function decodeQuotedPrintable(a) {
    a = a.replace(/_/g, " ");
    return decodeQuotedPrintableHelper(a, "=")
}

function decodeUrl(a) {
    return decodeQuotedPrintableHelper(a, "%")
}

function encodeQuotedPrintableHelper(g, e, b) {
    var f = "";
    var a = e.charCodeAt(0);
    for (var c = 0; c < g.length; ++c) {
        var d = g[c];
        if (b(d)) {
            f += e + formatNumber(g[c], 16, 2)
        } else {
            f += String.fromCharCode(d)
        }
    }
    return f
}

function encodeQuotedPrintable(b) {
    var a = function (c) {
        return c < 32 || c > 126 || c == EQUAL_SIGN || c == QUESTION_MARK
    };
    return encodeQuotedPrintableHelper(b, "=", a)
}

var URL_SAFE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_.-";

function encodeUrl(b) {
    var a = function (c) {
        return URL_SAFE.indexOf(String.fromCharCode(c)) == -1
    };
    return encodeQuotedPrintableHelper(b, "%", a)
}

function convertUtf16CodesToString(b) {
    var c = "";
    for (var a = 0; a < b.length; ++a) {
        c += String.fromCharCode(b[a])
    }
    return c
}

function convertUnicodeCodePointsToString(a) {
    var b = convertUnicodeCodePointsToUtf16Codes(a);
    return convertUtf16CodesToString(b)
}

function maybeInitMaps(a, f, g) {
    if (f.is_initialized) {
        return
    }
    var e = ["ROUNDTRIP", "INPUT_ONLY", "OUTPUT_ONLY"];
    for (var d = 0; d < e.length; ++d) {
        var k = e[d];
        var m = a[k];
        var l = decodeBase64(m);
        for (var c = 0; c < l.length; c += 4) {
            var b = (l[c] << 8) | l[c + 1];
            var h = (l[c + 2] << 8) | l[c + 3];
            if (d == 0 || d == 1) {
                f[b] = h
            }
            if (d == 0 || d == 2) {
                g[h] = b
            }
        }
    }
    f.is_initialized = true
}

var SJIS_TO_UNICODE = {};
var UNICODE_TO_SJIS = {};

function maybeInitSjisMaps() {
    maybeInitMaps(SJIS_MAP_ENCODED, SJIS_TO_UNICODE, UNICODE_TO_SJIS)
}

var ISO88591_TO_UNICODE = {};
var UNICODE_TO_ISO88591 = {};

function maybeInitIso88591Maps() {
    maybeInitMaps(ISO88591_MAP_ENCODED, ISO88591_TO_UNICODE, UNICODE_TO_ISO88591)
}

function lookupMapWithDefault(d, b, a) {
    var c = d[b];
    if (!c) {
        c = a
    }
    return c
}

function convertUnicodeCodePointsToSjisBytes(a) {
    maybeInitSjisMaps();
    var d = [];
    for (var c = 0; c < a.length; ++c) {
        var e = a[c];
        var b = lookupMapWithDefault(UNICODE_TO_SJIS, e, QUESTION_MARK);
        if (b <= 255) {
            d.push(b)
        } else {
            d.push(b >> 8);
            d.push(b & 255)
        }
    }
    return d
}

function convertUnicodeCodePointsToEucJpBytes(a) {
    maybeInitSjisMaps();
    var g = [];
    for (var e = 0; e < a.length; ++e) {
        var f = a[e];
        var d = lookupMapWithDefault(UNICODE_TO_SJIS, f, QUESTION_MARK);
        if (d > 255) {
            var c = convertSjisCodeToJisX208Code(d);
            var b = c | 32896;
            g.push(b >> 8);
            g.push(b & 255)
        } else {
            if (d >= 128) {
                g.push(142);
                g.push(d)
            } else {
                g.push(d)
            }
        }
    }
    return g
}

function convertUnicodeCodePointsToIso88591Bytes(b) {
    maybeInitIso88591Maps();
    var a = [];
    for (var c = 0; c < b.length; ++c) {
        var e = b[c];
        var d = lookupMapWithDefault(UNICODE_TO_ISO88591, e, QUESTION_MARK);
        a.push(d)
    }
    return a
}

function convertSjisBytesToUnicodeCodePoints(e) {
    maybeInitSjisMaps();
    var a = [];
    for (var c = 0; c < e.length;) {
        var b = -1;
        var g = e[c];
        if ((g >= 129 && g <= 159) || (g >= 224 && g <= 252)) {
            ++c;
            var d = e[c];
            if ((d >= 64 && d <= 126) || (d >= 128 && d <= 252)) {
                b = (g << 8) | d;
                ++c
            }
        } else {
            b = g;
            ++c
        }
        var f = lookupMapWithDefault(SJIS_TO_UNICODE, b, QUESTION_MARK);
        a.push(f)
    }
    return a
}

function convertIso88591BytesToUnicodeCodePoints(a) {
    maybeInitIso88591Maps();
    var b = [];
    for (var c = 0; c < a.length; ++c) {
        var d = a[c];
        var e = lookupMapWithDefault(ISO88591_TO_UNICODE, d, QUESTION_MARK);
        b.push(e)
    }
    return b
}

function convertJisX208CodeToSjisCode(c) {
    var e = c >> 8;
    var d = c & 255;
    var b = ((e - 1) >> 1) + ((e <= 94) ? 113 : 177);
    var a = d + ((e & 1) ? ((d < 96) ? 31 : 32) : 126);
    return (b << 8) | a
}

function convertSjisCodeToJisX208Code(c) {
    var b = c >> 8;
    var a = c & 255;
    var e = (b << 1) - (b <= 159 ? 224 : 352) - (a < 159 ? 1 : 0);
    var d = a - 31 - (a >= 127 ? 1 : 0) - (a >= 159 ? 94 : 0);
    return (e << 8) | d
}

function convertJisX208BytesToSjisBytes(e) {
    var d = [];
    for (var c = 0; c < e.length; c += 2) {
        var b = (e[c] << 8) | e[c + 1];
        var a = convertJisX208CodeToSjisCode(b);
        d.push(a >> 8);
        d.push(a & 255)
    }
    return d
}

function convertSjisBytesToJisX208Bytes(d) {
    var e = [];
    for (var c = 0; c < d.length; c += 2) {
        var b = (d[c] << 8) | d[c + 1];
        var a = convertSjisCodeToJisX208Code(b);
        e.push(a >> 8);
        e.push(a & 255)
    }
    return e
}

var ASCII = 0;
var JISX201 = 1;
var JISX208 = 2;
var ESCAPE_SEQUENCE_TO_MODE = {"(B": ASCII, "(J": JISX201, "$B": JISX208, "$@": JISX208};
var MODE_TO_ESCAPE_SEQUENCE = {};
MODE_TO_ESCAPE_SEQUENCE[ASCII] = "(B";
MODE_TO_ESCAPE_SEQUENCE[JISX201] = "(J";
MODE_TO_ESCAPE_SEQUENCE[JISX208] = "$B";

function convertIso2022JpBytesToUnicodeCodePoints(c) {
    maybeInitSjisMaps();
    var a = function (m, n, j) {
        var h = [];
        if (m == ASCII) {
            h = n
        } else {
            if (m == JISX201) {
                h = convertSjisBytesToUnicodeCodePoints(n)
            } else {
                if (m == JISX208) {
                    var l = convertJisX208BytesToSjisBytes(n);
                    h = convertSjisBytesToUnicodeCodePoints(l)
                } else {
                }
            }
        }
        for (var k = 0; k < h.length; ++k) {
            j.push(h[k])
        }
        n.length = 0
    };
    var b = [];
    var g = ASCII;
    var f = [];
    for (var d = 0; d < c.length;) {
        if (c[d] == 27) {
            a(g, f, b);
            ++d;
            var e = String.fromCharCode(c[d], c[d + 1]);
            g = ESCAPE_SEQUENCE_TO_MODE[e];
            if (!g) {
                g = ASCII
            }
            d += 2
        } else {
            f.push(c[d]);
            ++d
        }
    }
    a(g, f, b);
    return b
}

function convertEucJpBytesToUnicodeCodePoints(g) {
    maybeInitSjisMaps();
    var a = [];
    for (var e = 0; e < g.length;) {
        if (g[e] >= 128 && (e + 1) < g.length && g[e + 1] >= 128) {
            var d = (g[e] << 8) | g[e + 1];
            var c = d & 32639;
            var b = convertJisX208CodeToSjisCode(c);
            var f = lookupMapWithDefault(SJIS_TO_UNICODE, b, QUESTION_MARK);
            a.push(f);
            e += 2
        } else {
            if (g[e] < 128) {
                a.push(g[e])
            } else {
            }
            ++e
        }
    }
    return a
}

function convertUnicodeCodePointsToIso2022JpBytes(a) {
    maybeInitSjisMaps();
    var g = ASCII;
    var f = function (i) {
        if (g != i) {
            g = i;
            var k = MODE_TO_ESCAPE_SEQUENCE[g];
            var j = convertStringToUnicodeCodePoints(k);
            b.push(27);
            b = b.concat(j)
        }
    };
    var b = [];
    for (var e = 0; e < a.length; ++e) {
        var h = a[e];
        var d = lookupMapWithDefault(UNICODE_TO_SJIS, h, QUESTION_MARK);
        if (d > 255) {
            var c = convertSjisCodeToJisX208Code(d);
            f(JISX208);
            b.push(c >> 8);
            b.push(c & 255)
        } else {
            if (d >= 128) {
                f(JISX201);
                b.push(d)
            } else {
                f(ASCII);
                b.push(d)
            }
        }
    }
    f(ASCII);
    return b
}

var MIME_FULL_MATCH = /^=\?([^?]+)\?([BQ])\?([^?]+)\?=$/;
var MIME_PARTIAL_MATCH = /^=\?([^?]+)\?([BQ])\?([^?]+)\?=/;

function isMimeEncodedString(a) {
    return a.match(MIME_FULL_MATCH) != null
}

function decodeMime(e) {
    var a = e.match(MIME_FULL_MATCH);
    if (a) {
        var f = a[1];
        f = f.replace(/\*.*$/, "");
        var b = a[2];
        var c = a[3];
        var d;
        if (b == "B") {
            d = decodeBase64(c)
        } else {
            if (b == "Q") {
                d = decodeQuotedPrintable(c)
            }
        }
        if (f != "" && d) {
            return [f, d]
        }
    }
    return []
}

var OUTPUT_CONVERTERS = {
    ISO2022JP: convertUnicodeCodePointsToIso2022JpBytes,
    ISO88591: convertUnicodeCodePointsToIso88591Bytes,
    SHIFTJIS: convertUnicodeCodePointsToSjisBytes,
    EUCJP: convertUnicodeCodePointsToEucJpBytes,
    UTF8: convertUnicodeCodePointsToUtf8Bytes
};
var INPUT_CONVERTERS = {
    ISO2022JP: convertIso2022JpBytesToUnicodeCodePoints,
    ISO88591: convertIso88591BytesToUnicodeCodePoints,
    SHIFTJIS: convertSjisBytesToUnicodeCodePoints,
    EUCJP: convertEucJpBytesToUnicodeCodePoints,
    UTF8: convertUtf8BytesToUnicodeCodePoints
};

function convertUnicodeCodePointsToBytes(b, d) {
    var c = normalizeEncodingName(d);
    var a = OUTPUT_CONVERTERS[c];
    if (a) {
        return a(b)
    }
    return []
}

function convertBytesToUnicodeCodePoints(d, c) {
    var b = normalizeEncodingName(c);
    var a = INPUT_CONVERTERS[b];
    if (a) {
        return a(d)
    }
    return []
}

function escapeToUtf16(d) {
    var c = "";
    for (var a = 0; a < d.length; ++a) {
        var b = d.charCodeAt(a).toString(16).toUpperCase();
        c += "\\u" + "0000".substr(b.length) + b
    }
    return c
}

function escapeToUtf32(e) {
    var d = "";
    var a = convertStringToUnicodeCodePoints(e);
    for (var b = 0; b < a.length; ++b) {
        var c = a[b].toString(16).toUpperCase();
        d += "\\U" + "00000000".substr(c.length) + c
    }
    return d
}

function escapeToNumRef(h, f) {
    var b = convertStringToUnicodeCodePoints(h);
    var g = "";
    var e = f == 10 ? "" : "x";
    for (var c = 0; c < b.length; ++c) {
        var d = b[c].toString(f).toUpperCase();
        var a = "&#" + e + d + ";";
        g += a
    }
    return g
}

function escapeToPunyCode(b) {
    var a = convertStringToPunyCodes(b);
    return convertUnicodeCodePointsToString(a)
}

function convertBytesToEscapedString(g, e) {
    var f = "";
    for (var c = 0; c < g.length; ++c) {
        var d = (e == 16 ? "\\x" : "\\");
        var b = e == 16 ? 2 : 3;
        var a = d + formatNumber(g[c], e, b);
        f += a
    }
    return f
}

function convertStringToPunyCodes(d) {
    var c = convertStringToUnicodeCodePoints(d);
    var b = [];
    var a = "";
    if (PunyCode.encode(c, b)) {
        return b
    }
    return c
}

function convertPunyCodesToString(a) {
    var b = [];
    if (PunyCode.decode(a, b)) {
        return convertUnicodeCodePointsToString(b)
    }
    return convertUnicodeCodePointsToString(a)
}

function escapeToEscapedBytes(d, c, b) {
    var a = convertStringToUnicodeCodePoints(d);
    var e = convertUnicodeCodePointsToBytes(a, b);
    return convertBytesToEscapedString(e, c)
}

function escapeToBase64(c, b) {
    var a = convertStringToUnicodeCodePoints(c);
    var d = convertUnicodeCodePointsToBytes(a, b);
    return encodeBase64(d)
}

function escapeToQuotedPrintable(c, b) {
    var a = convertStringToUnicodeCodePoints(c);
    var d = convertUnicodeCodePointsToBytes(a, b);
    return encodeQuotedPrintable(d)
}

function escapeToUrl(c, b) {
    var a = convertStringToUnicodeCodePoints(c);
    var d = convertUnicodeCodePointsToBytes(a, b);
    return encodeUrl(d)
}

function escapeToMime(d, a, f) {
    var b = convertStringToUnicodeCodePoints(d);
    var e = convertUnicodeCodePointsToBytes(b, f);
    if (d == "") {
        return ""
    }
    var c = "=?" + f + "?";
    if (a == "base64") {
        c += "B?";
        c += encodeBase64(e)
    } else {
        c += "Q?";
        c += encodeQuotedPrintable(e)
    }
    c += "?=";
    return c
}

function unescapeFromUtf16(b) {
    var a = convertEscapedUtf16CodesToUtf16Codes(b);
    return convertUtf16CodesToString(a)
}

function unescapeFromUtf32(c) {
    var a = convertEscapedUtf32CodesToUnicodeCodePoints(c);
    var b = convertUnicodeCodePointsToUtf16Codes(a);
    return convertUtf16CodesToString(b)
}

function unescapeFromEscapedBytes(d, c, b) {
    var e = convertEscapedBytesToBytes(d, c);
    var a = convertBytesToUnicodeCodePoints(e, b);
    return convertUnicodeCodePointsToString(a)
}

function unescapeFromNumRef(c, b) {
    var a = convertNumRefToUnicodeCodePoints(c, b);
    return convertUnicodeCodePointsToString(a)
}

function unescapeFromPunyCode(b) {
    var a = convertStringToUnicodeCodePoints(b);
    return convertPunyCodesToString(a)
}

function unescapeFromBase64(d, b) {
    var c = decodeBase64(d);
    var a = convertBytesToUnicodeCodePoints(c, b);
    return convertUnicodeCodePointsToString(a)
}

function unescapeFromQuotedPrintable(d, b) {
    var c = decodeQuotedPrintable(d);
    var a = convertBytesToUnicodeCodePoints(c, b);
    return convertUnicodeCodePointsToString(a)
}

function unescapeFromUrl(d, b) {
    var c = decodeUrl(d);
    var a = convertBytesToUnicodeCodePoints(c, b);
    return convertUnicodeCodePointsToString(a)
}

function isEmptyOrSequenceOfWhiteSpaces(c) {
    for (var a = 0; a < c.length; ++a) {
        var b = c.charCodeAt(a);
        if (!(b == 9 || b == 10 || b == 13 || b == 32)) {
            return false
        }
    }
    return true
}

function splitMimeString(d) {
    var c = [];
    var b = "";
    while (d != "") {
        var a = d.match(MIME_PARTIAL_MATCH);
        if (a) {
            if (!isEmptyOrSequenceOfWhiteSpaces(b)) {
                c.push(b)
            }
            b = "";
            c.push(a[0]);
            d = d.substr(a[0].length)
        } else {
            b += d.charAt(0);
            d = d.substr(1)
        }
    }
    if (!isEmptyOrSequenceOfWhiteSpaces(b)) {
        c.push(b)
    }
    return c
}

function normalizeEncodingName(a) {
    return a.toUpperCase().replace(/[_-]/g, "")
}

function unescapeFromMime(g) {
    var e = splitMimeString(g);
    var d = "";
    for (var b = 0; b < e.length; ++b) {
        if (isMimeEncodedString(e[b])) {
            var f = decodeMime(e[b]);
            if (f.length == 0) {
                continue
            }
            var c = normalizeEncodingName(f[0]);
            var h = f[1];
            var a = convertBytesToUnicodeCodePoints(h, c);
            d += convertUnicodeCodePointsToString(a)
        } else {
            d += e[b]
        }
    }
    return d
};
