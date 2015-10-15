package com.itlao.repairservice.utils;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.R.fraction;
import android.R.integer;
import android.R.string;




public class JiexiZiliao {

	private static Document doc;
	static File  file;
	//构造函数初始化
	JiexiZiliao(File  file){
		
		JiexiZiliao.file=file;
	}
	
	public Document getDocument(File fileName) {  
        Document document = null;  
        try {  
            DocumentBuilderFactory factory = DocumentBuilderFactory  
                    .newInstance();  
            DocumentBuilder builder = factory.newDocumentBuilder();  
            document = builder.parse(fileName);  

        } catch (Exception e) {  
            e.printStackTrace();  
        }  

        return document;  
    }  
	/**
	 * @param args
	 */
	public  void JieXi() {
		// TODO Auto-generated method stub

	
		JiexiZiliao.file=file;
		try {
			/*DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
			//从DOM工厂中获得DOM解析器
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();//把要解析的xml文档读入DOM解析器  
			doc=dbBuilder.parse(file);*/
			
			doc=getDocument(file);
			/*String str_password=getValue("password");//密码
			String str_isshifu=getValue("isshifu");//是否师傅
			String str_username=getValue("username");//用户名
			String str_issavepsw=getValue("issavepsw");//是否保存密码
			String str_sex=getValue("sex");//性别
			String str_phone=getValue("phone");//电话号码
*/			
			ArrayList<savedShiFu> ssfArrayList=getSavedShifu("savedshifu");//获取保存的师傅资料
			for(savedShiFu ssf:ssfArrayList) {
				String username=ssf.username;
				int fff=ssf.items.size();
				for(WenTi wt:ssf.items) {
					String statement=wt.statement;
					String time=wt.time;
					System.out.println(statement);
				}
			}
			
			NodeList nList=doc.getElementsByTagName("questions");
			NodeList messages=doc.getElementsByTagName("messages");
			
			
			
			//遍历该集合（信息）
			for(int i=0;i<messages.getLength();i++){
			Element node=(Element)messages.item(i);
			//String string=node.getAttribute("id");
			NodeList chiList;
			if((chiList=node.getChildNodes())!=null) {
				int f=chiList.getLength();
				for(int j=0;j<chiList.getLength();j++){  
	                if(chiList.item(j).getNodeType()==Node.ELEMENT_NODE){  
	                	
	                    if("message".equals(chiList.item(j).getNodeName())){  
	                        
	                        Element node_chil=(Element)chiList.item(j);
	                        
	                        String statement=node_chil.getAttribute("content");
	                        String time=node_chil.getAttribute("time");
	                        /*node_chil.setAttribute("time", "2019-12-12");
	                        String time2=node_chil.getAttribute("time");
	                        modifyXmlFile(doc, file);*/
	                        
	                    }
	                }  
	            }
			 }
			}
			
			//遍历该集合（问题）
			for(int i=0;i<nList.getLength();i++){
			Element node=(Element)nList.item(i);
			//String string=node.getAttribute("id");
			NodeList chiList;
			if((chiList=node.getChildNodes())!=null) {
				int f=chiList.getLength();
				for(int j=0;j<chiList.getLength();j++){  
	                if(chiList.item(j).getNodeType()==Node.ELEMENT_NODE){  
	                	
	                    if("question".equals(chiList.item(j).getNodeName())){  
	                        
	                        Element node_chil=(Element)chiList.item(j);
	                        
	                        String statement=node_chil.getAttribute("statement");
	                        String state=node_chil.getAttribute("state");
	                        String time=node_chil.getAttribute("time");
	                        String photo=node_chil.getAttribute("photo");
	                        String recordvoice=node_chil.getAttribute("recordvoice");
	                        
	                        System.out.println(statement);
	                    }
	                }  
	            }
			 }
			}
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			String string=e1.toString();
			e1.printStackTrace();
			
		} 
		
	}

	/** 
     * 添加一个新的消息 
     */  
    public void addMessageNewNode(String time, String content) {  
        try {  
            Document document = getDocument(file);  
            NodeList nodeList = document.getElementsByTagName("messages");  
        
            Element element = document.createElement("message"); 
            element.setAttribute("time", time);
            element.setAttribute("content", content);
            nodeList.item(0).appendChild(element);
            // 此时真正的处理将新数据添加到文件中（磁盘）  
            modifyXmlFile(document, file);
           
        } catch (Exception e) {  
            e.printStackTrace();  
        }  

         
    }  
	/** 
     * 删除一个节点 
     */  
    public void deleteNode(String name) {
        
        doc=getDocument(file);
        NodeList nodeList = doc.getElementsByTagName("name");  
        for (int i = 0; i < nodeList.getLength(); i++) {  
            String value = nodeList.item(i).getFirstChild()  
                    .getTextContent();  
            if (name != null && name.equalsIgnoreCase(value)) {  
                Node parentNode = nodeList.item(i).getParentNode();  
                doc.getFirstChild().removeChild(parentNode);  
            }  
        }  
        modifyXmlFile(doc,file);  
    }  

