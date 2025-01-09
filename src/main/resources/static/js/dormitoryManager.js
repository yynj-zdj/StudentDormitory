var contentDormitoryManagerData;
var editContentDormitoryManagerData;
/**
 * 宿管管理导航
 */
function dormitoryManagerNav() {
    navSelected('#dormitoryManagerNavItem');
    contentHeadShow('.dormitoryManagerContentHead');
    clearContentTable();
    initDormitoryManager();
}

/**
 * 宿管导航初始化
 */
/**
 * 宿管导航初始化
 */
function initDormitoryManager() {
    $('#contentData').bootstrapTable({
        data: [], // 初始化为空数组
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
            title: '宿管编号',
            align: 'left',
            valign: 'left'
        }, {
            field: 'name',
            title: '姓名',
            align: 'left',
            valign: 'left'
        }, {
            field: 'sex',
            title: '性别',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'password',
            title: '密码',
            align: 'left',
            valign: 'left'
        }, {
            field: 'id',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: dormitoryManagerFormatter
        }]
    });
    initDormitoryManagerData();
}


function initDormitoryManagerData() {
    axios.get('/dormitoryManager', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(response => {
            contentDormitoryManagerData = response.data.data || [];
            var table = $('#contentData');
            table.bootstrapTable('load', contentDormitoryManagerData); // 使用 load 方法刷新表格
        })
        .catch(error => {
            console.error('Error:', error);
        });
}


function dormitoryManagerFormatter(value, row, index) {
    var id = value;
    var result = "";
    result += "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#dormitoryManagerUpdate'  onclick=\"dormitoryManagerUpdate('" + index + "')\"><i class='fa fa-pencil'></i> 修改</button>";
    result += "<button type='button' class='btn btn-danger' onclick=\"dormitoryManagerDelete('" + id + "')\"><i class='fa fa-trash'></i> 删除</button>";
    return result;
}

/**
 * 宿管查询
 */
function dormitoryManagerQuery() {
    var dormitoryManagerSn = $("#dormitoryManagerSn").val();
    var dormitoryManagerName = $("#dormitoryManagerName").val();

    if (isNull(dormitoryManagerSn)) {
        if (isNull(dormitoryManagerName)) {
            // 均为空，初始化查询
            initDormitoryManagerData();
        } else {
            // 按照姓名查询
            axios.get('/dormitoryManager/search/findByName?name=' + dormitoryManagerName, {
                headers: {
                    'Accept': 'application/json;charset=UTF-8',
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            })
                .then(response => {
                    contentDormitoryManagerData = response.data.data || [];
                    var table = $('#contentData');
                    table.bootstrapTable('load', contentDormitoryManagerData);
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    } else {
        // 按照宿管编号查询
        axios.get('/dormitoryManager/search/findBySn?sn=' + dormitoryManagerSn, {
            headers: {
                'Accept': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json;charset=UTF-8'
            }
        })
            .then(response => {
                // 确保返回的是一个数组
                contentDormitoryManagerData = Array.isArray(response.data.data) ? response.data.data : [response.data.data];
                var table = $('#contentData');
                table.bootstrapTable('load', contentDormitoryManagerData);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}


function dormitoryManagerAddSave() {
    var data = {};
    var sex = $('input[type="radio"][name="addDormitoryManagerSex"]:checked').val();
    data.sn = $("#addDormitoryManagerSn").val();
    data.name = $("#addDormitoryManagerName").val();
    data.password = $("#addDormitoryManagerPassword").val();
    data.sex = sex;
    axios.post('/dormitoryManager', data, {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(response => {
            swal('温馨提示', '新增宿管成功', 'success');
            initDormitoryManagerData();
        })
        .catch(error => {
            swal('温馨提示', '新增宿管失败', 'error');
        });
}

function dormitoryManagerUpdate(index) {
    var row = contentDormitoryManagerData[index];
    editContentDormitoryManagerData = contentDormitoryManagerData[index];
    $("#updateDormitoryManagerId").val(row.id);
    $("#updateDormitoryManagerSn").val(row.sn);
    $("#updateDormitoryManagerName").val(row.name);
    $("#updateDormitoryManagerPassword").val(row.password);
    if ('男' === row.sex) {
        $("#updateDormitoryManagerSexM").prop('checked', true);
        $("#updateDormitoryManagerSexF").prop('checked', false);
    } else {
        $("#updateDormitoryManagerSexF").prop('checked', true);
        $("#updateDormitoryManagerSexM").prop('checked', false);
    }
}

function dormitoryManagerUpdateSave(){
    var data = {};
    data.id = $("#updateDormitoryManagerId").val();
    data.sn = $("#updateDormitoryManagerSn").val();
    data.name = $("#updateDormitoryManagerName").val();
    data.password = $("#updateDormitoryManagerPassword").val();
    data.createTime = editContentDormitoryManagerData.createTime;
    var sex = $('input[type="radio"][name="updateDormitoryManagerSex"]:checked').val();
    data.sex = sex;
    axios.put('/dormitoryManager/' + data.id, data, {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(response => {
            swal('温馨提示', '修改宿管成功', 'success');
            initDormitoryManagerData();
        })
        .catch(error => {
            swal('温馨提示', '修改宿管失败', 'error');
        });
}

function dormitoryManagerDelete(id) {
    var data = {};
    data.id = id;
    axios.delete('/dormitoryManager/' + id, {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(response => {
            swal('温馨提示', '删除宿管成功', 'success');
            initDormitoryManagerData();
        })
        .catch(error => {
            swal('温馨提示', '删除宿管失败', 'error');
        });
}

/**
 * 上传Excel文件
 */
function dormitoryManagerUpload() {
    var uploadData = new FormData();
    var uploadName = $("#dormitoryManagerUploadFile").val();
    uploadData.append("file", $("#dormitoryManagerUploadFile")[0].files[0]);
    uploadData.append("name", uploadName);
    axios.post('/excel/import', uploadData, {
        headers: {
            'Accept': 'application/json;charset=UTF-8'
        }
    })
        .then(response => {
            swal('温馨提示', '导入成功', 'success');
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
