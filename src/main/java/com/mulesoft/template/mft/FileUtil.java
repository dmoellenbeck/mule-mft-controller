package com.mulesoft.template.mft;

import java.io.FileInputStream;

import java.io.IOException;



import org.mule.api.MuleMessage;

import org.mule.api.transformer.TransformerException;

import org.mule.transformer.AbstractMessageTransformer;



public class FileUtil extends AbstractMessageTransformer {



@Override

public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
	
	String pth = (String)message.getInvocationProperty("readFilePath");
	if (pth==null) pth="";
	String fn = pth+"/"+(String)message.getInvocationProperty("readFileName");

	try {

		FileInputStream fis = new FileInputStream(fn);
		message.setPayload(fis);
		return message;

	} catch (IOException ee) {
		System.out.println("File IO Error: "+ee.toString());
	}

	return null;
}



}

