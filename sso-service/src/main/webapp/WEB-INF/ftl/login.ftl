<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-store">
    <meta http-equiv="Cache-Control" content="must-revalidate">
    <title>登录页面</title>
</head>
<body>
<h1>欢迎登录</h1>

<div>
    <p style="color: red;">${msg}</p>
    <form id="login" action="login" method="POST">
        <label>用户名：</label><input type="text" id="username" name="username"/>
        <label>密码：</label><input type="text" id="password" name="password"/>
        <input type="submit" value="登录"/>
    </form>
</div>

<script type="text/javascript">
    var clientURL = window.location.search;                             // 获取浏览器地址栏上的参数（就是客户端的URL）
    document.getElementById("login").action="login" + clientURL;        // 添加到form表单上的action属性上
</script>
</body>
</html>