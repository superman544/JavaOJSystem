package cn.superman.web.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

import cn.superman.constant.ConstantParameter;

public class OfficeUtils {

    /**
     * 将Excel文档转换为一个html页面
     *
     * @param inputPath 本地文件读取的路径加文件名，如：C:\\1.xls
     * @param outputPath 本地文件输出的路径加文件名，如：C:\\1.xls
     * @throws Exception
     */
    public static void excel2Html(final String inputPath, final String outputPath) throws Exception {

        ExcelToHtmlConverter converter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

        @SuppressWarnings("static-access")
        Document htmlDocument = converter.process(new File(inputPath));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        outStream.close();

        String content = new String(outStream.toByteArray());

        FileOutputStream outputStream = new FileOutputStream(outputPath);
        PrintWriter pw = new PrintWriter(outputStream);
        pw.write(content);
        pw.close();
    }

    /**
     * 将一个Word文档转换为一个html页面
     *
     * @param inputFilePath 想要读取的文件所在文件夹的路径，如：C:\\
     * @param inputFileName 想要读取的文件名字，如：1.xls
     * @param outputFilePath 准备写出的文件所在文件夹的路径，如：C:\\
     * @param outputFileName 准备写出的文件名字，如：1.xls
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void word2Html(final String inputFilePath, final String inputFileName, final String outputFilePath, final String outputFileName)
            throws IOException, ParserConfigurationException, TransformerException {

		InputStream input = new FileInputStream(inputFilePath + inputFileName);
		HWPFDocument wordDocument = new HWPFDocument(input);
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder()
						.newDocument());

        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            @Override
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                return suggestedName;
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);
        List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = pics.get(i);
                try {
                    pic.writeImageContent(new FileOutputStream(inputFilePath + pic.suggestFullFileName()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, ConstantParameter.DEFAULT_CHARSET_CODE);
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        outStream.close();

        String content = new String(outStream.toByteArray());

        FileOutputStream outputStream = new FileOutputStream(outputFilePath + outputFileName);
        PrintWriter pw = new PrintWriter(outputStream);
        pw.write(content);
        pw.close();
    }
}
