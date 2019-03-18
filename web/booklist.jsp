<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>书籍列表</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="modal fade" id="deleteBookByIdModal" tabindex="-1" role="dialog" aria-labelledby="deleteBookByIdModal"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteBookById">删除</h5>
            </div>
            <div class="modal-body">
                您真的要删除吗？
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">算了算了</button>
                <button type="button" class="btn btn-primary" id="deleteBookByIdFinal" onclick="deleteBookById()">
                    不删留着过年？
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="delBookSelectedModal" tabindex="-1" role="dialog" aria-labelledby="delBookSelectedodalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="delBookSelectedLabel">删除</h5>
            </div>
            <div class="modal-body">
                您真的要删除吗？
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">算了算了</button>
                <button type="button" class="btn btn-primary" onclick="delBookSelected()">不删留着过年？</button>
            </div>
        </div>
    </div>
</div>

<!--详细信息（修改页）-->
<div class="modal fade" id="queryBookByIdModal" tabindex="-1" role="dialog" aria-labelledby="queryBookByIdModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">书籍详细信息</h5>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-group">
                        <input type="hidden" id="bookid" name="bookid">
                        <label for="name" class="col-sm-2 control-label">书名</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="name" name="name" placeholder="请输入书的名字">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="author" class="col-sm-2 control-label">作者</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="author" name="author" placeholder="请输入作者的名字">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="price" class="col-sm-2 control-label">价格</label>
                        <div class="col-sm-10">
                            <input type="number" class="form-control" id="price" name="price" placeholder="请输入书的价格">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="publishdate" class="col-sm-2 control-label">出版日期</label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="publishdate" name="publishdate"
                                   placeholder="请输入书的出版日期">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="quantity" class="col-sm-2 control-label">数量</label>
                        <div class="col-sm-10">
                            <input type="number" class="form-control" id="quantity" name="quantity"
                                   placeholder="请输入书的数量">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="caid" class="col-sm-2 control-label">所属类别</label>
                        <div class="col-sm-10">
                            <select name="caid" id="caid" class="form-control"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="status" class="col-sm-2 control-label">状态</label>
                        <div class="col-sm-10">
                            <select name="status" id="status" class="form-control">
                                <option value="0">上架</option>
                                <option value="1">下架</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        请选择<input type="file" name="pic" id="pic" onchange="previewAndActiveButton()"/>
                        <button type="button" class="btn btn-primary" onclick="addPic()">添加图片</button>
                        <div class="col-sm-10" id="pics"></div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">返回</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="updateBook()">更新信息</button>
            </div>
        </div>
    </div>
</div>
<!--查询区域-->
<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
    <div class="panel panel-default">
        <div class="panel-heading" role="tab" id="headingOne">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true"
                   aria-controls="collapseOne">
                    条件查询
                </a>
            </h4>
        </div>
        <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
            <div class="panel-body">
                <form class="form-inline" role="form">
                    <div class="form-group">
                        <label for="name_key">书名:</label>
                        <input type="text" class="form-control" id="name_key" name="name_key"
                               placeholder="请输入书名" autocomplete="off">
                        <label for="author_key">作家姓名:</label>
                        <input type="text" class="form-control" id="author_key" name="author_key"
                               placeholder="请输入作家姓名" autocomplete="off">
                        <select class="form-control" id="caid_key" name="caid_key"></select>
                    </div>
                    <button type="button" onclick="queryByCondition()" class="btn btn-primary">查询</button>
                    <button type="button" class="btn btn-success" onclick="export2Excel()">导出表格到Excel</button>
                </form>
            </div>
        </div>
    </div>
</div>
<!--查询区域end-->
<button type="button" class="btn btn-danger" style="display:none;" data-toggle='modal'
        data-target='#delBookSelectedModal'>删除选中项
