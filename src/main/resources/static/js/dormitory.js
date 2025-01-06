var contentDormitoryData;
var editContentDormitoryData;

/**
 * 宿舍管理导航
 */
function dormitoryNav() {
    navSelected('#dormitoryNavItem');
    contentHeadShow('.dormitoryContentHead');
    clearContentTable();
    initDormitory();
}

/**
 * 宿舍导航初始化
 */
function initDormitory() {
    $('#contentData').bootstrapTable({
        data: contentDormitoryData,
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
            title: '宿舍编号',
            align: 'left',
            valign: 'left'
        }, {
            field: 'buildingName',
            title: '所属楼宇',
            align: 'left',
            valign: 'left'
        }, {
            field: 'floor',
            title: '所属楼层',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'maxNumber',
            title: '最大可住人数',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'livedNumber',
            title: '已住人数',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'id',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: dormitoryFormatter
        }]
    });
    initDormitoryData();
}

function initDormitoryData() {
    axios.get('/dormitory', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            contentDormitoryData = response.data.data;
            addBuildingData(contentDormitoryData);
        })
        .catch(function (error) {
            console.error('Error fetching dormitory data:', error);
        });
}

function addBuildingData(contentDormitoryData) {
    axios.get('/dormitory/building', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            var buildingData = response.data.data;
            for (var i = 0; i < contentDormitoryData.length; i++) {
                for (var j = 0; j < buildingData.length; j++) {
                    if (contentDormitoryData[i].buildingId === buildingData[j].id) {
                        contentDormitoryData[i].buildingName = buildingData[j].name;
                        break;
                    }
                }
            }
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentDormitoryData, dataType: "json"});
        })
        .catch(function (error) {
            console.error('Error fetching building data:', error);
        });
}

function dormitoryFormatter(value, row, index) {
    var id = value;
    var result = "";
    if (loginType === '3') {
        return result;
    }
    result += "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#dormitoryUpdate' onclick=\"dormitoryUpdate('" + index + "')\"><i class='fa fa-pencil'></i> 修改</button>";
    result += "<button type='button' class='btn btn-danger' onclick=\"dormitoryDelete('" + id + "')\"><i class='fa fa-trash'></i> 删除</button>";
    return result;
}

function dormitoryQuery() {
    var dormitorySn = $("#dormitorySn").val();
    if (isNull(dormitorySn)) {
        axios.get('/dormitory', {
            headers: {
                'Accept': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json;charset=UTF-8'
            }
        })
            .then(function (response) {
                contentDormitoryData = response.data.data;
                addBuildingData(contentDormitoryData);
            })
            .catch(function (error) {
                console.error('Error fetching dormitory data:', error);
            });
    } else {
        axios.get('/dormitory/search/findBySn?sn=' + dormitorySn, {
            headers: {
                'Accept': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json;charset=UTF-8'
            }
        })
            .then(function (response) {
                var dataArray = [];
                var data = response.data.data;
                if (data) {
                    if (Array.isArray(data)) {
                        dataArray = data;
                    } else {
                        dataArray.push(data);
                    }
                }
                contentDormitoryData = dataArray;
                addBuildingData(contentDormitoryData);
            })
            .catch(function (error) {
                console.error('Error fetching dormitory data by sn:', error);
            });
    }
}


function dormitoryAdd() {
    axios.get('/dormitory/building', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            var contentData = response.data.data;
            var html = "";
            for (var i = 0; i < contentData.length; i++) {
                html += "<option value=\"" + contentData[i].id + "\">" + contentData[i].name + "</option>";
            }
            $("#addDormitoryBuildingId").html(html);
        })
        .catch(function (error) {
            console.error('Error fetching building data:', error);
        });
}

function dormitoryAddSave() {
    var data = {};
    data.sn = $("#addDormitorySn").val();
    data.buildingId = $("#addDormitoryBuildingId").val();
    data.floor = $("#addDormitoryFloor").val();
    data.maxNumber = $("#addDormitoryMaxNumber").val();
    data.livedNumber = $("#addDormitoryLivedNumber").val();
    axios.post('/dormitory', data, {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            swal('温馨提示', '新增宿舍成功', 'success');
            initDormitoryData();
        })
        .catch(function (error) {
            swal('温馨提示', '新增宿舍失败', 'error');
            console.error('Error adding dormitory:', error);
        });
}

function dormitoryUpdate(index) {
    var row = contentDormitoryData[index];
    editContentDormitoryData = contentDormitoryData[index];
    $("#updateDormitoryId").val(row.id);
    $("#updateDormitorySn").val(row.sn);
    $("#updateDormitoryFloor").val(row.floor);
    $("#updateDormitoryMaxNumber").val(row.maxNumber);
    $("#updateDormitoryLivedNumber").val(row.livedNumber);
    axios.get('/dormitory/building', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            var contentData = response.data.data;
            var html = "";
            for (var i = 0; i < contentData.length; i++) {
                html += "<option value=\"" + contentData[i].id + "\">" + contentData[i].name + "</option>";
            }
            $("#updateDormitoryBuildingId").html(html);
        })
        .catch(function (error) {
            console.error('Error fetching building data:', error);
        });
}

function dormitoryUpdateSave() {
    var data = {};
    data.id = $("#updateDormitoryId").val();
    data.sn = $("#updateDormitorySn").val();
    data.floor = $("#updateDormitoryFloor").val();
    data.maxNumber = $("#updateDormitoryMaxNumber").val();
    data.livedNumber = $("#updateDormitoryLivedNumber").val();
    data.buildingId = $("#updateDormitoryBuildingId").val();
    data.createTime = editContentDormitoryData.createTime;
    axios.put('/dormitory/' + data.id, data, {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            swal('温馨提示', '修改宿舍成功', 'success');
            initDormitoryData();
        })
        .catch(function (error) {
            swal('温馨提示', '修改宿舍失败', 'error');
            console.error('Error updating dormitory:', error);
        });
}

function dormitoryDelete(id) {
    axios.delete('/dormitory/' + id, {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            swal('温馨提示', '删除宿舍成功', 'success');
            initDormitoryData();
        })
        .catch(function (error) {
            swal('温馨提示', '删除宿舍失败', 'error');
            console.error('Error deleting dormitory:', error);
        });
}

/**
 * 上传Excel文件
 */
function dormitoryUpload() {
    var uploadData = new FormData();
    var uploadName = $("#dormitoryUploadFile").val();
    uploadData.append("file", $("#dormitoryUploadFile")[0].files[0]);
    uploadData.append("name", uploadName);
    uploadData.append("table", "dormitory"); // 添加 table 参数
    axios.post('/excel/import', uploadData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
        .then(function (response) {
            swal('温馨提示', '导入成功', 'success');
            initDormitoryData(); // 重新发送查询全部的请求
        })
        .catch(function (error) {
            console.error('Error uploading file:', error);
        });
}
