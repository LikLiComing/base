package com.escredit.base.util.word;

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

    /**
     * 用一个docx文档作为模板，然后替换其中的内容，再写入目标文档中。
     * @throws Exception
     */
    public static void templateWrite(Map<String, Object> params) throws IOException {
        // 模板路径
        String filePath = "C:/Users/ink/Desktop/test.docx";
        InputStream is = new FileInputStream(filePath);
        XWPFDocument doc = new XWPFDocument(is);
        //替换段落里面的变量
        replaceInPara(doc, params);
        //替换表格里面的变量
        replaceInTable(doc, params);
        OutputStream os = new FileOutputStream("C:/Users/ink/Desktop/test1.docx");
        doc.write(os);

//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        doc.write(bos);
//        bos.flush();
//        bos.toByteArray();
//        close(bos);

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
            for (int i=0; i<runs.size(); i++) {
                XWPFRun run = runs.get(i);
                String runText = run.toString();
                matcher = matcher(runText);
                if (matcher.find()) {
                    while ((matcher = matcher(runText)).find()) {
                        runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
                    }
                    run.setText(runText,0);
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
     * 正则匹配字符串
     * @param str
     * @return
     */
    private static Matcher matcher(String str) {
        String regex = "\\$\\{(.*?)}";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
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
        templateWrite(params);
    }
}
