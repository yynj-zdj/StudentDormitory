var contentStudentData;
var editContentStudentData;

/**
 * 学生管理导航
 */
function studentNav() {
    navSelected('#studentNavItem');
    contentHeadShow('.studentContentHead');
    clearContentTable();
    initStudent();
}

/**
 * 学生导航初始化
 */
function initStudent() {
    $('#contentData').bootstrapTable({
        data: [],
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
            field: 'sn',
            title: '学生编号',
            align: 'center',
            valign: 'left'
        }, {
            field: 'name',
            title: '姓名',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'sex',
            title: '性别',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'id',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: studentFormatter
        }]
    });
    initStudentData();
}

/**
 * 初始化学生数据
 */
function initStudentData() {
    axios.get('http://localhost:8082/student')
        .then(function (response) {
            contentStudentData = response.data.data; // 从响应中提取实际数据
            var table = $('#contentData');
            table.bootstrapTable('load', contentStudentData); // 使用 load 方法加载数据
        })
        .catch(function (error) {
            console.error("Error fetching student data:", error);
        });
}

/**
 * 学生表格的修改和删除操作
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function studentFormatter(value, row, index) {
    var id = value;
    var result = "";
    if (loginType === '3') {
        return result;
    }
    result += "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#studentUpdate' onclick=\"studentUpdate('" + index + "')\"><i class='fa fa-pencil'></i> 修改</button>";
    result += "<button type='button' class='btn btn-danger' onclick=\"studentDelete('" + id + "')\"><i class='fa fa-trash'></i> 删除</button>";
    return result;
}

/**
 * 学生查询
 */
function studentQuery() {
    var studentSn = $("#studentSn").val();
    var studentName = $("#studentName").val();

    if (isNull(studentSn)) {
        if (isNull(studentName)) {
            // 均为空，初始化查询
            initStudentData();
        } else {
            // 按照姓名查询
            axios.get('http://localhost:8082/student/search/findByName?name=' + studentName)
                .then(function (response) {
                    console.log("Response data:", response.data); // 添加日志输出

                    // 确保返回的数据是数组格式
                    var studentData = response.data.data;
                    if (studentData && typeof studentData === 'object' && !Array.isArray(studentData)) {
                        studentData = [studentData]; // 将单个对象转换为数组
                    }

                    contentStudentData = studentData || [];
                    var table = $('#contentData');
                    table.bootstrapTable('load', contentStudentData);
                })
                .catch(function (error) {
                    console.error("Error fetching student data by name:", error);
                });
        }
    } else {
// 按照学生编号查询
        axios.get('http://localhost:8082/student/search/findBySn?sn=' + studentSn)
            .then(function (response) {
                console.log("Response data:", response.data); // 添加日志输出

                // 确保返回的数据是数组格式
                var studentData = response.data.data;
                if (studentData && typeof studentData === 'object' && !Array.isArray(studentData)) {
                    studentData = [studentData]; // 将单个对象转换为数组
                }

                contentStudentData = studentData || [];
                var table = $('#contentData');
                table.bootstrapTable('load', contentStudentData);
            })
            .catch(function (error) {
                console.error("Error fetching student data by sn:", error);
            });

    }
}


/**
 * 新增保存学生
 */
function studentAddSave() {
    var data = {};
    var sex = $('input[type="radio"][name="addStudentSex"]:checked').val();
    data.sn = $("#addStudentSn").val();
    data.name = $("#addStudentName").val();
    data.password = $("#addStudentPassword").val();
    data.sex = sex;
    axios.post('http://localhost:8082/student', data)
        .then(function (response) {
            swal('温馨提示', '新增学生成功', 'success');
            initStudentData();
        })
        .catch(function (error) {
            swal('温馨提示', '新增学生失败', 'error');
            console.error("Error adding student:", error);
        });
}

/**
 * 学生更新
 * @param index
 */
function studentUpdate(index) {
    var row = contentStudentData[index];
    editContentStudentData = contentStudentData[index];
    $("#updateStudentId").val(row.id);
    $("#updateStudentSn").val(row.sn);
    $("#updateStudentName").val(row.name);
    $("#updateStudentPassword").val(row.password);
    if ('男' === row.sex) {
        $("#updateStudentSexM").prop('checked', true);
        $("#updateStudentSexF").prop('checked', false);
    } else {
        $("#updateStudentSexF").prop('checked', true);
        $("#updateStudentSexM").prop('checked', false);
    }
}

function studentUpdateSave() {
    var data = {};
    data.id = $("#updateStudentId").val();
    data.sn = $("#updateStudentSn").val();
    data.name = $("#updateStudentName").val();
    data.password = $("#updateStudentPassword").val();
    data.createTime = editContentStudentData.createTime;
    var sex = $('input[type="radio"][name="updateStudentSex"]:checked').val();
    data.sex = sex;
    axios.put('http://localhost:8082/student/' + data.id, data)
        .then(function (response) {
            swal('温馨提示', '修改学生成功', 'success');
            initStudentData();
        })
        .catch(function (error) {
            swal('温馨提示', '修改学生失败', 'error');
            console.error("Error updating student:", error);
        });
}

/**
 * 学生删除
 * @param id
 */
function studentDelete(id) {
    axios.delete('http://localhost:8082/student/' + id)
        .then(function (response) {
            swal('温馨提示', '删除学生成功', 'success');
            initStudentData();
        })
        .catch(function (error) {
            swal('温馨提示', '删除学生失败', 'error');
            console.error("Error deleting student:", error);
        });
}

/**
 * 上传Excel文件
 */
function studentUpload() {
    var uploadData = new FormData();
    var uploadName = $("#studentUploadFile").val();
    uploadData.append("file", $("#studentUploadFile")[0].files[0]);
    uploadData.append("name", uploadName);
    uploadData.append("table", "student"); // 添加 table 参数
    axios.post('http://localhost:8082/excel/import', uploadData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
        .then(function (response) {
            swal('温馨提示', '导入成功', 'success');
            initStudentData(); // 重新发送查询全部学生的请求
        })
        .catch(function (error) {
            console.error("Error uploading file:", error);
        });
}
