function assert(express){
    var l = express.length;
    var msg = (typeof express[l-1] === 'string')? express[l-1]: 'Assert Error';
    for(var b in express){
        if(!express[b]){
            throw new Error(msg);
        }
    }
}

function randBin(){
    return Math.random() >= 0.5;
}

var values = '富强民主文明和谐自由平等公正法治爱国敬业诚信友善';

function str2utf8(str){
    // return in hex

    var notEncoded = /[A-Za-z0-9\-\_\.\!\~\*\'\(\)]/g;
    var str1 = str.replace(notEncoded, function(c) { return c.codePointAt(0).toString(16); });
    // var str1 = str.replace(notEncoded, c=>c.codePointAt(0).toString(16));
    var str2 = encodeURIComponent(str1);
    var concated = str2.replace(/%/g, '').toUpperCase();
    return concated;
}

function utf82str(utfs){
    assert(typeof utfs === 'string', 'utfs Error');

    var l = utfs.length;

    assert((l & 1) === 0);

    var splited = [];

    for(var i = 0; i < l; i++){
        if((i & 1) === 0){
            splited.push('%');
        }
        splited.push(utfs[i]);
    }

    return decodeURIComponent(splited.join(''));
}

function hex2duo(hexs){
    // duodecimal in array of number

    // '0'.. '9' -> 0.. 9
    // 'A'.. 'F' -> 10, c - 10    a2fFlag = 10
    //          or 11, c - 6      a2fFlag = 11
    assert(typeof hexs === 'string')

    var duo = [];

    for(var c in hexs){
        // var n = (hexs[c]).toString(16);

        var n = parseInt(hexs[c], 16);
        // var n = Number.parseInt(hexs[c], 16);
        if(n < 10){
            duo.push(n);
        }else{
            if(randBin()){
                duo.push(10);
                duo.push(n - 10);
            }else{
                duo.push(11);
                duo.push(n - 6);
            }
        }
    }
    return duo;
}

function duo2hex(duo){
    assert(duo instanceof Array);

    var hex = [];

    var l = duo.length;

    var i = 0;

    while(i < l){
        if(duo[i] < 10){
            hex.push(duo[i]);
        }else{
            if(duo[i] === 10){
                i++;
                hex.push(duo[i] + 10);
            }else{
                i++;
                hex.push(duo[i] + 6);
            }
        }
        i++;
    }
    var a=""
    for(var i = 0; i < hex.length; i++) {
        a+=(hex[i].toString(16).toUpperCase())
    }
    return a;
    
    // return hex.map(v=>v.toString(16).toUpperCase()).join('');
}


function duo2values(duo){
    var a=""
    for(var i = 0; i < duo.length; i++) {
        a+=(values[2*duo[i]]+values[2*duo[i]+1])
    }

    return a;
    // return duo.map(d=>values[2*d]+values[2*d+1]).join('');
}

function valuesDecode(encoded){
    var duo = [];

    for(var c in encoded){
        var i = values.indexOf(encoded[c]);
        if(i === -1){
            continue;
        }else if(i & 1){
            continue;
        }else{
            // i is even
            duo.push(i >> 1);
        }
    }
    
    var hexs = duo2hex(duo);

    assert((hexs.length & 1) === 0);

    var str;
    try{
        str = utf82str(hexs);
    }catch(e){
        throw e;
    }
    return str;
}


function valuesEncode(str){
    return duo2values(hex2duo(str2utf8(str)));
}

// ------------------



// var encoded = valuesEncode("公正民主公正自由法治和谐公正民主公正自由公正民主法治和谐");
// console.log(encoded);
// var decoded = valuesDecode("和谐民主和谐民主和谐民主和谐民主");
// console.log(decoded);