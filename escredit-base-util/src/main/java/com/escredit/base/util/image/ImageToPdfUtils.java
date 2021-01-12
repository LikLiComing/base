package com.escredit.base.util.image;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.*;

import static org.apache.pdfbox.pdmodel.common.PDRectangle.*;

/**
 * 图片转PDF工具
 *
 * @author xuwucheng
 * @date 2020/12/4 16:49
 */
public class ImageToPdfUtils {

    public static byte[] convert(byte[] source){
        PDDocument document = new PDDocument();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PDPageContentStream contentStream;
        byte[] byteArray = null;

        try {
            PDPage page = new PDPage(A4);

            document.addPage(page);

            PDImageXObject pxImg = PDImageXObject.createFromByteArray(document, source, "temp");
            contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(pxImg, 0, 0, A4.getWidth(), A4.getHeight());
            contentStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                document.save(bos);

                bos.flush();
                byteArray = bos.toByteArray();

                bos.close();
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArray;
    }
}
