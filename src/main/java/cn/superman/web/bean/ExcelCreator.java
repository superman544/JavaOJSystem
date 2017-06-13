package cn.superman.web.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class ExcelCreator {
    private ExcelCreator() {

    }

    private static final int FIRST_TABLE_ROW_NUMBER = 0;

    /**
     * 生成的Excel文件的文件名
     */
    private String fileName = System.currentTimeMillis() + "";
    /**
     * 如果是输出到本地的话，这个路径将会记录输出到本地的哪一个路径
     */
    private String fileSavePath;

    /**
     * 工作表的表名
     */
    private String sheetName = "表1";

    /**
     * 每一列数据的表头信息
     */
    private String[] headers;

    /**
     * 每一行的数据
     */
    private List<List<String>> datas;

    /**
     * 每一列的宽度
     */
    private short[] colWidths;
    /**
     * 是否水平居中
     */
    private boolean isHorizontalCENTER = false;
    /**
     * 是否垂直居中
     */
    private boolean isVerticalCENTER = false;

    /**
     * 是否自动换行
     */
    private boolean isWrapText = true;

    public static class Builder {
        /**
         * 生成的Excel文件的文件名
         */
        private String fileName = System.currentTimeMillis() + "";
        /**
         * 如果是输出到本地的话，这个路径将会记录输出到本地的哪一个路径
         */
        private String fileSavePath;

        /**
         * 工作表的表名
         */
        private String sheetName = "表1";

        /**
         * 每一列数据的表头信息
         */
        private String[] headers;

        /**
         * 每一行的数据
         */
        private List<List<String>> datas;

        /**
         * 每一列的宽度
         */
        private short[] colWidths;
        /**
         * 是否水平居中
         */
        private boolean isHorizontalCENTER = false;
        /**
         * 是否垂直居中
         */
        private boolean isVerticalCENTER = false;

        /**
         * 是否自动换行
         */
        private boolean isWrapText = true;

        public ExcelCreator bulid() throws FileNotFoundException {

            if (fileSavePath == null || fileSavePath.isEmpty()) {
                throw new FileNotFoundException("文件保存的根目录不能为空");
            }

            ExcelCreator creator = new ExcelCreator();
            creator.fileName = fileName;
            creator.fileSavePath = fileSavePath;
            creator.sheetName = sheetName;
            creator.headers = headers;
            creator.datas = datas;
            creator.colWidths = colWidths;
            creator.isHorizontalCENTER = isHorizontalCENTER;
            creator.isVerticalCENTER = isVerticalCENTER;
            creator.isWrapText = isWrapText;
            return creator;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder fileSavePath(String fileSavePath) {
            this.fileSavePath = fileSavePath;
            return this;
        }

        public Builder sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public Builder headers(String[] headers) {
            this.headers = headers;
            return this;
        }

        public Builder datas(List<List<String>> datas) {
            this.datas = datas;
            return this;
        }

        public Builder colWidths(short[] colWidths) {
            this.colWidths = colWidths;
            return this;
        }

        public Builder isHorizontalCENTER(boolean isHorizontalCENTER) {
            this.isHorizontalCENTER = isHorizontalCENTER;
            return this;
        }

        public Builder isVerticalCENTER(boolean isVerticalCENTER) {
            this.isVerticalCENTER = isVerticalCENTER;
            return this;
        }

        public Builder isWrapText(boolean isWrapText) {
            this.isWrapText = isWrapText;
            return this;
        }
    }

    public void create() throws IOException {
        // 生成excel
        HSSFWorkbook wb = makeExcel();
        // 设置输出流
        FileOutputStream fileOut = new FileOutputStream(fileSavePath + File.separator + fileName + ".xls");
        writeToStream(wb, fileOut);
    }

    public void create(HttpServletResponse response) throws IOException {
        // 生成excel
        HSSFWorkbook wb = makeExcel();
        // 设置输出流
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=student.xls");
        writeToStream(wb, response.getOutputStream());
    }

    private HSSFWorkbook makeExcel() {
        HSSFWorkbook wb = new HSSFWorkbook();// 创建Excel工作簿对象
        HSSFSheet sheet = wb.createSheet(sheetName);// 创建Excel工作表对象
        HSSFRow tableHeadRow = sheet.createRow(FIRST_TABLE_ROW_NUMBER); // 创建Excel工作表的行

        if (colWidths != null) {
            // 设置每行宽度
            for (int i = 0; i < colWidths.length; i++) {
                sheet.setColumnWidth(i, colWidths[i] * 256);
            }
        }

        HSSFCellStyle cellStyle = createCellStyle(wb);
        HSSFCell headCell = null;
        // 创建表头
        for (int i = 0; i < headers.length; i++) {
            headCell = tableHeadRow.createCell(i);
            headCell.setCellStyle(cellStyle); // 创建Excel工作表指定行的单元格
            headCell.setCellValue(headers[i]);
        }

        // 设置每一行的数据
        HSSFRow dataRow = null;
        List<String> rowDatas = null;
        HSSFCell dataCell = null;
        int dataRowNumber = FIRST_TABLE_ROW_NUMBER + 1;
        for (int dataNumber = 0; dataNumber < datas.size(); dataNumber++, dataRowNumber++) {
            dataRow = sheet.createRow(dataRowNumber);
            rowDatas = datas.get(dataNumber);

            for (int i = 0; i < rowDatas.size(); i++) {
                dataCell = dataRow.createCell(i);
                dataCell.setCellStyle(cellStyle);
                dataCell.setCellValue(rowDatas.get(i));
            }
        }
        return wb;
    }

    private HSSFCellStyle createCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle cellStyle = wb.createCellStyle();// 创建单元格样式

        if (isHorizontalCENTER) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        }

        if (isVerticalCENTER) {
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        }

        if (isWrapText) {
            cellStyle.setWrapText(true);
        }

        return cellStyle;
    }

    private void writeToStream(HSSFWorkbook wb, OutputStream outputStream) throws IOException {
        wb.write(outputStream);
        outputStream.flush();
        wb.close();
        outputStream.close();
    }
}
