package com.xkw.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.w3c.dom.Document;


@SuppressWarnings("deprecation")
public class ExportUtil  
{  
    private HSSFWorkbook wb = null;  
  
    private HSSFSheet sheet = null;  
    private static final String LOWER_CHAR = "abcdefghijkimlopqrstuvwxyz";
    /**
	 * 生成指定长度的随机字符串
	 * @param count
	 * @return
	 */
	public static String makeRandomStr(int count) {
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for(int k=0; k<count; k++) {
			char ch = LOWER_CHAR.charAt(random.nextInt(LOWER_CHAR.length()-1));
			buf.append(ch);
		}
		return buf.toString();
	}
    
    /**
     * 转换为html
     * @param fileName
     * @param outPutFile
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private static int k = 0;
    @SuppressWarnings("rawtypes")
	public static String convert2Html(String fileName, String outPutFile)  
            throws TransformerException, IOException, ParserConfigurationException {
         
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));      
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());  
        final Map<Integer, Object> map = new HashMap<>();
        k = 0;
        wordToHtmlConverter.setPicturesManager(new PicturesManager()  
        {  
            /**
             * 按建议名称保存图片
             */
            public String savePicture( byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches )  
            {  	
            	String datedir=FileUtil.getDateMonthDir();
            	File temp=new File(Const.BASE_DIR+Const.UPLOAD_IMAGES_PATH + datedir);
            	if(!temp.exists()){
            		temp.mkdirs();
            	}
            	suggestedName = datedir+"/"+Encodes.encodeByMD5(suggestedName+System.currentTimeMillis()+makeRandomStr(6))+"."+pictureType.toString().toLowerCase();
            	map.put(k++, suggestedName);
                return Const.BASE_URL+Const.UPLOAD_IMAGES_PATH+suggestedName;
            }  
        });
        wordToHtmlConverter.processDocument(wordDocument);  
        //save pictures  
        List pics = wordDocument.getPicturesTable().getAllPictures();  
        if( pics!=null ){  
            for(int i=0; i<pics.size(); i++){  

                Picture pic = (Picture)pics.get(i);
                try {
                	String suggestedName = (String) map.get(i);
                    pic.writeImageContent(new FileOutputStream(Const.BASE_DIR+Const.UPLOAD_IMAGES_PATH+suggestedName));  
                } catch (FileNotFoundException e) {  
                    e.printStackTrace();
                }    
            }  
        } 
        Document htmlDocument = wordToHtmlConverter.getDocument();  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        DOMSource domSource = new DOMSource(htmlDocument);  
//        System.out.println();
        StreamResult streamResult = new StreamResult(out);  
//        domSource.
        TransformerFactory tf = TransformerFactory.newInstance();  
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");  
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");  
        serializer.setOutputProperty(OutputKeys.METHOD, "html");  
