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
	private String mediaPath;
	private String staticPath;

	public Downloader(String path, String staticPath) {
		this.mediaPath = path;
		this.staticPath = staticPath;
	}

	/**
	 * Funcion que fuerza la descarga de un archivo
	 * @param item
	 * @param request
	 * @param response
	 * @param context
	 * @throws ServletException
	 * @throws IOException
	 */
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
	
	/**
	 * Funcion que emite la descarga de un archivo, el navegador decide si lo muestra como tal
	 * o fuerza la descarga
	 * @param item
	 * @param request
	 * @param response
	 * @param context
	 * @throws ServletException
	 * @throws IOException
	 */
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
	
	/**
	 * Funcion que emite un contenido statico
	 * @param item
	 * @param request
	 * @param response
	 * @param context
	 * @throws ServletException
	 * @throws IOException
	 */
	public void streamStatic(String item, HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws ServletException, IOException {
		
		String filePath = this.staticPath + item;
		System.out.println(filePath);
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
	
	/**
	 * Parsea las urls para los distintos tipos de medios
	 * @param item
	 * @return
	 */
	private String getFilePath(String item){
		String filePath ;
		if (item.contains(".png") || item.contains(".jpg")){
			filePath = this.mediaPath +"image/" + item;
		}else if(item.contains(".webm")){
			filePath = this.mediaPath +"video/" + item;
		}else{
			filePath = this.mediaPath +"file/" + item;
		}
		return filePath;
	}

}
