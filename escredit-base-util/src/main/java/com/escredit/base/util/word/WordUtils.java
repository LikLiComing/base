package com.escredit.base.util.word;

import com.escredit.base.util.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: WangYuJie
 * @Date: 2021/1/4 11:08
 */
public class WordUtils {

    private static String REGEX = "\\$\\{(.*?)}";

    /**
     * 用一个docx文档作为模板，然后替换其中的内容，再写入目标文档中。
     * @param params 填充参数
     * @param inPath 模板路径
     * @param regex 正则匹配,默认匹配 ${字段名}
     * @return byte[]
     * @throws IOException
     */
    public static byte[] templateWriteToByte(Map<String, Object> params, String inPath, String regex) throws IOException {
        InputStream is = new FileInputStream(inPath);
        XWPFDocument doc = new XWPFDocument(is);
        if (StringUtils.isNotEmpty(regex)) {
            REGEX = regex;
        }
        //替换段落里面的变量
        replaceInPara(doc, params);
        //替换表格里面的变量
        replaceInTable(doc, params);
        //替换页眉里面的变量
        replaceHeader(doc, params);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        doc.write(bos);
        bos.flush();
        byte[] byteArray = bos.toByteArray();

        close(bos);
        close(is);
        doc.close();
        return byteArray;
    }

    /**
     * 用一个docx文档作为模板，然后替换其中的内容，再写入目标文档中。
     * @param params 填充参数
     * @param inPath 模板路径
     * @param outPath 输出路径
     * @param regex 正则匹配,默认匹配 ${字段名}
     * @throws IOException
     */
    public static void templateWrite(Map<String, Object> params, String inPath, String outPath, String regex) throws IOException {
        InputStream is = new FileInputStream(inPath);
        XWPFDocument doc = new XWPFDocument(is);
        if (StringUtils.isNotEmpty(regex)) {
            REGEX = regex;
        }
        //替换段落里面的变量
        replaceInPara(doc, params);
        //替换表格里面的变量
        replaceInTable(doc, params);
        //替换页眉里面的变量
        replaceHeader(doc, params);

        OutputStream os = new FileOutputStream(outPath);
        doc.write(os);
        close(os);

        close(is);
        doc.close();
    }


    /**
     * 替换段落里面的变量
     * @param doc 要替换的文档
     * @param params 参数
     */
    private static void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;

        while (iterator.hasNext()) {
            para = iterator.next();
            replaceInPara(para, params);
        }
    }

    /**
     * 替换段落里面的变量
     * @param para 要替换的段落
     * @param params 参数
     */
    private static void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
        List<XWPFRun> runs;
        Matcher matcher;
        if (matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            System.out.println("2:"+runs);
            for (XWPFRun run : runs) {
                String runText = run.toString();
                matcher = matcher(runText);
                if (matcher.find()) {
                    while ((matcher = matcher(runText)).find()) {
                        runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
                    }
                    run.setText(runText, 0);
                }
            }
        }
    }

    /**
     * 替换表格里面的变量
     * @param doc 要替换的文档
     * @param params 参数
     */
    private static void replaceInTable(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFTable> iterator = doc.getTablesIterator();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        while (iterator.hasNext()) {
            table = iterator.next();
            rows = table.getRows();
            for (XWPFTableRow row : rows) {
                cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    paras = cell.getParagraphs();
                    for (XWPFParagraph para : paras) {
                        replaceInPara(para, params);
                    }
                }
            }
        }
    }

    /**
     * 替换页眉
     * @param doc
     * @param params
     */
    private static void replaceHeader(XWPFDocument doc, Map<String, Object> params){
        List<XWPFHeader> xwpfHeaderList = doc.getHeaderList();
        Iterator iterator = xwpfHeaderList.iterator();
        XWPFParagraph para;
        XWPFHeader xwpfHeader;
        while (iterator.hasNext()) {
            xwpfHeader = (XWPFHeader) iterator.next();
            List<XWPFParagraph> xwpfParagraphList = xwpfHeader.getParagraphs();
            for (XWPFParagraph xwpfParagraph : xwpfParagraphList) {
                para = xwpfParagraph;
                replaceInPara(para, params);
            }
        }
    }

    /**
     * 正则匹配字符串
     * @param str
     * @return
     */
    private static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(str);
    }

    /**
     * 关闭输入流
     * @param is
     */
    private static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流
     * @param os
     */
    private static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("company", "珠海格力有限公司珠海格力有限公司珠海格力有限公司珠海格力有限公司珠海格力有限公司珠海格力有限公司");
        params.put("author", "aaa");
        params.put("address", "湖北省武汉市政府");
        params.put("email", "4543516545646@fdff.ccsda");
        params.put("phone", "18186473860");
        params.put("workday", "7");
        params.put("pay", "1554");
        params.put("date", "1554年7月12日");
        params.put("year", "1554");
        // 模板路径
        String filePath = "C:/Users/ink/Desktop/test.docx";
        String outPath = "C:/Users/ink/Desktop/test1.docx";
        templateWrite(params, filePath, outPath, "");
    }
}
