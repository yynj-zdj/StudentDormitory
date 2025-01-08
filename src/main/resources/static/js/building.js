var contentBuildingData;
var editContentBuildingData;

/**
 * 楼宇管理导航
 */
function buildingNav() {
    navSelected('#buildingNavItem');
    contentHeadShow('.buildingContentHead');
    clearContentTable();
    initBuilding();
}

/**
 * 楼宇导航初始化
 */
function initBuilding() {
    $('#contentData').bootstrapTable({
        data: contentBuildingData,
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
            field: 'name',
            title: '楼宇名称',
            align: 'left',
            valign: 'left'
        }, {
            field: 'location',
            title: '楼宇所属位置',
            align: 'left',
            valign: 'left'
        }, {
            field: 'dormitoryManagerSn',
            title: '宿管编号',
            align: 'left',
            valign: 'left'
        }, {
            field: 'dormitoryManagerName',
            title: '宿管名称',
            align: 'left',
            valign: 'left'
        }, {
            field: 'id',
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: buildingFormatter
        }]
    });
    initBuildingData();
}

/**
 * 初始化楼宇数据
 */
function initBuildingData() {
    axios.get('/building', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            contentBuildingData = response.data.data.map(buildingDto => ({
                id: buildingDto.id,
                location: buildingDto.location,
                name: buildingDto.name,
                dormitoryManagerId: buildingDto.dormitoryManager.id,
                dormitoryManagerSn: buildingDto.dormitoryManager.sn,
                dormitoryManagerName: buildingDto.dormitoryManager.name
            }));
            var table = $('#contentData');
            table.bootstrapTable('refreshOptions', {data: contentBuildingData, dataType: "json"});
        })
        .catch(function (error) {
            console.error(error);
        });
}

/**
 * 楼宇表格的修改和删除操作
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
function buildingFormatter(value, row, index) {
    var id = row.id; // 确保使用 row.id 而不是 value
    var result = "";
    result += "<button type='button' class='btn btn-warning' data-toggle='modal' data-target='#buildingUpdate' onclick=\"buildingUpdate('" + id + "')\"><i class='fa fa-pencil'></i> 修改</button>";
    result += "<button type='button' class='btn btn-danger' onclick=\"buildingDelete('" + id + "')\"><i class='fa fa-trash'></i> 删除</button>";
    return result;
}


/**
 * 楼宇查询
 */
function buildingQuery() {
    var buildingName = $("#buildingName").val();
    if (isNull(buildingName)) {
        axios.get('/building', {
            headers: {
                'Accept': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json;charset=UTF-8'
            }
        })
            .then(function (response) {
                contentBuildingData = response.data.data.map(buildingDto => ({
                    id: buildingDto.id,
                    location: buildingDto.location,
                    name: buildingDto.name,
                    dormitoryManagerId: buildingDto.dormitoryManager.id,
                    dormitoryManagerSn: buildingDto.dormitoryManager.sn,
                    dormitoryManagerName: buildingDto.dormitoryManager.name
                }));
                var table = $('#contentData');
                table.bootstrapTable('refreshOptions', {data: contentBuildingData, dataType: "json"});
            })
            .catch(function (error) {
                console.error(error);
            });
    } else {
        axios.get('/building/search/findByName?name=' + buildingName, {
            headers: {
                'Accept': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json;charset=UTF-8'
            }
        })
            .then(function (response) {
                contentBuildingData = response.data.data.map(buildingDto => ({
                    id: buildingDto.id,
                    location: buildingDto.location,
                    name: buildingDto.name,
                    dormitoryManagerId: buildingDto.dormitoryManager.id,
                    dormitoryManagerSn: buildingDto.dormitoryManager.sn,
                    dormitoryManagerName: buildingDto.dormitoryManager.name
                }));
                var table = $('#contentData');
                table.bootstrapTable('refreshOptions', {data: contentBuildingData, dataType: "json"});
            })
            .catch(function (error) {
                console.error(error);
            });
    }
}

function buildingAdd() {
    var contentData;
    var html = "";
    axios.get('/building/dormitoryManager', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            contentData = response.data.data;
            for (var i = 0; i < contentData.length; i++) {
                html += "<option value=\"" + contentData[i].id + "\">" + contentData[i].name + "</option>";
            }
            $("#addBuildingDormitoryManager").html(html);
        })
        .catch(function (error) {
            console.error(error);
        });
}

function buildingAddSave() {
    var data = {};
    data.name = $("#addBuildingName").val();
    data.location = $("#addBuildingLocation").val();
    data.dormitoryManagerId = $("#addBuildingDormitoryManager").val();
    axios.post('/building', data, {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            swal('温馨提示', '新增楼宇成功', 'success');
            initBuildingData();
        })
        .catch(function (error) {
            swal('温馨提示', '新增楼宇失败', 'error');
        });
}

function buildingUpdate(id) {
    // 找到对应的楼宇数据
    var row = contentBuildingData.find(item => item.id === parseInt(id));
    if (!row) {
        console.error("未找到对应ID的楼宇数据", id);
        return;
    }
    editContentBuildingData = row;

    $("#updateBuildingId").val(row.id);
    $("#updateBuildingName").val(row.name);
    $("#updateBuildingLocation").val(row.location);

    var html = "";
    axios.get('/building/dormitoryManager', {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            var contentData = response.data.data;
            for (var i = 0; i < contentData.length; i++) {
                html += "<option value=\"" + contentData[i].id + "\" " + (contentData[i].id === row.dormitoryManagerId ? "selected" : "") + ">" + contentData[i].name + "</option>";
            }
            $("#updateBuildingDormitoryManager").html(html);
        })
        .catch(function (error) {
            console.error(error);
        });
}


function buildingUpdateSave() {
    var data = {};
    data.id = $("#updateBuildingId").val();
    data.name = $("#updateBuildingName").val();
    data.location = $("#updateBuildingLocation").val();
    data.dormitoryManagerId = $("#updateBuildingDormitoryManager").val();
    data.createTime = editContentBuildingData.createTime;
    axios.put('/building/' + data.id, data, {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            swal('温馨提示', '修改楼宇成功', 'success');
            initBuildingData();
        })
        .catch(function (error) {
            swal('温馨提示', '修改楼宇失败', 'error');
        });
}

function buildingDelete(id) {
    var data = {};
    data.id = id;
    axios.delete('/building/' + id, {
        headers: {
            'Accept': 'application/json;charset=UTF-8',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            swal('温馨提示', '删除楼宇成功', 'success');
            initBuildingData();
        })
        .catch(function (error) {
            swal('温馨提示', '删除楼宇失败', 'error');
        });
}

/**
 * 上传Excel文件
 */
function buildingUpload() {
    var uploadData = new FormData();
    var uploadName = $("#buildingUploadFile").val();
    uploadData.append("file", $("#buildingUploadFile")[0].files[0]);
    uploadData.append("name", uploadName);
    axios.post('/excel/import', uploadData, {
        headers: {
            'Accept': 'application/json;charset=UTF-8'
        }
    })
        .then(function (response) {
            swal('温馨提示', '导入成功', 'success');
        })
        .catch(function (error) {
            console.error(error);
        });
}

// 辅助函数：检查是否为空
function isNull(value) {
    return value === null || value === undefined || value.trim() === '';
}
