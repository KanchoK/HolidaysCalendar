package com.web;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by R500 on 25.7.2014 г..
 */
public class DocumentGenerator {
    public static void generateDocument(String names, String beginDate, String endDate) {
        XWPFDocument document = null;
        try {
            document = new XWPFDocument(new FileInputStream("src/main/webapp/documents/VacationForm.docx"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);

        XWPFTable table = document.createTable(10, 2);

        table.getRow(0).getCell(0).setText("От /трите имена/");
        table.getRow(0).getCell(1).setText(names);
        table.getRow(1).getCell(0).setText("До");
        table.getRow(1).getCell(1).setText("Новарто ООД");
        table.getRow(2).getCell(0).setText("Оставащи дни от год. отпуска преди молбата");
        table.getRow(2).getCell(1).setText("");
        table.getRow(3).getCell(0).setText("Моля да ми бъде разрешена отпуска            от");
        table.getRow(3).getCell(1).setText(beginDate);
        table.getRow(4).getCell(0).setText("                       до");
        table.getRow(4).getCell(1).setText(endDate);
        table.getRow(5).getCell(0).setText("Всичко дни");
        table.getRow(5).getCell(1).setText("");
        table.getRow(6).getCell(0).setText("Основание за отпуската");
        table.getRow(6).getCell(1).setText("Полагаем годишен отпуск");
        table.getRow(7).getCell(0).setText("Разрешено от /подпис/");
        table.getRow(7).getCell(1).setText("");
        table.getRow(8).getCell(0).setText("Дата");
        table.getRow(8).getCell(1).setText(today);
        table.getRow(9).getCell(0).setText("Подпис на служителя");
        table.getRow(9).getCell(1).setText(".");

        XWPFParagraph paragraph = table.getRow(9).getCell(1).getParagraphs().get(0);
        paragraph.setIndentationRight(4300);

        // Get a list of the rows in the table
        List<XWPFTableRow> rows = table.getRows();
        int rowCt = 0;
        int colCt = 0;
        for (XWPFTableRow row : rows) {
            // get table row properties (trPr)
            CTTrPr trPr = row.getCtRow().addNewTrPr();
            // set row height; units = twentieth of a point, 360 = 0.25"
            CTHeight ht = trPr.addNewTrHeight();
            ht.setVal(BigInteger.valueOf(1120));

            // get the cells in this row
            List<XWPFTableCell> cells = row.getTableCells();
            // add content to each cell
            for (XWPFTableCell cell : cells) {
                // get a table cell properties element (tcPr)
                CTTcPr tcpr = cell.getCTTc().addNewTcPr();
                // set vertical alignment to "center"
                CTVerticalJc va = tcpr.addNewVAlign();
                va.setVal(STVerticalJc.CENTER);
            } // for cell
            colCt = 0;
            rowCt++;
        } // for row

        FileOutputStream out = null;
        try {
            out = new FileOutputStream("src/main/webapp/documents/HolidayRequest.docx");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            document.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
