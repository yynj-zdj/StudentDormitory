var contentLiveData;
var editContentLiveData;

/**
 * 入住管理导航
 */
function liveNav() {
    navSelected('#liveNavItem');
    contentHeadShow('.liveContentHead');
    clearContentTable();
    initLive();
}

/**
 * 入住导航初始化
 */
function initLive() {
    $('#contentData').bootstrapTable({
        data: contentLiveData,
        dataType: 'json',
        pagination: true,
        pageSize: 5,
        striped: true,
        search: false,
        singleSelect: false,
        showHeader: true,
        showFooter: false,
        showColumns: false,
        showRefresh: false,
        showToggle: false,
        sortable: false,
        columns: [{
            field: 'studentSn',
            title: '学生编号',
            align: 'left',
            valign: 'left'
        }, {
            field: 'studentName',
            title: '学生姓名',
            align: 'left',
            valign: 'left'
        }, {
            field: 'dormitorySn',
            title: '宿舍编号',
            align: 'left',
            valign: 'left'
        }, {
            field: 'liveDate',
            title: '入住日期',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'id',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: liveFormatter
        }]
    });
    initLiveData();
}

function initLiveData() {
    axios.get('/live', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            contentLiveData = response.data.data;
            addStudentAndDormitoryData(contentLiveData);
        })
        .catch(function (error) {
            console.error(error);
        });
}

function addStudentAndDormitoryData(contentLiveData) {
    axios.get('/student', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            var studentData = response.data.data;
            return axios.get('/dormitory', {
                headers: {
                    'Accept': 'application/json;charset=UTF-8',
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            })
                .then(function (response) {
                    var dormitoryData = response.data.data;
                    for (var i = 0; i < contentLiveData.length; i++) {
                        for (var j = 0; j < studentData.length; j++) {
                            if (contentLiveData[i].studentId === studentData[j].id) {
                                contentLiveData[i].studentName = studentData[j].name;
                                contentLiveData[i].studentSn = studentData[j].sn;
                                break;
                            }
                        }
                        for (var k = 0; k < dormitoryData.length; k++) {
                            if (contentLiveData[i].dormitoryId === dormitoryData[k].id) {
                                contentLiveData[i].dormitorySn = dormitoryData[k].sn;
                                break;
                            }
                        }
                    }
                    var table = $('#contentData');
                    table.bootstrapTable('refreshOptions', {data: contentLiveData, dataType: "json"});
                });
        })
        .catch(function (error) {
            console.error(error);
        });
}

function liveFormatter(value, row, index) {
    var id = value;
    var result = "";
    result += "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#liveUpdate' onclick=\"liveUpdate('" + index + "')\"><i class='fa fa-pencil'></i> 修改</button>";
    result += "<button type='button' class='btn btn-danger' onclick=\"liveDelete('" + id + "')\"><i class='fa fa-trash'></i> 删除</button>";
    return result;
}

function liveQuery() {
    var liveDormitorySn = $("#liveDormitorySn").val();
    var liveStudentNameOrSn = $("#liveStudentNameOrSn").val();
    initLiveData();

    if (isNull(liveDormitorySn)) {
        if (isNull(liveStudentNameOrSn)) {
            initLiveData();
        } else {
            //学生过滤
            var afterContentLiveData = new Array();
            for (var i = 0; i < contentLiveData.length; i++) {
                if (contentLiveData[i].studentSn === liveStudentNameOrSn) {
                    afterContentLiveData.push(contentLiveData[i]);
                } else if (contentLiveData[i].studentName == liveStudentNameOrSn) {
                    afterContentLiveData.push(contentLiveData[i]);
                }
            }
            contentLiveData = afterContentLiveData;
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentLiveData, dataType: "json"});
        }
    } else {
        if (isNull(liveStudentNameOrSn)) {
            //宿舍过滤
            var afterContentLiveData = new Array();
            for (var i = 0; i < contentLiveData.length; i++) {
                if (contentLiveData[i].dormitorySn === liveDormitorySn) {
                    afterContentLiveData.push(contentLiveData[i]);
                }
            }
            contentLiveData = afterContentLiveData;
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentLiveData, dataType: "json"});
        } else {
            //学生宿舍过滤
            var afterContentLiveData = new Array();
            for (var i = 0; i < contentLiveData.length; i++) {
                if (contentLiveData[i].studentSn === liveStudentNameOrSn) {
                    if (contentLiveData[i].dormitorySn === liveDormitorySn) {
                        afterContentLiveData.push(contentLiveData[i]);
                    }
                } else if (contentLiveData[i].studentName == liveStudentNameOrSn) {
                    if (contentLiveData[i].dormitorySn === liveDormitorySn) {
                        afterContentLiveData.push(contentLiveData[i]);
                    }
                }
            }
            contentLiveData = afterContentLiveData;
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentLiveData, dataType: "json"});
        }
    }
}

