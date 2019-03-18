<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>新增图书</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<%--新增书籍表单--%>
<form class="form-horizontal" role="form" action="book" method="post" enctype="multipart/form-data">
    <div class="form-group">
        <label for="name" class="col-sm-2 control-label">书名:</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="name" name="name" placeholder="请输入书名" autocomplete="off">
        </div>
    </div>
    <!--作者-->
    <div class="form-group">
        <label for="author" class="col-sm-2 control-label">作者:</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="author" name="author" placeholder="请输入作者" autocomplete="off">
        </div>
    </div>
    <!--价格-->
    <div class="form-group">
        <label for="price" class="col-sm-2 control-label">价格:</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="price" name="price" placeholder="请输入价格" autocomplete="off">
        </div>
    </div>
    <!--出版日期-->
    <div class="form-group">
        <label for="publishdate" class="col-sm-2 control-label">出版日期:</label>
        <div class="col-sm-10">
            <input type="date" class="form-control" id="publishdate" name="publishdate" placeholder="请输入书名"
                   autocomplete="off">
        </div>
    </div>
    <!--数量-->
    <div class="form-group">
        <label for="quantity" class="col-sm-2 control-label">数量:</label>
        <div class="col-sm-10">
            <input type="tel" class="form-control" id="quantity" name="quantity" placeholder="请输入书籍库存数量"
                   autocomplete="off">
        </div>
    </div>
    <!--所在类别-->
    <div class="form-group">
        <label for="caid" class="col-sm-2 control-label">所在类别:</label>
        <div class="col-sm-10">
            <select class="form-control" id="caid" name="caid"></select>
        </div>
    </div>
    <!--状态-->
    <div class="form-group">
        <label for="status" class="col-sm-2 control-label">状态:</label>
        <div class="col-sm-10">
            <select class="form-control" id="status" name="status">
                <option value="0">上架</option>
                <option value="1">下架</option>
            </select>
        </div>
    </div>
    <!--图片-->
    <div class="form-group">
        <button type="button" class="btn btn-primary" onclick="addPic()">添加图片</button>
        <div class="col-sm-10" id="pics"></div>
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

    $(function () {
        queryAllCategories();
    });

    function queryAllCategories() {
        $.post("book", "method=queryAllCategories", function (data) {
            let ops = "<option value='-1'>==请选择==</option>";
            //msg====List<Category>
            $.each(data, function (i, c) {
                ops += "<option value='" + c.id + "'>" + c.name + "</option>";
            });
            $("#caid").html(ops);
        }, "json");
    }

    /*点击即出现一个添加图片的表单域*/
    /*这里做同步的文件上传，异步上传移步booklist.jsp模态框的修改图书*/
    /*相较于异步存后再显示，这样的预览速度更快*/
    let i = 0;

    function addPic() {
        let pic =
            "<div id='picDiv" + i + "'>" +
            "请选择:<input type='file' name='pic' id='pic" + i + "' onchange='preview(" + i + ")'/><br/>" +
            "<div id='setCover" + i + "' style='display:none'>" +
            "设为封面:<input type='radio' id='cover" + i + "' name='cover' value='" + i + "'/>" +
            "<button id='cancelUpload_btn" + i + "' type='button' onclick='cancelUpload(" + i + ")'>取消上传这一张</button>" +
            "</div>" +
            "<br/><br/><br/>" +
            "</div>";
        i++;
        $("#pics").append(pic);
    }

    function preview(value) {
        let fileTag = $("#pic" + value);
        let url = getObjectURL(fileTag[0].files[0]);
        let img = "";
        if (url != null) {
            img += "<img src='" + url + "' height='300' width='200'>"
        }
        fileTag.after(img);
        $("#setCover" + value).css("display", "block");
    }

    function cancelUpload(value) {
        $("#pic" + value).attr("disabled", "disabled");
        $("#cover" + value).attr("disabled", "disabled");
        $("#picDiv" + value).css("display", "none");
    }

    // 图片选中即显示的方法
    function getObjectURL(file) {
        var url = null;
        if (window.createObjectURL != undefined) { // basic
            url = window.createObjectURL(file);
        } else if (window.URL != undefined) { // mozilla(firefox)
            url = window.URL.createObjectURL(file);
        } else if (window.webkitURL != undefined) { // webkit or chrome
            url = window.webkitURL.createObjectURL(file);
        }
        return url;
    }
</script>
</body>
</html>




