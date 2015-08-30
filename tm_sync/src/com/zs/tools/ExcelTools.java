package com.zs.tools;

import com.zs.tools.bean.ExcelCell;
import com.zs.tools.bean.ExcelObject;
import com.zs.tools.bean.ExcelRow;
import com.zs.tools.bean.ExcelSheet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Allen on 2015/6/2.
 */
public class ExcelTools {
    private static final Log logger = LogFactory.getLog(ExcelTools.class);

    /**
     * ExcelObject中包含的数据构造excel，并且写入到指定路径
     * @param eob excel数据
     */
    public static ByteArrayOutputStream writeExcelFile(ExcelObject eob, Map<Short, Short> columnWidthMap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            List<ExcelSheet> excelSheetList = eob.getExcelSheetList();
            HSSFWorkbook book = new HSSFWorkbook();
            for(ExcelSheet sheet : excelSheetList){
                HSSFSheet poiSheet = book.createSheet();
                if(null != columnWidthMap){
                    for(Map.Entry<Short, Short> entry : columnWidthMap.entrySet()){
                        poiSheet.setColumnWidth(entry.getKey(), entry.getValue());
                    }
                }
                List<ExcelRow> excelRowList = sheet.getExcelRowList();
                for(ExcelRow row : excelRowList){
                    HSSFRow poiRow = poiSheet.createRow(row.getRow());
                    List<ExcelCell> excelCellList = row.getExcelCellList();
                    for(ExcelCell cell : excelCellList){
                        if(cell.getRow() == row.getRow()){
                            //创建默认的Excel样式
                            HSSFCellStyle cellStyle = book.createCellStyle();
                            //设置单元格边框
                            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                            //设置字体上下居中
                            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                            HSSFCell poiCell = poiRow.createCell(cell.getCol());
                            if(cell.getMergeCol() > 0 && cell.getMergeRow() == 0){
                                poiSheet.addMergedRegion(new Region(cell.getRow(),cell.getCol(),cell.getRow(),(byte)(cell.getCol() + cell.getMergeCol())));
                            }else if(cell.getMergeRow() > 0 && cell.getMergeCol() == 0){
                                poiSheet.addMergedRegion(new Region(cell.getRow(),cell.getCol(),cell.getRow() + cell.getMergeRow(),cell.getCol()));
                            }else if(cell.getMergeCol() > 0 && cell.getMergeRow() > 0){
                                poiSheet.addMergedRegion(new Region(cell.getRow(),cell.getCol(),cell.getRow() + cell.getMergeRow(),(byte)(cell.getCol() + cell.getMergeCol())));
                            }
                            poiCell.setCellValue(new HSSFRichTextString(cell.getValue()));

                        }else{
                            throw new Exception("单元格行与所属行不一致");
                        }
                    }
                }
            }
            book.write(bos);

        }catch(Exception e){
            logger.error("写入excel出现异常", e);
        }
        return bos;
    }
}
