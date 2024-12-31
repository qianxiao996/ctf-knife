// JavaScript Document
$("#error-alert").hide();
$("#copy-alert").hide();
String.prototype.replaceAll = function (s1, s2) {
	var reg = new RegExp(s1, "g");
	return this.replace(reg, s2);
}

function encrypt() {
	var msg = $("#text-decryped").val();
	var key = $("#text-key").val();

	if (msg.length < 1) {
		$("#error-alert").show();
		$("#copy-alert").hide();
		$("#error-alert").text("请输入待加密的明文!");
	} else {
		if (key.length < 1) {
			key = "";
		}
		$("#text-encryped").val(enc_encrypt(msg, key));
		$("#error-alert").hide();
		$("#copy-alert").hide();
	}

}

function decrypt() {
	var msg = $("#text-decryped").val();
	var key = $("#text-key").val();

	if (msg.length < 1) {
		$("#error-alert").show();
		$("#copy-alert").hide();
		$("#error-alert").text("请输入待解密的密文!");
	} else {
		if (key.length < 1) {
			key = "";
		}
		try {
			$("#error-alert").hide();
			var str = enc_decrypt(msg, key);
		} catch (err) {
			$("#error-alert").show();
			$("#copy-alert").hide();
			$("#error-alert").text("解密失败！");
		} finally {
			$("#text-encryped").val(str);
		}

	}

}


function copyUrl2() {
	var Url2 = document.getElementById("text-encryped");
	Url2.select();
	document.execCommand("Copy");
	$("#copy-alert").show();
	$("#error-alert").hide();
	// Hide the alert after 3 seconds
	setTimeout(function() {
		$("#copy-alert").hide();
	}, 3000);
}

function enc_encrypt(plaintext, key) {
	ciphertext = CryptoJS.Rabbit.encrypt(plaintext, key).toString();
	return ciphertext;
}

function enc_decrypt(ciphertext, key) {
	var bytes = CryptoJS.Rabbit.decrypt(ciphertext, key);
	var decryptedtext = bytes.toString(CryptoJS.enc.Utf8);
	return decryptedtext;
}
