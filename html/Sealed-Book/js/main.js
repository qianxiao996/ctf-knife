// JavaScript Document

$("#error-alert").hide();
$("#copy-alert").hide();

var password = "BlackCat184";

String.prototype.replaceAll = function(s1, s2) {
	var reg = new RegExp(s1, "g");
	return this.replace(reg, s2);
}

//加密
function encrypt() {
	var msg = $("#text-decryped").val();
	var key = $("#text-key").val();

	if (msg.length < 1) {
		$("#error-alert").show();
		$("#copy-alert").hide();
		$("#error-alert").text("汝无惑，天书亦无所解答。（请输入待加密的明文）");
	} else {
		if (key.length < 1) {
			key = password;
		}

		$("#text-encryped").val(togod(msg, key));
		$("#error-alert").hide();
		$("#copy-alert").hide();
	}

}

//解密
function decrypt() {
	var msg = $("#text-encryped").val();
	var key = $("#text-key").val();
	$("#error-alert").text(msg.length);

	if (msg.length < 1) {
		$("#error-alert").show();
		$("#copy-alert").hide();
		$("#error-alert").text("汝无惑，天书亦无所解答。（请输入待解密的密文）");
	} else {
		if (msg.substring(0, 2) != "曰：") {
			$("#error-alert").show();
			$("#copy-alert").hide();
			$("#error-alert").text("天书未曾收录此言（不是天书，请确定密文来源本工具并且密文以“曰：开头”）");
		} else {
			if (key.length < 1) {
				key = password;
			}
			try {
				$("#error-alert").hide();
				var str = toman(msg, key);
				if(str.length < 1){
					$("#error-alert").show();
					$("#copy-alert").hide();
					$("#error-alert").text("阁下可曾记得此为何真人所言？（天书有误，请确定密钥正确并未被篡改）");
				}
			} catch (err) {
				$("#error-alert").show();
				$("#copy-alert").hide();
				$("#error-alert").text("阁下可曾记得此为何真人所言？（天书有误，请确定密钥正确并未被篡改）");
			} finally {
				$("#text-decryped").val(str);

			}



		}


	}

}


function copyUrl2() {
	var Url2 = document.getElementById("text-encryped");
	Url2.select();
	document.execCommand("Copy");
	$("#copy-alert").show();
	$("#error-alert").hide();
}

function togod(msg, key) {
	var str = CryptoJS.AES.encrypt(msg, key).toString();

	str = str.substring(10);

	str = str.replaceAll("e", "渺");
	str = str.replaceAll("E", "莽");
	str = str.replaceAll("t", "茫");
	str = str.replaceAll("T", "乐");
	str = str.replaceAll("a", "生");
	str = str.replaceAll("A", "终");
	str = str.replaceAll("o", "仙");
	str = str.replaceAll("O", "鬼");
	str = str.replaceAll("i", "人");
	str = str.replaceAll("I", "吉");
	str = str.replaceAll("n", "凶");
	str = str.replaceAll("N", "清");
	str = str.replaceAll("s", "灵");
	str = str.replaceAll("S", "空");
	str = str.replaceAll("h", "命");
	str = str.replaceAll("H", "精");
	str = str.replaceAll("r", "炁");
	str = str.replaceAll("R", "神");
	str = str.replaceAll("d", "魔");
	str = str.replaceAll("D", "梵");
	str = str.replaceAll("l", "周");
	str = str.replaceAll("L", "量");
	str = str.replaceAll("c", "道");
	str = str.replaceAll("C", "天");
	str = str.replaceAll("u", "地");
	str = str.replaceAll("U", "荡");
	str = str.replaceAll("m", "度");
	str = str.replaceAll("M", "罗");
	str = str.replaceAll("w", "色");
	str = str.replaceAll("W", "元");
	str = str.replaceAll("f", "始");
	str = str.replaceAll("F", "玄");
	str = str.replaceAll("g", "御");
	str = str.replaceAll("G", "浩");
	str = str.replaceAll("y", "劫");
	str = str.replaceAll("Y", "虚");
	str = str.replaceAll("p", "界");
	str = str.replaceAll("P", "真");
	str = str.replaceAll("b", "实");
	str = str.replaceAll("B", "华");
	str = str.replaceAll("v", "威");
	str = str.replaceAll("V", "运");
	str = str.replaceAll("k", "魂");
	str = str.replaceAll("K", "魄");
	str = str.replaceAll("j", "融");
	str = str.replaceAll("J", "象");
	str = str.replaceAll("x", "霄");
	str = str.replaceAll("X", "冥");
	str = str.replaceAll("q", "照");
	str = str.replaceAll("Q", "净");
	str = str.replaceAll("z", "微");
	str = str.replaceAll("Z", "幽");
	str = str.replaceAll("0", "观");
	str = str.replaceAll("1", "陀");
	str = str.replaceAll("2", "阿");
	str = str.replaceAll("3", "龙");
	str = str.replaceAll("4", "阎");
	str = str.replaceAll("5", "东");
	str = str.replaceAll("6", "西");
	str = str.replaceAll("7", "南");
	str = str.replaceAll("8", "北");
	str = str.replaceAll("9", "玉");
	str = str.replaceAll("\\+", "太");
	str = str.replaceAll("/", "坤");
	str = str.replaceAll("=", "尊");

	return "曰：" + str;
}

