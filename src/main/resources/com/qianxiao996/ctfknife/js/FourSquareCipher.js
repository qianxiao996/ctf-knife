function encode(input, _keySquare1, _keySquare2) {

    if (!input || input.length < 1) {
        return;
    }
    // var plaintext = input.toLowerCase().replace(/[^a-z]/g, "").replace(/[j]/g, "i");
    // var k1 = _keySquare1.toLowerCase().replace(/[^a-z]/g, "");
    // var k2 = _keySquare2.toLowerCase().replace(/[^a-z]/g, "");
    var plaintext = input.toLowerCase().replace(/[j]/g, "i");
    var k1 = _keySquare1.toLowerCase();
    var k2 = _keySquare2.toLowerCase();
  
    if (plaintext.length < 1) {
        return "请输入一些明文(仅限字母和数字)"
    }
    if (k1.length != 25 || k2.length != 25) {
        return "keyssquare的长度必须为25个字符"
    }
  
    if (plaintext.length % 2 == 1) {
        plaintext += "x";
    }
  
    var ciphertext = "";
    var rt = "abcdefghiklmnopqrstuvwxyz";
  
    for (var i = 0; i < plaintext.length; i += 2) {
        var a = (rt.indexOf(plaintext.charAt(i)) % 5);
        var b = Math.floor(rt.indexOf(plaintext.charAt(i)) / 5);
        var c = Math.floor(rt.indexOf(plaintext.charAt(i + 1)) % 5);
        var d = Math.floor(rt.indexOf(plaintext.charAt(i + 1)) / 5);
        ciphertext += k1.charAt(5 * b + c);
        ciphertext += k2.charAt(5 * d + a);
    }
  
    // return ciphertext.toUpperCase();
    return ciphertext;
  }
  
  function decode(input, _keySquare1, _keySquare2) {
    if (!input || input.length < 1) {
        return;
      }
      // var ciphertext = input.toLowerCase().replace(/[^a-z0-9]/g, "").replace(/[j]/g, "i");
      // var k1 = _keySquare1.toLowerCase().replace(/[^a-z]/g, "");
      // var k2 = _keySquare2.toLowerCase().replace(/[^a-z]/g, "");
      var ciphertext = input.toLowerCase().replace(/[i]/g, "j");
      var k1 = _keySquare1.toLowerCase();
      var k2 = _keySquare2.toLowerCase();
      if (ciphertext.length < 1) {
        return "请输入一些明文(仅限字母和数字)"
      }
      if (k1.length !== 25 || k2.length !== 25) {
        return "keyssquare的长度必须为25个字符"
      }
      if (ciphertext.length % 2 === 1) {
        return "密文应该是偶数长度！"
      }
    
      var plaintext = "";
      var rt = "abcdefghiklmnopqrstuvwxyz";
    
      for (var i = 0; i < ciphertext.length; i += 2) {
        var a = k1.indexOf(ciphertext.charAt(i)) % 5;
        var b = Math.floor(k1.indexOf(ciphertext.charAt(i)) / 5);
        var c = k2.indexOf(ciphertext.charAt(i + 1)) % 5;
        var d = Math.floor(k2.indexOf(ciphertext.charAt(i + 1)) / 5);
        plaintext += rt.charAt(5 * b + c);
        plaintext += rt.charAt(5 * d + a);
      }
    //   return plaintext.toUpperCase();
      return plaintext;
  }
  
  
  
  // var input = "youngandsuccessful";
  // var a= encode(input,"securitysecuritysecuritys","informationinformationinf")
  // console.log(a); 

  // var input = "YFEOINIFUACFCIYTYO";
  // var a= decode(input,"securitysecuritysecuritys","informationinformationinf")
  // console.log(a); 
