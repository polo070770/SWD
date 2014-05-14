package controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Downloader {

	private final int BUFSIZE = 4096;
	private String filePath;

	public Downloader(String path) {
		this.filePath = path;
	}

	public void download(String item, HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws ServletException, IOException {
		
		String filePath = getFilePath(item);

		File file = new File(filePath);
		int length = 0;
		ServletOutputStream outStream = response.getOutputStream();
		String mimetype = context.getMimeType(filePath);

		// sets response content type
		if (mimetype == null) {
			mimetype = "application/octet-stream";
		}
		response.setContentType(mimetype);
		response.setContentLength((int) file.length());
		String fileName = (new File(filePath)).getName();

		// sets HTTP header
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");

		byte[] byteBuffer = new byte[BUFSIZE];
		DataInputStream in = new DataInputStream(new FileInputStream(file));

		// reads the file's bytes and writes them to the response stream
		while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
			outStream.write(byteBuffer, 0, length);
		}

		in.close();
		outStream.close();
	}
	
	public void stream(String item, HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws ServletException, IOException {
		
		String filePath = getFilePath(item);
		File file = new File(filePath);
		int length = 0;
		ServletOutputStream outStream = response.getOutputStream();
		String mimetype = context.getMimeType(filePath);

		// sets response content type
		if (mimetype == null) {
			mimetype = "application/octet-stream";
		}
		response.setContentType(mimetype);

		byte[] byteBuffer = new byte[BUFSIZE];
		DataInputStream in = new DataInputStream(new FileInputStream(file));

		// reads the file's bytes and writes them to the response stream
		while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
			outStream.write(byteBuffer, 0, length);
		}

		in.close();
		outStream.close();
	}
	
	
	private String getFilePath(String item){
		String filePath ;
		if (item.contains(".png") || item.contains(".jpg")){
			filePath = this.filePath +"image/" + item;
		}else if(item.contains(".webm")){
			filePath = this.filePath +"video/" + item;
		}else{
			filePath = this.filePath +"file/" + item;
		}
		return filePath;
	}

}