function toman(msg, key) {

	str = msg.substring(2);

	str = str.replaceAll("渺", "e");
	str = str.replaceAll("莽", "E");
	str = str.replaceAll("茫", "t");
	str = str.replaceAll("乐", "T");
	str = str.replaceAll("生", "a");
	str = str.replaceAll("终", "A");
	str = str.replaceAll("仙", "o");
	str = str.replaceAll("鬼", "O");
	str = str.replaceAll("人", "i");
	str = str.replaceAll("吉", "I");
	str = str.replaceAll("凶", "n");
	str = str.replaceAll("清", "N");
	str = str.replaceAll("灵", "s");
	str = str.replaceAll("空", "S");
	str = str.replaceAll("命", "h");
	str = str.replaceAll("精", "H");
	str = str.replaceAll("炁", "r");
	str = str.replaceAll("神", "R");
	str = str.replaceAll("魔", "d");
	str = str.replaceAll("梵", "D");
	str = str.replaceAll("周", "l");
	str = str.replaceAll("量", "L");
	str = str.replaceAll("道", "c");
	str = str.replaceAll("天", "C");
	str = str.replaceAll("地", "u");
	str = str.replaceAll("荡", "U");
	str = str.replaceAll("度", "m");
	str = str.replaceAll("罗", "M");
	str = str.replaceAll("色", "w");
	str = str.replaceAll("元", "W");
	str = str.replaceAll("始", "f");
	str = str.replaceAll("玄", "F");
	str = str.replaceAll("御", "g");
	str = str.replaceAll("浩", "G");
	str = str.replaceAll("劫", "y");
	str = str.replaceAll("虚", "Y");
	str = str.replaceAll("界", "p");
	str = str.replaceAll("真", "P");
	str = str.replaceAll("实", "b");
	str = str.replaceAll("华", "B");
	str = str.replaceAll("威", "v");
	str = str.replaceAll("运", "V");
	str = str.replaceAll("魂", "k");
	str = str.replaceAll("魄", "K");
	str = str.replaceAll("融", "j");
	str = str.replaceAll("象", "J");
	str = str.replaceAll("霄", "x");
	str = str.replaceAll("冥", "X");
	str = str.replaceAll("照", "q");
	str = str.replaceAll("净", "Q");
	str = str.replaceAll("微", "z");
	str = str.replaceAll("幽", "Z");
	str = str.replaceAll("观", "0");
	str = str.replaceAll("陀", "1");
	str = str.replaceAll("阿", "2");
	str = str.replaceAll("龙", "3");
	str = str.replaceAll("阎", "4");
	str = str.replaceAll("东", "5");
	str = str.replaceAll("西", "6");
	str = str.replaceAll("南", "7");
	str = str.replaceAll("北", "8");
	str = str.replaceAll("玉", "9");
	str = str.replaceAll("太", "+");
	str = str.replaceAll("坤", "/");
	str = str.replaceAll("尊", "=");

	var st = CryptoJS.AES.decrypt("U2FsdGVkX1" + str, key).toString(CryptoJS.enc.Utf8);


	return st;
}