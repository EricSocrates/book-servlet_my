<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>添加类别</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%--发送同步请求添加书籍类别--%>
<form class="form-horizontal" role="form" method="post" action="book">
    <input type="hidden" name="method" value="addCategory"/>
    <div class="form-group">
        <label for="name" class="col-sm-2 control-label">类别名字</label>
        <div class="col-sm-3">
            <input type="text" class="form-control" id="name" name="name" onblur="checkName(this.value)"
                   placeholder="请输入类别名字" autocomplete="off">
        </div>
        <div class="col-sm-5">
            <span id="msg"></span>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-primary">添加</button>
        </div>
    </div>
</form>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="js/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
<script>
    function checkName(name) {
        if (name != "") {
            //$.ajax({type:"POST"});
            //简化版ajax的post请求参数列表： url      data         success      dataType
            $.post("book", "method=checkCategoryName&name=" + name, function (msg) {
                console.log(msg);
                if (msg.flag == "success") {
                    $("#msg").text("类名可用");
                    $("button[type='submit']").attr("disabled", false);
                } else {
                    $("#msg").text("类名重复");
                    $("button[type='submit']").attr("disabled", true);
                }
            }, "json");
        }
    }
</script>
</body>
</html>