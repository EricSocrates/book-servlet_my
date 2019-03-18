<%--
  Created by IntelliJ IDEA.
  User: Yy
  Date: 2019/3/9
  Time: 11:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <style>
        /*检查到是因为page-header的原因导致页面多了一块*/
        .page-header {
            margin: 2px 0px;
        }
    </style>
</head>
<body>
<!--详细信息（修改页）-->
<div class="modal fade" id="bookDetailModal" tabindex="-1" role="dialog" aria-labelledby="bookDetailModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">书籍详细信息</h5>
            </div>
            <div class="modal-body">
                <div id="carouselExampleIndicators" class="carousel slide">
                    <ol class="carousel-indicators"></ol>
                    <div class="carousel-inner" role="listbox"></div>
                    <!-- Controls -->
                    <a class="left carousel-control" href="#carouselExampleIndicators" role="button" data-slide="prev">
                        <span class="glyphicon glyphicon-chevron-left"></span>
                        <span class="sr-only">Previous</span>
                    </a>
                    <a class="right carousel-control" href="#carouselExampleIndicators" role="button" data-slide="next">
                        <span class="glyphicon glyphicon-chevron-right"></span>
                        <span class="sr-only">Next</span>
                    </a>
                </div>
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-group">
                        <input type="hidden" id="bookid" name="bookid">
                        <label class="col-sm-2 control-label">书名</label>
                        <div class="col-sm-10">
                            <span id="name"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">作者</label>
                        <div class="col-sm-10">
                            <span id="author"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">价格</label>
                        <div class="col-sm-10">
                            <span id="price"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">出版日期</label>
                        <div class="col-sm-10">
                            <span id="publishdate"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">数量</label>
                        <div class="col-sm-10">
                            <span id="quantity"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">所属类别</label>
                        <div class="col-sm-10">
                            <span id="caname"></span>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">返回</button>
                <button type="button" class="btn btn-success" data-dismiss="modal" id="detailAddToCart">加入购物车</button>
            </div>
        </div>
    </div>
</div>
<%--页头--%>
<%--<div class="page-header">--%>
<%--<img src="image/header.jpg" alt="" width="100%" height="130px">--%>
<%--</div>--%>
<%--导航条--%>
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
            <a class="navbar-brand" href="#">书籍展示</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">首页</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="index.jsp">进入后台</a></li>
                <li><a href="showcart.jsp">查看购物车</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">用户名<span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#" onclick="logout()">注销</a></li>
                    </ul>
                </li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<div class="container-fluid">
    <div class="row" id="pics">
    </div>
</div>
<br>
<br>
您最近浏览的书籍如下：<br>
<div id="recentvisiting"></div>


<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="js/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
<script>
    let pn = 1;
    let ps = 8;
    $(function () {
        queryRecentVisiting();
        queryData(pn, ps);
    });

    function queryRecentVisiting() {
        $.post("book", "method=queryRecentVisiting", function (msg) {
            console.log(msg);
            if (msg != null && msg.length > 0) {
                let ul = "<ul>";
                $.each(msg, function (i, book) {
                    ul += "<li><a href='javascript:void(0);'  onclick='bookDetail(" + JSON.stringify(book) + ")'>" + book.name + "</a></li>"
                });
                ul += "</ul>";
                $("#recentvisiting").html(ul);
            } else {
                $("#recentvisiting").text("第一次访问！");
            }
        }, "json");
    }


    //查询上架且未删除的书籍信息
    function queryData(pn, ps) {
        $.post("book", "method=queryBookSeparated&queryWay=front&pageNumber=" + pn + "&pageSize=" + ps, function (msg) {
            console.log(msg);
            let pics = "<div class='row'>";
            $.each(msg.rows, function (i, b) {
                console.log(b.bps[0].iscover);
                pics +=
                    "<div class='col-sm-6 col-md-4'>" +
                    "<div class='thumbnail'>" +
                    (b.bps[0].iscover == '1' ? "<img src='/book_servlet" + b.bps[0].savepath + "' alt=''>" : "没有封面喔") +
                    "<div class='caption'>" +
                    "<div>" + b.name + "</div>" +
                    "<div>" + "￥" + b.price + "</div>" +
                    "<a class='btn btn-primary' onclick='bookDetail(" + JSON.stringify(b) + ")' >" + "详情" + "</a>" +
                    "  <a onclick='addToCart(\"" + b.id + "\")' class='btn btn-success' >" + "加入购物车" + "</a>" +
                    "</div>" +
                    "</div>" +
                    "</div>";
            });
            pics += "</div>";
            $("#pics").html(pics);
        }, "json");
    }

    function bookDetail(book) {
        $.post("book", "method=addBookCookie&id=" + book.id, function (msg) {
            console.log(msg);
            queryRecentVisiting();
        }, "json");
        $(".carousel-control").css("display", "block");
        let year = (1900 + book.publishdate.year);
        let month = (book.publishdate.month + 1) >= 10 ? (book.publishdate.month + 1) : "0" + (book.publishdate.month + 1);
        let date = (book.publishdate.date) >= 10 ? (book.publishdate.date) : "0" + (book.publishdate.date);
        let publishdate_result = year + "-" + month + "-" + date;
        $("#bookDetailModal").modal("show");
        $("#name").text(book.name);
        $("#author").text(book.author);
        $("#price").text(book.price);
        $("#caname").text(book.ca.name);
        $("#quantity").text(book.quantity);
        $("#publishdate").text(publishdate_result);
        $("#detailAddToCart").click(function () {
            addToCart(book.id);
        });
        let img = "";
        let indicator = "";
        console.log(book.bps.length + "length");
        if (book.bps.length <= 1) {
            $(".carousel-control").css("display", "none");
            return;
        }
        $.each(book.bps, function (i, b) {
            console.log(b);
            indicator += "<li data-target='#carouselExampleIndicators' data-slide-to='" + i + "'" + (i == 0 ? "class='active'" : "") + "></li>";
            img += "<div class='item" + (i == 0 ? " active" : "") + "'>" +
                "<img style='width: 200px;height: 200px' src='/book_servlet" + b.savepath + "'>" +
                "</div>";
        });
        $(".carousel-inner").html(img);
        $(".carousel-indicators").html(indicator);
        $('.carousel').carousel({
            interval: 2000,
            ride: true
        })
    }

    //同步地加入购物车
    function addToCart(id) {
        window.location.href = "book?method=addToCart&id=" + id;
        book = "";
    }
</script>
</body>
</html>