</button>
<table class="table table-bordered table-striped table-condensed table-hover">
    <thead>
    <tr>
        <th><input type="checkbox" id="checkAll" name="checkbox" onchange="checkAll()"><label for="checkAll">全选</label>
        </th>
        <th>序号</th>
        <th>书名</th>
        <th>作者</th>
        <th>售价</th>
        <th>出版日期</th>
        <th>所属类别</th>
        <th>状态</th>
        <th>数量</th>
        <th>是否已删除</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody id="data"></tbody>
</table>
<div>
    总共<span id="state_total"></span>条记录，
    当前显示<span id="state_start"></span>条到<span id="state_end"></span>条记录。
    每页显示 <select name="ps" id="ps" onchange="changePageSize(this.value)">
    <option value="1">1</option>
    <option value="2">2</option>
    <option value="3">3</option>
</select>条
    <br>
    <button id="first" type="button" class="btn btn-primary">首页</button>
    <button id="pre" type="button" class="btn btn-success">上一页</button>
    <button id="next" type="button" class="btn btn-info">下一页</button>
    <button id="end" type="button" class="btn btn-danger">尾页</button>

</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="js/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
<script src="js/ajaxfileupload.js"></script>
<script>

    //条件查询
    function queryByCondition() {
        //重置条件
        condition = "";
        //获得条件
        let name_key = $("#name_key").val();
        let caid_key = $("#caid_key").val();
        let author_key = $("#author_key").val();
        let bookid = "";
        if (name_key != null && name_key != "") {
            condition += "&nameKey=" + name_key;
        }
        if (author_key != null && author_key != "") {
            condition += "&authorKey=" + author_key;
        }
        if (caid_key != null && caid_key != "" && caid_key != "-1") {
            condition += "&caidKey=" + caid_key;
        }
        queryData(1, ps, condition);
    }

    let pn = 1;
    let ps = 3;
    let pre, next, end;
    let condition = "";
    $(function () {
        $.post("book", "method=queryAllCategories", function (data) {
            let ops = "<option value='-1'>==请选择书籍类别==</option>";
            //msg====List<Category>
            $.each(data, function (i, c) {
                ops += "<option value='" + c.id + "'>" + c.name + "</option>";
            });
            $("#caid_key").html(ops);
        }, "json");


        queryData(pn, ps, condition);

        //~~~~~~~~注册点击事件
        $("#first").on("click", function () {
            queryData(1, ps, condition);
        });
        $("#pre").on("click", function () {
            queryData(pre, ps, condition);
        });
        $("#next").on("click", function () {
            queryData(next, ps, condition);
        });
        $("#end").on("click", function () {
            queryData(end, ps, condition);
        });
    });

    //全选checkbox点击
    function checkAll() {
        if ($("input#checkAll").prop("checked")) {
            $("input[name='check']").css("display", "inline").prop("checked", true);
            $("button:contains('删除选中')").css({"display": "block", "margin-bottom": "20px"});
            return;
        }
        $("input[name='check']").css("display", "none").prop("checked", false);
        $("button:contains('删除选中')").css("display", "none");
    }

    //每次分页的最终执行
    function queryData(pageNumber, pageSize, c) {
        $("input#checkAll").prop("checked", false);
        $.ajax({
            type: "POST",
            url: "book",  //?method=queryBookSeparated&pageNumber=x&pageSize=x&no=xx
            data: "method=queryBookSeparated&pageNumber=" + pageNumber + "&pageSize=" + pageSize + c,
            dataType: "json",
            success: function (msg) {
                //msg----{rows:[{pageNumber},{pageSize},{no,id:xx,num:xx,status}]}
                console.log(msg);
                pre = msg.pre;
                next = msg.next;
                end = msg.pageCount;
                ps = msg.pageSize;
                pn = msg.pageNumber;
                let trs = "";
                $.each(msg.rows, function (i, book) {
                    let year = (1900 + book.publishdate.year);
                    let month = (book.publishdate.month + 1) >= 10 ? (book.publishdate.month + 1) : "0" + (book.publishdate.month + 1);
                    let date = (book.publishdate.date) >= 10 ? (book.publishdate.date) : "0" + (book.publishdate.date);
                    let publishdate_result = year + "-" + month + "-" + date;
                    trs += "<tr>" +
                        "<td><input style='display:none' type='checkbox' onchange='checkedChange()' name='check' value='" + book.id + "'></td</td>" +
                        "<td>" + (msg.start + i + 1) + "</td>" +
                        "<td>" + book.name + "</td>" +
                        "<td>" + book.author + "</td>" +
                        "<td>" + book.price + "</td>" +
                        "<td>" + publishdate_result + "</td>" +
                        "<td>" + book.ca.name + "</td>" +
                        "<td>" + (book.status == 0 ? "上架" : "下架") + "</td>" +
                        "<td>" + book.quantity + "</td>" +
                        "<td>" + (book.del == 0 ? "否" : "是") + "</td>" +
                        "<td><button type='button' class='btn btn-primary' onclick='queryBookById(\"" + book.id + "\")'>修改</button>" +
                        " <button type='button' class='btn btn-danger' style='margin: 0 10px' name='deleteBookById' data-toggle='modal' data-target='#deleteBookByIdModal'  onclick='onClickDeleteBookById(\"" + book.id + "\")'>" + "删除" + "</button></td>" +
                        "</tr>";
                });
                $("#state_total").text(msg.total);
                $("#state_start").text(msg.start + 1);
                if (msg.pageNumber == msg.pageCount) {
                    $("#state_end").text(msg.total % msg.pageSize == 0 ? (msg.start + msg.pageSize) : (msg.start + (msg.total - (msg.pageCount - 1) * msg.pageSize)));
                } else {
                    $("#state_end").text(msg.start + msg.pageSize);
                }
                $("select#ps").val(msg.pageSize);
                $("#data").html(trs);
                checkAll();
            }
        })
    }

    //显示书籍详情
    function queryBookById(id) {
        $.post("book", "method=queryBookById&id=" + id, function (msg) {
            $("#bookid").val(msg.id);
            bookid = msg.id;
            $("#name").val(msg.name);
            $("#price").val(msg.price);
            $("#author").val(msg.author);
            $("#publishdate").val(msg.publishdateFormatted);
            $("#status").val(msg.status);
            $("#quantity").val(msg.quantity);
            $("#pic").val("");
            $("#preview_pic").css("display", "none");
            $("button:contains('添加图片')").attr("disabled", true);
            $.post("book", "method=queryAllCategories", function (data) {
                let ops = "";
                //msg====List<Category>
                $.each(data, function (i, c) {
                    if (msg.ca.name == c.name) ops += "<option value='" + c.id + "' selected>" + c.name + "</option>";
                    else ops += "<option value='" + c.id + "'>" + c.name + "</option>";
                });
                $("#caid").html(ops);
            }, "json");

            if (msg.bps != null && msg.bps.length > 0) {
                let pics = "";
                console.log(msg.bps);
                $.each(msg.bps, function (i, bp) {
                    pics += "<div class='row'>" +
                        "<div class='col-sm-6 col-md-4'>" +
                        "<div class='thumbnail'>" +
                        "<img src='/book_servlet" + bp.savepath + "' alt=''>" +
                        "<div class='caption'>" +
                        "<button type='button' class='btn btn-primary' onclick='setCover(\"" + msg.id + "\", \"" + bp.id + "\")'>" + (bp.iscover == '1' ? "当前封面" : "设置封面") + "</button> " +
                        "<button type='button' class='btn btn-danger' onclick='deleteBookpic(\"" + bp.id + "\")'>删除图片</button>" +
                        "</div>" +
                        "</div>" +
                        "</div>" +
                        "</div>";
                });
                $("#pics").html(pics);
            } else {
                $("#pics").html();
            }
            $("#queryBookByIdModal").modal("show");
        }, "json");
    }

    //异步上传图片
    function addPic() {
        if ($("input#pic").val() != "") {
            //异步的文件上传
            $.ajaxFileUpload({
                type: "post",
                url: "bookpic",
                //在传入照片的同时传入对应的bookid，这个全局值在打开modal时已经赋好
                data: {"bookid": bookid},
                dataType: "json",
                // 需要上传的文件域的ID，即<input type="file">的ID
                //上传多个时可以用“[]”
                fileElementId: "pic",
                success: function (msg) {
                    console.log(msg);
                    if (msg.flag == "success") {
                        queryBookById(bookid);
                    }
                }
            });
        }
    }

    //更新时，预览图片和激活上传按钮
    function previewAndActiveButton() {
        let file = $("input#pic");
        console.log(file);
        if (file.val() != "") {
            $("button:contains('添加图片')").attr("disabled", false);
            let url = getObjectURL(file[0].files[0]);
            $("#preview_pic").remove();
            let img = "";
            if (url != null) {
                img += "<img src='" + url + "' id='preview_pic' height='300' width='200'>"
            }
            file.after(img);
        }
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

    // 设置图片为封面
    function setCover(bid, bpid) {
        $.post("bookpic", "method=setCover&bid=" + bid + "&bpid=" + bpid, function (msg) {
            if (msg.flag == "success") {
                queryBookById(bid);
                $("#queryBookByIdModal").modal("show");
            }
        }, "json");
    }

    // 删除图片
    function deleteBookpic(bpid) {
        $.post("bookpic", "method=deleteBookpic&id=" + bpid, function (msg) {
            if (msg.flag == "success") {
                queryBookById(bookid);
            }
        }, "json");
    }

    //更新书籍信息
    function updateBook() {
        $.ajax({
            type: "POST",
            url: "book",
            data: "method=updateBook&" + $("form#detailForm").serialize(),
            dataType: "json",
            success: function (msg) {
                if (msg.flag == "success") {
                    alert("更新成功");
                    $("#queryBookByIdModal").modal("hide");
                    window.location.reload();
                }
            },
            error: function (err) {
                console.log(err);
            }
        });
    }

    //点击按ID删除书
    function onClickDeleteBookById(value) {
        $("#deleteBookByIdFinal").attr("value", value);
    }

    //执行按ID删除书
    function deleteBookById() {
        deleteBook($("#deleteBookByIdFinal").val());
        $("#deleteBookByIdModal").modal('hide');
    }

    //删除书的最终调用
    function deleteBook(value) {
        console.log(value);
        $.post("book", "method=deleteBookById&id=" + value, function (msg) {
            queryData(1, ps, "");
        }, "text");
    }

    //删除选中书
    function delBookSelected() {
        $("input[name='check']:checked").each(function () {
            deleteBook($(this).val());
        });
        $("#delBookSelectedModal").modal('hide');
    }

    //全选的子checkbox变化监听
    function checkedChange() {
        let flag = true;
        let flag_inverse = false;
        $("input[name=check]").each(function () {
            if (!$(this).prop("checked")) {
                flag = false;
            } else {
                flag_inverse = true;
            }
        });
        if (flag) {
            $("input#checkAll").prop("checked", true);
        } else {
            $("input#checkAll").prop("checked", false);
        }

        if (!flag_inverse) {
            $("input[name=check]").css("display", "none");
            $("button:contains('删除选中')").css("display", "none");
        }
    }

    //修改每页显示条数
    function changePageSize(val) {
        queryData(1, val, condition);
    }

    //将查询结果导出到Excel
    function export2Excel() {
        //重置条件
        condition = "";
        //获得条件
        let name_key = $("#name_key").val();
        let caid_key = $("#caid_key").val();
        let author_key = $("#author_key").val();
        let bookid = "";
        if (name_key != null && name_key != "") {
            condition += "&nameKey=" + name_key;
        }
        if (author_key != null && author_key != "") {
            condition += "&authorKey=" + author_key;
        }
        if (caid_key != null && caid_key != "" && caid_key != "-1") {
            condition += "&caidKey=" + caid_key;
        }
        window.location.href = "book?method=export2Excel&queryBookSeparated&pageNumber=" + pn + "&pageSize=" + ps + condition;
    }
</script>
</body>
</html>


