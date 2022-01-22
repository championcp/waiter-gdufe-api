package com.waiterxiaoyy.backandroiddesign.utils.poi;

import com.waiterxiaoyy.backandroiddesign.entity.SelectiveCourse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author :WaiterXiaoYY
 * @description: 读取选修课
 * @data :2020/12/17 19:40
 */
public class SelectiveCoursePoi {
    private static HashMap<String, SelectiveCourse> selectiveCourseMap;
    private static String filepath = "F:\\java3.0\\BackAndroidDesign\\src\\main\\resources\\static\\List of elective courses.xlsx";
    public static HashMap<String, SelectiveCourse> getSelectiveCourse() {
        selectiveCourseMap = new HashMap<String, SelectiveCourse>();

        try {
            InputStream inputStream = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int maxRowNum = sheet.getPhysicalNumberOfRows();

            for(int i = 0; i < maxRowNum; i++) {
                Row row = sheet.getRow(i);

                String courseid = getCellStringValue(row.getCell(1));
                String coursename = getCellStringValue(row.getCell(2));
                Integer credit = Integer.parseInt(getCellStringValue(row.getCell(3)));
                String courseCollege = getCellStringValue(row.getCell(4));
                String coursetype = getCellStringValue(row.getCell(5));
                if(selectiveCourseMap.containsKey(coursename)) {
                    SelectiveCourse selectiveCourse = selectiveCourseMap.get(coursename);
                    List<String> courseidList = selectiveCourse.getCourseid();
                    courseidList.add(courseid);
                    selectiveCourse.setCourseid(courseidList);

                    List<Integer> creditList = selectiveCourse.getCredit();
                    creditList.add(credit);
                    selectiveCourse.setCredit(creditList);

                    selectiveCourseMap.put(coursename, selectiveCourse);
                } else {
                    List<String> courseidList1 = new ArrayList<String>();
                    List<Integer> creditList1 = new ArrayList<Integer>();
                    courseidList1.add(courseid);
                    creditList1.add(credit);

                    SelectiveCourse selectiveCourse = new SelectiveCourse(coursetype, courseidList1, coursename, creditList1, courseCollege);
                    selectiveCourseMap.put(coursename, selectiveCourse);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return selectiveCourseMap;
    }
    public static void main(String[] args) {
        selectiveCourseMap = new HashMap<String, SelectiveCourse>();
        try {
            InputStream inputStream = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int maxRowNum = sheet.getPhysicalNumberOfRows();

            for(int i = 0; i < maxRowNum; i++) {
                Row row = sheet.getRow(i);

                String courseid = getCellStringValue(row.getCell(1));
                String coursename = getCellStringValue(row.getCell(2));
                Integer credit = Integer.parseInt(getCellStringValue(row.getCell(3)));
                String courseCollege = getCellStringValue(row.getCell(4));
                String coursetype = getCellStringValue(row.getCell(5));
                if(selectiveCourseMap.containsKey(coursename)) {
                    SelectiveCourse selectiveCourse = selectiveCourseMap.get(coursename);
                    List<String> courseidList = selectiveCourse.getCourseid();
                    courseidList.add(courseid);
                    selectiveCourse.setCourseid(courseidList);

                    List<Integer> creditList = selectiveCourse.getCredit();
                    creditList.add(credit);
                    selectiveCourse.setCredit(creditList);

                    selectiveCourseMap.put(coursename, selectiveCourse);
                } else {
                    List<String> courseidList1 = new ArrayList<String>();
                    List<Integer> creditList1 = new ArrayList<Integer>();
                    courseidList1.add(courseid);
                    creditList1.add(credit);

                    SelectiveCourse selectiveCourse = new SelectiveCourse(coursetype, courseidList1, coursename, creditList1, courseCollege);
                    selectiveCourseMap.put(coursename, selectiveCourse);
                }
            }
            for (SelectiveCourse selectiveCourse : selectiveCourseMap.values()) {
                System.out.println(selectiveCourse.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getCellStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        int type = cell.getCellType();
        String cellValue;
        switch (type) {
            case 3:
                cellValue = "";
                break;
            case 5  :
                cellValue = "";
                break;
            case 4:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case 0:
                cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                break;
            case 1:
                cellValue = cell.getStringCellValue();
                break;
            case 2:
                cellValue = cell.getCellFormula();
                break;
            default:
                cellValue = "";
                break;
        }
        return cellValue;
    }
}
