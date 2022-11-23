package com.example.demo.helper;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.response.paper.PaperResponse;

public class PaperExcelExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<PaperResponse> papers;
	
	public PaperExcelExporter( List<PaperResponse> data) {
		this.papers = data;
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine() {
        sheet = workbook.createSheet("Papers");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Paper ID", style);      
        createCell(row, 1, "Student name", style);       
        createCell(row, 2, "Class Name", style);    
        createCell(row, 3, "Mark", style);
        createCell(row, 4, "Result", style);
         
    }
	
	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value?"PASS":"FAIL");
        }else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }else if (value instanceof String) {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (PaperResponse p : papers) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, p.getId(), style);
            createCell(row, columnCount++, p.getStudentName(), style);
            createCell(row, columnCount++, p.getClassName(), style);
            createCell(row, columnCount++, p.getMark(), style);
            createCell(row, columnCount++, p.getIsPass(), style);
             
        }
    }
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        outputStream.flush();
        outputStream.close();
    }
}
