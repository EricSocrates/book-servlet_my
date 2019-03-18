<%--
  Created by IntelliJ IDEA.
  User: Yy
  Date: 2019/3/15
  Time: 19:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>首页</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <style>
        div {
            margin: 0;
            padding: 0;
            border: 0;
        }
    </style>
</head>
<body>
<table class="table table-bordered table-striped table-hover">
    <thead>
    <tr>
        <th>编号</th>
        <th>书名</th>
        <th>作者</th>
        <th>价格</th>
        <th>数量</th>
        <th>总价</th>
    </tr>
    </thead>
    <tbody>
    <c:set var="total" value="0"></c:set>
    <c:forEach items="${sessionScope.shoppingCart}" var="kv" varStatus="i">
        <tr>
            <td>${i.index + 1}</td>
            <td>${kv.value.book.name}</td>
            <td>${kv.value.book.author}</td>
            <td>${kv.value.book.price}</td>
            <td>${kv.value.count}</td>
            <td>${kv.value.sum}</td>
            <c:set var="total" value="${kv.value.sum + total}"></c:set>
        </tr>
    </c:forEach>
    <tr>
        <td colspan="6">合计：${total}</td>
    </tr>
    </tbody>
</table>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="js/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>