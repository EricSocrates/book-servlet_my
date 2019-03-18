<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>欢迎使用书籍管理系统</title>
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<!--页头 start-->
<%--<div class="page-header">
    <img src="image/timg.gif" width="100%" height="130px" />
</div>--%>
<!--页头 end-->
<!--导航-->
<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">书籍管理后台</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">首页</a></li>
            </ul>
            <%--已登录 待添加--%>
            <%--<c:if test="${admin != null}">--%>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="index_main.jsp">展示页</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle"
                       data-toggle="dropdown">欢迎:用户名<span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="javascript:void(0);"
                               data-toggle="modal" data-target="#myModal">个人信息</a></li>
                        <li class="divider"></li>
                        <li><a href="admin?method=logout">注销</a></li>
                    </ul>
                </li>
                <%--</ul>--%>
                <%--</c:if>--%>
                <%--未登录 待添加
                <c:if test="${admin == null}">
                  <ul class="nav navbar-nav navbar-right">
                    <li><a href="login.jsp">登录</a></li>
                    <li><a href="reg.jsp">注册</a></li>
                  </ul>
                </c:if>--%>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<!--导航end-->
<div class="container-fluid">
    <div class="row">
        <div class="col-md-3">
            <div class="list-group">
                <a href="welcome.jsp" target="test" class="list-group-item active">
                    书籍管理
                </a>
                <%--已登录--%>
                <%--<c:if test="${admin != null}">--%>
                <a href="addcategory.jsp" target="test" class="list-group-item">添加类别</a>
                <a href="addbook.jsp" target="test" class="list-group-item">添加书籍</a>
                <a href="booklist.jsp" target="test" class="list-group-item">书籍列表</a>
                <%--</c:if>--%>
            </div>

        </div>
        <div class="col-md-9">
            <iframe src="welcome.jsp" width="100%" frameborder="0" height="1000px" name="test"
                    onload="changeActive()"></iframe>
        </div>
    </div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="js/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
<script>
    $(function () {

        //点击才给左侧版块名加active的方法
        /*$(".list-group a").click(function() {
          //给被点击的添加class，同胞元素（siblings）删除class
          console.log($(this));
          $(this).addClass("active").siblings().removeClass("active");
        });*/

    });

    //改良后的active激活方法
    function changeActive() {
        let iframe_src = $("iframe[name='test']").get(0).contentWindow.location.pathname;
        let href = iframe_src.split("/");
        $("a[href='" + href[href.length - 1] + "']").addClass("active").siblings().removeClass("active");
    }
</script>
</body>
</html>