<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<title>Mi cuenta</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
</head>
<body bgcolor="white">
	
<h1>Mi cuenta</h1>
	<c:forEach var="message" items="${messages.getAll()}">
		<h2>${message.text}</h2>
	</c:forEach>
		${client.name}<br>
		${client.getCreditRounded()} €<br>
		<a href="${URLS.micuenta}">Mi cuenta</a>
		<br>
	  	<a href="${URLS.carrito}">Carrito</a>
		<br>
		<a href="${URLS.catalogo}">Catalogo</a>
		<br>
		<table>
		<c:forEach var="item" items="${client.getItems()}">
			<tr>
				<td><img src="${URLS.staticcontent}img/thumb/${item.image}" ></td>
				<td>${item.name}</td>
				<td><a a href="${URLS.download}${item.id}">Download</a></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