    /** 
     * 根据name修改某个节点的内容 
     */  
    public void updateNode(String name) {  
    	doc=getDocument(file);  
        NodeList nodeList = doc.getElementsByTagName("name");  
        for (int i = 0; i < nodeList.getLength(); i++) {  
            String value = nodeList.item(i).getFirstChild()  
                    .getTextContent();  
            if (name != null && name.equalsIgnoreCase(value)) {  
                Node parentNode = nodeList.item(i).getParentNode();  
                NodeList nl = parentNode.getChildNodes();  
                for (int j = 0; j < nl.getLength(); j++) {  
                    String modifyNode = nl.item(j).getNodeName();  
                    if (modifyNode.equalsIgnoreCase("age")) {  
                        nl.item(j).getFirstChild().setTextContent("25");  
                    }  
                }  
            }  
        }  
        modifyXmlFile(doc, file);  
    }  

    /** 
     * 保存密码 
     */  
    public void SavedPassWord() {  
    	doc=getDocument(file);  
        NodeList nodeList = doc.getElementsByTagName("issavepsw");
        nodeList.item(0).getFirstChild().setTextContent("true");
       
        modifyXmlFile(doc, file);  
    }  
    /** 
     * 取消保存密码 
     */  
    public void notSavedPassWord() {  
    	doc=getDocument(file);  
        NodeList nodeList = doc.getElementsByTagName("issavepsw");
        nodeList.item(0).getFirstChild().setTextContent("false");
       
        modifyXmlFile(doc, file);  
    }  
	/** 
     * 将改动持久到文件 
     */  
    public static void modifyXmlFile(Document doc, File file) {  
        try {  
            TransformerFactory tf = TransformerFactory.newInstance();  
            Transformer tfer = tf.newTransformer();  
            DOMSource dsource = new DOMSource(doc);  
           // StreamResult sr = new StreamResult(new File("student.xml"));  
            StreamResult sr = new StreamResult(file); 
            tfer.transform(dsource, sr);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
	
	public  String  getValue(String string) {
		String str = null;
		doc=getDocument(file);
		NodeList nList=doc.getElementsByTagName(string);
		//for(int i=0;i<nList.getLength();i++){
			Element node=(Element)nList.item(0);
			str=node.getFirstChild().getNodeValue();
		//}
		return str;
	}
	public  ArrayList<savedShiFu>  getSavedShifu(String string) {
		ArrayList<savedShiFu> items_savedshifu = null;
		savedShiFu object= null;
		doc=getDocument(file);
		NodeList List=doc.getElementsByTagName(string);
		NodeList nList = null;
		//int dd=List.getLength();
		for(int j=0;j<List.getLength();j++){  
            if(List.item(j).getNodeType()==Node.ELEMENT_NODE){ 
		 nList=List.item(j).getChildNodes();
				 
            }
		}
		//int dd2=nList.getLength();
		items_savedshifu=new ArrayList<savedShiFu>();
		//师傅集合
		for(int i=0;i<nList.getLength();i++){
		if(nList.item(i).getNodeType()==Node.ELEMENT_NODE){	
		Element node=(Element)nList.item(i);
		String str_username=node.getAttribute("username");//用户名
		String str_name=node.getAttribute("name");//真实名字
		String str_rank=node.getAttribute("rank");//等级
		String str_expert=node.getAttribute("expert");//擅长
		String str_tyle=node.getAttribute("tyle");//师傅类型
		String str_idcard=node.getAttribute("idcard");//身份证号码
		
		object=null;
		object=new savedShiFu();
		object.savedShiFu(str_username, str_name, str_rank, str_expert, str_tyle, str_idcard);
		
		
		NodeList chiList;
		//师傅的问题
		if((chiList=node.getChildNodes())!=null) {
			//多个问题
			int ddd=chiList.getLength();
			for(int k=0;k<chiList.getLength();k++){  
                if(chiList.item(k).getNodeType()==Node.ELEMENT_NODE){  
                	
                    if("question".equals(chiList.item(k).getNodeName())){  
                        
                        Element node_chil=(Element)chiList.item(k);
                        
                        String statement=node_chil.getAttribute("statement");//问题描述
                        String state=node_chil.getAttribute("state");//问题状态
                        String time=node_chil.getAttribute("time");//问题时间
                        String photo=node_chil.getAttribute("photo");//问题图片
                        String recordvoice=node_chil.getAttribute("recordvoice");//问题录音
                        
                        object.savedWenti(statement, state, time, photo, recordvoice);
                        System.out.println(statement);
                    }
                }  
            }
		  }
		
		}
	 }
		items_savedshifu.add(object);
		return items_savedshifu;
	}
}




//储存师傅的类
class savedShiFu{
	String username, name, rank, expert, tyle, idcard;
	public void savedShiFu(String username,String name,String rank,String expert,String tyle,String idcard)
	   {
		this.username=username;
		this.name=name;
		this.rank=rank;
		this.expert=expert;
		this.tyle=tyle;
		this.idcard=idcard;
	   }
	String statement, state, time, photo, recordvoice;
	ArrayList<WenTi> items=new ArrayList<WenTi>();
	public void savedWenti(String statement,String state,String time,String photo,String recordvoice) {
		
		WenTi wt=new WenTi(statement,state,time,photo,recordvoice);
		items.add(wt);
	}
	
	/*private void wneti(String statement,String state,String time,String photo,String recordvoice) {
		
		
	}*/
}

//储存问题的类
class WenTi{
	String statement, state, time, photo, recordvoice;
    WenTi(String statement,String state,String time,String photo,String recordvoice) {
		
    	this.statement=statement;
		this.state=state;
		this.time=time;
		this.photo=photo;
		this.recordvoice=recordvoice;
	}
}