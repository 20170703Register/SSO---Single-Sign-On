<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-store">
    <meta http-equiv="Cache-Control" content="must-revalidate">
    <title>欢迎页面</title>
</head>
<body>
<h1>登录成功！</h1>
<p>Hi! ${username}</p>

<div>
    <button type="button" onclick="logout()">用户登出</button>
</div>

<script>
    /**
     * 用户登出
     * */
    var logout = function(){
        var url = "logout";
        window.location.href = url;
    }
</script>
</body>
</html>