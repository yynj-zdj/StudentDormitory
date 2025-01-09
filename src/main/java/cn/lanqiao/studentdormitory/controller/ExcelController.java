package cn.lanqiao.studentdormitory.controller;


import cn.lanqiao.studentdormitory.pojo.Dormitory;
import cn.lanqiao.studentdormitory.pojo.Student;

import cn.lanqiao.studentdormitory.result.Resultx;

import cn.lanqiao.studentdormitory.service.IBuildingService;
import cn.lanqiao.studentdormitory.service.IDormitoryService;
import cn.lanqiao.studentdormitory.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2025-01-03
 * 导入导出功能 Dormitory Student模块 其他模块可在此增加
 */
@RestController
@RequestMapping("/excel")
@Slf4j
public class ExcelController {

    @Autowired
    private IDormitoryService iDormitoryService;

    @Autowired
    private IBuildingService iBuildingService;

    @Autowired
    private IStudentService iStudentService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportExcel(@RequestParam String table) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(table);

        if ("student".equalsIgnoreCase(table)) {
            List<Student> students = iStudentService.list();
            createStudentSheet(sheet, students);
        } else if ("dormitory".equalsIgnoreCase(table)) {
            List<Dormitory> dormitories = iDormitoryService.list();
            createDormitorySheet(sheet, dormitories);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", table + ".xlsx");
        headers.setContentLength(bytes.length);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @PostMapping("/import")
    public Resultx<String> importExcel(@RequestParam("file") MultipartFile file, @RequestParam("table") String table) {
        if (file.isEmpty()) {
            return Resultx.error("上传文件为空");
        }

        try {
            if ("student".equalsIgnoreCase(table)) {
                List<Student> students = parseStudentExcel(file);
                boolean result = iStudentService.saveBatch(students);
                if (result) {
                    return Resultx.success("导入成功");
                } else {
                    return Resultx.error("导入失败");
                }
            } else if ("dormitory".equalsIgnoreCase(table)) {
                List<Dormitory> dormitories = parseDormitoryExcel(file);
                boolean result = iDormitoryService.saveBatch(dormitories);
                if (result) {
                    return Resultx.success("导入成功");
                } else {
                    return Resultx.error("导入失败");
                }
            } else {
                return Resultx.error("不支持的表类型");
            }
        } catch (IOException e) {
            log.error("导入Excel文件时发生错误: ", e);
            return Resultx.error("导入失败: " + e.getMessage());
        }
    }

    private List<Student> parseStudentExcel(MultipartFile file) throws IOException {
        List<Student> students = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // 跳过表头行
                continue;
            }

            Student student = new Student();
            student.setId((int) row.getCell(0).getNumericCellValue());
            student.setSn(getCellValueAsString(row.getCell(1)));
            student.setName(getCellValueAsString(row.getCell(2)));
            student.setSex(getCellValueAsString(row.getCell(3)));
            student.setPassword(getCellValueAsString(row.getCell(4)));

            students.add(student);
        }

        workbook.close();
        return students;
    }

    private List<Dormitory> parseDormitoryExcel(MultipartFile file) throws IOException {
        List<Dormitory> dormitories = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // 跳过表头行
                continue;
            }

            Dormitory dormitory = new Dormitory();
            dormitory.setId((int) row.getCell(0).getNumericCellValue());
            dormitory.setSn(getCellValueAsString(row.getCell(1)));
            dormitory.setBuildingId(getCellValueAsString(row.getCell(2))); // 以 String 类型获取
            dormitory.setFloor(getCellValueAsString(row.getCell(3)));
            dormitory.setMaxNumber((int) row.getCell(4).getNumericCellValue());
            dormitory.setLivedNumber((int) row.getCell(5).getNumericCellValue());

            dormitories.add(dormitory);
        }

        workbook.close();
        return dormitories;
    }


    private void createStudentSheet(Sheet sheet, List<Student> students) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("学生编号");
        headerRow.createCell(2).setCellValue("姓名");
        headerRow.createCell(3).setCellValue("性别");
        headerRow.createCell(4).setCellValue("密码");

        int rowNum = 1;
        for (Student student : students) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getId());
            row.createCell(1).setCellValue(student.getSn());
            row.createCell(2).setCellValue(student.getName());
            row.createCell(3).setCellValue(student.getSex());
            row.createCell(4).setCellValue(student.getPassword());
        }
    }

    private void createDormitorySheet(Sheet sheet, List<Dormitory> dormitories) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("宿舍编号");
        headerRow.createCell(2).setCellValue("楼宇ID");
        headerRow.createCell(3).setCellValue("楼层");
        headerRow.createCell(4).setCellValue("最大可住人数");
        headerRow.createCell(5).setCellValue("已住人数");

        int rowNum = 1;
        for (Dormitory dormitory : dormitories) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dormitory.getId());
            row.createCell(1).setCellValue(dormitory.getSn());
            row.createCell(2).setCellValue(dormitory.getBuildingId());
            row.createCell(3).setCellValue(dormitory.getFloor());
            row.createCell(4).setCellValue(dormitory.getMaxNumber());
            row.createCell(5).setCellValue(dormitory.getLivedNumber());
        }
    }

    /***
     * 处理不同类型单元格的字符串转换
     * @param cell
     * @return
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
