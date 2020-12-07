package com.escredit.base.util.image;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图片转PDF工具
 *
 * @author xuwucheng
 * @date 2020/12/4 16:49
 */
public class ImageToPdfUtils {

    public static void convertImgToPDF(String imagePath, String pdfPath) throws IOException {
        PDDocument document = new PDDocument();
        InputStream in = new FileInputStream(imagePath);
        BufferedImage bimg = ImageIO.read(in);
        float width = bimg.getWidth();
        float height = bimg.getHeight();
        PDPage page = new PDPage(new PDRectangle(width, height));
        document.addPage(page);
        PDImageXObject img = PDImageXObject.createFromFile(imagePath, document);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(img, 0, 0);
        contentStream.close();
        in.close();
        document.save(pdfPath);
        document.close();
    }

    public static byte[] convert(byte[] source) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        byte[] byteArray = null;

        try {
            //设置文档页边距
            document.setMargins(0,0,0,0);

            PdfWriter.getInstance(document, outputStream);
            //打开文档
            document.open();
            //获取图片的宽高
            Image image = Image.getInstance(source);
            image.scalePercent((document.getPageSize().getWidth() / image.getWidth()) * 100);
            float imageHeight = image.getScaledHeight();
            float imageWidth = image.getScaledWidth();
            //设置页面宽高与图片一致
            Rectangle rectangle = new Rectangle(imageWidth, imageHeight);
            document.setPageSize(rectangle);
            //图片居中
            image.setAlignment(Image.ALIGN_CENTER);
            //新建一页添加图片
            document.newPage();
            document.add(image);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
            try {
                outputStream.flush();
                byteArray = outputStream.toByteArray();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArray;
    }
}
