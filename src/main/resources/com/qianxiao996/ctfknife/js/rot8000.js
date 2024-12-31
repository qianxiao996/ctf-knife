function rotTransform(inputString, rotationValue) {
    // 如果没有提供rotationValue，则使用默认值 0x8000
    if (typeof rotationValue === 'undefined') {
        rotationValue = 0x8000;
    }

    var whitespace = /\s/;
    var tests = [whitespace];

    function isValid(char) {
        for (var i = 0; i < tests.length; i++) {
            if (tests[i].test(char)) {
                return false;
            }
        }
        return true;
    }

    function rot(codePoint) {
        return codePoint < rotationValue
            ? codePoint + rotationValue
            : codePoint - rotationValue;
    }

    var len = inputString.length;
    var result = [];

    for (var x = 0; x < len; x++) {
        var y = inputString[x];
        var v = isValid(y);
        var z = inputString.charCodeAt(x); // 使用charCodeAt代替codePointAt以保证兼容性
        result.push(v ? rot(z) : z);
    }

    // 使用Array.prototype.map和String.fromCharCode来构建结果字符串
    return String.fromCharCode.apply(String, result);
}