//        serializer.setOutputProperty(OutputKeys., value);
        serializer.transform(domSource, streamResult);
        out.close();
         writeFile(new String(out.toByteArray()), outPutFile); 
         
        return new String(out.toByteArray());
    }
    
    /**
     * 将内容写入指定路径下的<a href="https://www.baidu.com/s?wd=html%E6%96%87%E4%BB%B6&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1YLuyD3m1fzrH99PWuWujFh0ZwV5Hcvrjm3rH6sPfKWUMw85HfYnjn4nH6sgvPsT6K1TL0qnfK1TL0z5HD0IgF_5y9YIZ0lQzqlpA-bmyt8mh7GuZR8mvqVQL7dugPYpyq8Q1D1PW6knHTvnWmdP163nH03rHb" target="_blank" class="baidu-highlight">html文件</a>
     * @param content
     * @param path
     */    
    public static void writeFile(String content, String path) {  
        FileOutputStream fos = null;  
        BufferedWriter bw = null;  
        try {  
            File file = new File(path);  
            fos = new FileOutputStream(file);  
            bw = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));  
            bw.write(content);
        } catch (FileNotFoundException fnfe) {  
            fnfe.printStackTrace();  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        } finally {  
            try {  
                if (bw != null)  
                    bw.close();  
                if (fos != null)  
                    fos.close();  
            } catch (IOException ie) {  
            }  
        }  
    }
    
    /**
     * 解析word文档中的字符串
     * 并以字符串形式返回文档的内容
     * @param filePath
     * @return
     */
    public static String parseWordDocFile(String filePath) {
    	OPCPackage opcPackage = null;
    	InputStream is = null;
        try {
        	String ext = filePath.substring(filePath.lastIndexOf(".")+1);
        	if("doc".equals(ext.toLowerCase())) {
        		is = new FileInputStream(new File(filePath));
        		WordExtractor ex = new WordExtractor(is);
        		return ex.getText();
        	} else if("docx".equals(ext.toLowerCase())) {
        		opcPackage = POIXMLDocument.openPackage(filePath);
        		POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
        		return extractor.getText();
        	} else {
        		return "DOC_NULL";
        	}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	try {
	    		if(opcPackage!=null) {
	    			opcPackage.close();
	    		}
	    		if(is!=null) {
	    			is.close();
	    		}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
        return null;
     }
  
    /** 
     * @param wb 
     * @param sheet 
     */  
    public ExportUtil( HSSFWorkbook wb, HSSFSheet sheet)   
    {  
        this.wb = wb;  
        this.sheet = sheet;  
    }  
  
    /** 
     * 合并单元格后给合并后的单元格加边框 
     *  
     * @param region 
     * @param cs 
     */  
    public void setRegionStyle(CellRangeAddress region, HSSFCellStyle cs)  
    {  
  
        int toprowNum = region.getFirstRow();  
        for (int i = toprowNum; i <= region.getLastRow(); i++)  
        {  
        	HSSFRow row = sheet.getRow(i);  
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++)  
            {  
                HSSFCell cell = row.getCell(j);// HSSFCellUtil.getCell(row,  
                                                // (short) j);  
                cell.setCellStyle(cs);  
            }  
        }  
    }  
  
    /** 
     * 设置表头的单元格样式 
     *  
     * @return 
     */  
    public HSSFCellStyle getHeadStyle()  
    {  
        // 创建单元格样式  
    	HSSFCellStyle cellStyle = wb.createCellStyle();  
        // 设置单元格的背景颜色为淡蓝色  
        cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);  
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        // 设置单元格居中对齐  
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 设置单元格垂直居中对齐  
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 创建单元格内容显示不下时自动换行  
        cellStyle.setWrapText(true);  
        // 设置单元格字体样式  
        HSSFFont font = wb.createFont();  
        // 设置字体加粗  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        font.setFontName("宋体");  
        font.setFontHeight((short) 200);  
        cellStyle.setFont(font);  
        // 设置单元格边框为细线条  
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        return cellStyle;  
    }  
  
    /** 
     * 设置表体的单元格样式 
     *  
     * @return 
     */  
    public HSSFCellStyle getBodyStyle()  
    {  
        // 创建单元格样式  
        HSSFCellStyle cellStyle = wb.createCellStyle();  
        // 设置单元格居中对齐  
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 设置单元格垂直居中对齐  
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 创建单元格内容显示不下时自动换行  
        cellStyle.setWrapText(true);  
        // 设置单元格字体样式  
        HSSFFont font = wb.createFont();  
        // 设置字体加粗  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        font.setFontName("宋体");  
        font.setFontHeight((short) 200);  
        cellStyle.setFont(font);  
        // 设置单元格边框为细线条  
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);  
//        cellStyle
        return cellStyle;  
    }  
    public static void main(String arg[]){
    	String file = "E:\\qq1.doc";
    	try {
    		convert2Html(file,"E:\\hello.html");
//			System.out.println(convert2Html(file,"E:\\hello.html"));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}  