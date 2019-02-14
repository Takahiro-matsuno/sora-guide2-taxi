function checkPassword() {
    console.log("きた");

    var checkNowNew = false;
    var checkNewCon = false;

    var nowValue = document.getElementById("nowPassword").value;
    var newValue = document.getElementById("newPassword").value;
    var conValue = document.getElementById("confirmPassword").value;

    var errorElement = document.getElementById("errorMessage");
    var message = "";

    // 現在・新パスワードの比較
    if (nowValue !== "" && newValue !== "") {
        if (nowValue !== newValue) {
            checkNowNew = true;
        } else {
            message = "同じパスワードは設定できません。";
            console.log(message);
        }
    }
    // 新・確認パスワード
    if (newValue !== "" && conValue !== "") {
        if (newValue === conValue) {
            checkNewCon = true;
        } else if (message === "") {
            message = "新しいパスワードと確認用のパスワードが一致しません。";
            console.log(message);
        }
    }

    errorElement.innerText = message;
    return checkNowNew && checkNewCon;
}