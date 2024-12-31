// JavaScript Document
$("#error-alert").hide();
$("#copy-alert").hide();

function encrypt() {
	var msg = $("#text-decryped").val();


	if (msg.length < 1) {
		$("#error-alert").show();
		$("#copy-alert").hide();
		$("#error-alert").text("请输入待加密的明文!");
	} else {
		var result = tadpole_dict.encodeTadpole(msg);
		$("#text-encryped").val(result);
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
		try {
			$("#error-alert").hide();
			var str = tadpole_dict.decode(msg);
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

var tadpole_dict = {
	decode: function(s){
		return s.replace(/ ?\/([ۖۗۘۙۚۛۜ۟۠ۡۢۤۧۨ۫۬]{10,}) ?|\[:([A-Za-z0-9\+\/]{2,})={0,2}:\]/g, tadpole_dict.decoder);
	},
	parseURL: function(str, p1, offset, s){
		return "<a href='" + p1.replace("&amp;", "&") + "'>" + p1 + "</a>";
	},
	parseIMG: function(str, p1, offset, s){
		return "<img src='" + p1.replace("&amp;", "&") + "'/>";
	},
	ba64S: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/",
	ba64O: {},
	randA: [1750,1751,1752,1753,1754,1755,1756,1759,1760,1761,1762,1764,1767,1768,1771,1772],
	randS: [],
	randO: {},
	initRand: function(){
		for (var i = 0; i < 16; ++i)
		{
			tadpole_dict.randS.push(String.fromCharCode(tadpole_dict.randA[i]));
			tadpole_dict.randO[tadpole_dict.randS[i]] = i;
		}
		for (i = 0; i < 64; ++i)
		{
			tadpole_dict.ba64O[tadpole_dict.ba64S[i]] = i;
		}
	},

	decoder: function(str, p1, p2, offset, s)
	{
		var rslt;
		if (str[0] == "[")
		{
			rslt = tadpole_dict.decodeB64(p2);
		}
		else
		{
			rslt = tadpole_dict.decodeTadpole(p1) + " ";
		}
		if (tadpole_dict.enablecolor)
		{
			rslt = rslt.replace(/\[color=(#[0-9a-fA-F]{6})\](.+?)\[\/color\]/g, "<font color='$1'>$2</font>");
		}
		if (tadpole_dict.enableurl)
		{
			rslt = rslt.replace(/\[url\]([^<>\[]+?)\[\/url\]/g, tadpole_dict.parseURL);
		}
		if (tadpole_dict.enableimg)
		{
			rslt = rslt.replace(/\[img\]([^<>\[]+?)\[\/img\]/g, tadpole_dict.parseIMG);
		}
		return rslt;
	},
	decodeTadpole: function(p1)
	{
		var b1 = tadpole_dict.tadpole2byte(p1);
		var n = b1[0];
		var m = b1[1];
		var n0 = n;
		var b0 = [];
		for (var i = 2; i < b1.length; ++i)
		{
			b0.push((b1[i] * 207 +512 - n - m) & 0xFF);
			n = b1[i];
		}
		if (tadpole_dict.checksum(b0) != (n0 << 8) + m)
		{
			return "/'";
		}
		return tadpole_dict.byte2u16(b0, true);
	},
	decodeB64: function(p2)
	{
		var b0 = tadpole_dict.b642byte(p2);
		return tadpole_dict.byte2u16(b0, false);
	},
	encodeTadpole: function(str)
	{
		var b0 = tadpole_dict.u162byte(str, true);
		var sum = tadpole_dict.checksum(b0);
		var b1 = [];
			
		var m = sum & 0xFF;
		var n = sum >> 8;
		b1.push(n);
		b1.push(m);
		for (var i = 0; i < b0.length; ++i)
		{
			n = ((n + m + b0[i]) * 47) & 0xFF;
			b1.push(n);
		}
		return tadpole_dict.byte2tadpole(b1);	
	},
	encodeB64: function(str)
	{
		var b0 = tadpole_dict.u162byte(str, false);
		return "[:" + tadpole_dict.byte2b64(b0) + ":]";
	},
	tadpole2byte: function(s)
	{
		var b = [];
		var ah = -1;
		for (var i = 0; i < s.length; ++i)
		{
			if (ah >= 0)
			{
				b.push(tadpole_dict.randO[s[i]] + ah);
				ah = -1;
			}
			else
			{
				ah = tadpole_dict.randO[s[i]] << 4
			}
		}
		return b;
	},
	byte2tadpole: function(b)
	{
		var s = " /";
		for (var i = 0; i < b.length; ++i)
		{
			s += tadpole_dict.randS[b[i] >> 4] + tadpole_dict.randS[b[i] & 15];	
		}
		return s + " ";
	},

	checksum: function(b)
	{
		var a0 = 0;
		var a1 = 0;
		for (var i = 0; i < b.length; ++i)
		{
			a0 = (a0 + b[i]) * 3001 % 5021;
			a1 = (a1 + a0 + b[i] ) * 3011 % 5011;
		}
		return ((a0 & 0xFF) | ((a1 & 0xFF) << 8)) ^ 22155;
	},
	b642byte: function(s)
	{
		var b = [];
		var k = 0;
		for (var i = 0; i < s.length; ++i)
		{
			var stat = i % 4;
			var j = tadpole_dict.ba64O[s[i]];
			if (stat == 0)
			{
				k = j << 2;
			}
			else if (stat == 1)
			{
				k |= j >> 4;
				b.push(k);
				k = (j & 15) << 4;
			}
			else if (stat == 2)
			{
				k |= j >> 2;
				b.push(k);
				k = (j & 3) << 6;
			}
			else if (stat == 3)
			{
				k |= j;
				b.push(k);
			}
		}
		return b;
	},
	byte2b64: function(b)
	{
		var s = "";
		var stat;
		var k = 0;
		for (var i = 0; i < b.length; ++i)
		{
			stat = i % 3;
			var j = b[i];
			if (stat == 0)
			{
				k = j >> 2;
				s += tadpole_dict.ba64S[k];
				k = (j & 3) << 4;
			}
			else if (stat == 1)
			{
				k |= j >> 4;
				s += tadpole_dict.ba64S[k];
				k = (j & 15) << 2;
			}
			else if (stat == 2)
			{
				k |= j >> 6;
				s += tadpole_dict.ba64S[k];
				k = j & 63;
				s += tadpole_dict.ba64S[k];
			}
		}
		if (stat == 0)
		{
			s += tadpole_dict.ba64S[k];
			s += "==";
		}
		else if (stat == 1)
		{
			s += tadpole_dict.ba64S[k];
			s += "=";
		}
		return s;
	},
	u162byte: function(s, header)
	{
		var b = [];
		if (header)
		{
			b = [0, 0];
		}
		for (var i = 0; i < s.length; ++i)
		{
			var j = s.charCodeAt(i);
			if (j >= 0xD800 && j < 0xE000)
			{
				j = (((j & 0x3FF) << 10) | (s.charCodeAt(i + i) & 0x3FF)) + 0x10000;
				++i;
			}
			if (j < 128)
			{
				b.push(j);
			}
			else if (j < 2048)
			{
				b.push(0xC0 | (j >> 6));
				b.push(0x80 | (j & 0x3F));
			}
			else if (j < 65536)
			{
				b.push(0xE0 | (j >> 12));
				b.push(0x80 | ((j >> 6) & 0x3F));
				b.push(0x80 | (j & 0x3F));
			}
			else
			{
				b.push(0xF0 | (j >> 18));
				b.push(0x80 | ((j >> 12) & 0x3F));
				b.push(0x80 | ((j >> 6) & 0x3F));
				b.push(0x80 | (j & 0x3F));
			}
		}
		if (header)
		{
			var len = b.length - 2;
			b[0] = len >> 8;
			b[1] = len & 0xFF;
		}
		return b;
	},
	byte2u16: function(b, header)
	{
		var s = "";
		var i = 0;
		if (header) i = 2; 
		for (; i < b.length; ++i)
		{
			if (b[i] == 9) {
				s += "    ";
			}
			else if (b[i] < 32) {
				s += "\n";
			}
			else if (b[i] == 32) {
				s += " ";
			}
			else if (b[i] == 38) {
				s += "&";
			}
			else if (b[i] == 60) {
				s += "<";
			}
			else if (b[i] == 62) {
				s += ">";
			}
			else if (b[i] < 128) {
				s += String.fromCharCode(b[i]);
			}
			else if (b[i] < 224) {
				s += String.fromCharCode(((b[i] & 31) << 6) | (b[i+1] & 63));
				++i;
			}
			else if (b[i] < 240) {
				s += String.fromCharCode(((b[i] & 15) << 12) | ((b[i+1] & 63) << 6) | (b[i+2] & 63));
				i += 2;
			}
			else {
				var j = (((b[i] & 7) << 18) | ((b[i+1] & 63) << 12) | ((b[i+2] & 63) << 6) | (b[i+3] & 63)) - 0x10000;
				s += String.fromCharCode(0xD800 | (j >> 10), 0xDC00 | (j & 1023 ));
				i += 3;	
			}
		}
		return s;
	}
};
tadpole_dict.initRand();
