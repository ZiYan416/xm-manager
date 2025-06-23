// 全局正则表达式方法

// 仅数字
export function numInput(input) {
    const regex = /^\d+$/; // 仅允许数字
    return regex.test(input);
}

// 名称空格限制
export function noblankInput(input) {
    // 匹配规则：不能以空格开头或结尾，英文和数字之间可以有单个空格，中文字符之间不能有空格
    const regex = /^(?! )(?:[A-Za-z0-9]+(?: [A-Za-z0-9]+)*)*$|^(?! )[\u4e00-\u9fa5]+(?! )$/;
    return regex.test(input);
}

export function validateMax100Chars(input) {
    // 匹配规则：允许任意字符但不超过 100 个字符
    const regex = /^.{0,100}$/;
    return regex.test(input);
}

export function validateCodeFormat(input) {
    // 匹配规则：以1-3个大写字母开头，后跟最多10位数字
    const regex = /^[A-Z]{1,3}\d{1,10}$/;
    return regex.test(input);
}

export function validateNoChinese(input) {
    // 匹配规则：不允许包含中文字符
    const regex = /^[^\u4e00-\u9fa5]*$/;
    return regex.test(input);
}

export function validateAlphanumeric(input) {
    // 匹配规则：仅允许数字和英文字符（包括大小写字母）
    const regex = /^[A-Za-z0-9]+$/;
    return regex.test(input);
}

export function validateUsername(input) {
    // 匹配规则：6-12位，仅允许大小写字母、数字和下划线
    const regex = /^[A-Za-z0-9_]{6,12}$/;
    return regex.test(input);
}

export function validatePassword(input) {
    // 匹配规则：6-12位，必须包含大/小写字母、符号、数字中的至少两个
    const hasLower = /[a-z]/.test(input); // 包含小写字母
    const hasUpper = /[A-Z]/.test(input); // 包含大写字母
    const hasDigit = /[0-9]/.test(input); // 包含数字
    const hasSymbol = /[\!\@\#\$\%\^\&\*\(\)\-\=\+\[\]\{\}\|\\\:\"\;\'\,\.\<\>\/\?\~\`]/.test(input); // 包含符号

    const types = [hasLower, hasUpper, hasDigit, hasSymbol].filter(Boolean).length;

    // 验证长度和类型
    return input.length >= 6 && input.length <= 12 && types >= 2;
}

// 验证手机号码（中国大陆手机号）
export function validatePhone(input) {
    // 匹配规则：中国大陆手机号，以13/14/15/16/17/18/19开头，共11位数字
    const regex = /^(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])\d{8}$/;
    return regex.test(input);
}

// 验证邮箱地址
export function validateEmail(input) {
    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return regex.test(input);
}

// 验证昵称（小于12位）
export function validateNickname(input) {
    const regex = /^.{1,12}$/; // 匹配规则：最多12位
    return regex.test(input);
}

// 验证网址格式
export function validateUrl(input) {
    // 网址正则表达式
    const regex = /^(https?:\/\/)?([\da-z\.-]+\.[a-z\.]{2,6}|[\d\.]+)([\/\w \.-]*)*\/?$/;
    return regex.test(input);
}