function liveAdd() {
    var selectStudentData;
    var selectDormitoryData;
    var html1 = "";
    var html2 = "";
    axios.get('/student', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            selectStudentData = response.data.data;
            for (var i = 0; i < selectStudentData.length; i++) {
                html1 += "<option value=\"" + selectStudentData[i].id + "\">" + selectStudentData[i].name + "</option>";
            }
            $("#addLiveStudentId").html(html1);
        })
        .catch(function (error) {
            console.error(error);
        });

    axios.get('/dormitory', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            selectDormitoryData = response.data.data;
            for (var i = 0; i < selectDormitoryData.length; i++) {
                html2 += "<option value=\"" + selectDormitoryData[i].id + "\">" + selectDormitoryData[i].sn + "</option>";
            }
            $("#addLiveDormitoryId").html(html2);
        })
        .catch(function (error) {
            console.error(error);
        });
}

function liveAddSave() {
    var data = {};
    data.studentId = $("#addLiveStudentId").val();
    data.dormitoryId = $("#addLiveDormitoryId").val();
    data.liveDate = $("#addLiveDate").val();
    axios.post('/live', JSON.stringify(data), {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            swal('温馨提示', '新增入住成功', 'success');
            initLiveData();
        })
        .catch(function (error) {
            swal('温馨提示', '新增入住失败', 'error');
            console.error(error);
        });
}

function liveUpdate(index) {
    var row = contentLiveData[index];
    var selectStudentData;
    var selectDormitoryData;
    var html1 = "";
    var html2 = "";
    editContentLiveData = contentLiveData[index];
    $("#updateLiveId").val(row.id);
    $("#updateLiveDate").val(row.liveDate);
    axios.get('/student', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            selectStudentData = response.data.data;
            for (var i = 0; i < selectStudentData.length; i++) {
                html1 += "<option value=\"" + selectStudentData[i].id + "\">" + selectStudentData[i].name + "</option>";
            }
            $("#updateLiveStudentId").html(html1);
        })
        .catch(function (error) {
            console.error(error);
        });

    axios.get('/dormitory', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            selectDormitoryData = response.data.data;
            for (var i = 0; i < selectDormitoryData.length; i++) {
                html2 += "<option value=\"" + selectDormitoryData[i].id + "\">" + selectDormitoryData[i].sn + "</option>";
            }
            $("#updateLiveDormitoryId").html(html2);
        })
        .catch(function (error) {
            console.error(error);
        });
}

function liveUpdateSave() {
    var data = {};
    data.id = $("#updateLiveId").val();
    data.liveDate = $("#updateLiveDate").val();
    data.studentId = $("#updateLiveStudentId").val();
    data.dormitoryId = $("#updateLiveDormitoryId").val();
    data.createTime = editContentLiveData.createTime;
    axios.put('/live/' + data.id, JSON.stringify(data), {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            swal('温馨提示', '修改入住成功', 'success');
            initLiveData();
        })
        .catch(function (error) {
            swal('温馨提示', '修改入住失败', 'error');
            console.error(error);
        });
}

function liveDelete(id) {
    var data = {};
    data.id = id;
    axios.delete('/live/' + id, {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            swal('温馨提示', '删除入住成功', 'success');
            initLiveData();
        })
        .catch(function (error) {
            swal('温馨提示', '删除入住失败', 'error');
            console.error(error);
        });
}

/**
 * 上传Excel文件
 */
function liveUpload() {
    var uploadData = new FormData();
    var uploadName = $("#liveUploadFile").val();
    uploadData.append("file", $("#liveUploadFile")[0].files[0]);
    uploadData.append("name", uploadName);
    axios.post('/excel/import', uploadData, {
        headers: {
            'Accept': 'application/json;charset=UTF-8'
        },
        processData: false,
        contentType: false
    })
        .then(function (response) {
            swal('温馨提示', '导入成功', 'success');
        })
        .catch(function (error) {
            console.error(error);
        });
}
