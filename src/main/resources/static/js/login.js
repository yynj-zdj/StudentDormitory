/**
 * 初始化验证码
 */
function initCaptcha() {
    $("#captchaImg").attr("src", "/captcha?t=" + new Date().getTime());
}

/**
 * 校验账户和密码录入非空，光标离开时非空提示
 * @param input
 */
function checkInputInfoNull(input) {
    if (input == 'account') {
        var account = $("#account").val();
        if (isNull(account)) {
            $("#accountCheckInfo").html("账户不能为空！");
        } else {
            $("#accountCheckInfo").html("");
        }
    } else if (input == 'password') {
        var password = $("#password").val();
        if (isNull(password)) {
            $("#passwordCheckInfo").html("密码不能为空！");
        } else {
            $("#passwordCheckInfo").html("");
        }
    }
}

/**
 * 生成随机的UUID
 */
function genUuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";
    var uuid = s.join("");
    return uuid;
}

/**
 * 判断字符串非空
 */
function isNull(str) {
    if (str === null || str === undefined || str === '' || str.replace(/(^\s*)|(\s*$)/g, '') === '') {
        return true;
    } else {
        return false;
    }
}


/**
 * 校验登录录入信息是否为空
 * @param account
 * @param password
 * @param type
 * @param captcha
 * @returns {string|null}
 */
function checkInputInfo(account, password, type, captcha) {
    if (isNull(account)) {
        return "账户不能为空！";
    }
    if (isNull(password)) {
        return "密码不能为空！";
    }
    if (isNull(type)) {
        return "请您选择登录类型！";
    }
    if (isNull(captcha)) {
        return "验证码不能为空！";
    }
    return null;
}

// 点击验证码图片刷新
$("#captchaImg").click(function() {
    initCaptcha();
});