package com.escredit.base.util.excelutil;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dym
 * @date 2021/4/16 - 10:30
 */
public class BigDataExcelUtil extends DefaultHandler {

    private SharedStringsTable sst;
    private String lastContents;
    private boolean nextIsString;

    private List<String> rowlist = new ArrayList<>();
    private List<Map> datalist = new ArrayList<>();
    private int curRow = 0;
    private int curCol = 0;
    private String col = "";
    private Map map = new HashMap();

    /**
     * 读取第一个工作簿的入口方法
     *
     * @param path
     */
    public List<Map> readOneSheet(String path, int sheetidx) throws Exception {
        OPCPackage pkg = OPCPackage.open(path);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        InputStream sheet = r.getSheet("rId"+sheetidx);

        InputSource sheetSource = new InputSource(sheet);
        parser.parse(sheetSource);

        sheet.close();

        return datalist;
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory
                .createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        // c => 单元格
        if (name.equals("c")) {
            col = attributes.getValue("r");
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true
            String cellType = attributes.getValue("t");
            if (cellType != null && cellType.equals("s")) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }
        }
        // 置空
        lastContents = "";
    }


    public void endElement(String uri, String localName, String name)
            throws SAXException {
        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        if (nextIsString) {
            try {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx))
                        .toString();
                nextIsString = false;
//                System.out.println("lastContents值为："+lastContents);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
        // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
        if (name.equals("v")) {
            String value = lastContents.trim();
            rowlist.add(curCol, value);
            curCol++;
            map.put(col, value);
        } else {
            // 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
            if (name.equals("row")) {
                addDataList(curRow);
                rowlist.clear();
                curRow++;
                curCol = 0;
            }
        }
    }

    private void addDataList(int curRow) {
        Map rowMap = new HashMap(map);
        datalist.add(curRow,rowMap);
        map.clear();
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // 得到单元格内容的值
        lastContents += new String(ch, start, length);
    }
}
/**
 * @program: escredit-base
 * @description: 大数据量excel解析
 * @author: duyiming
 * @create: 2021-04-16 10:30
 **/