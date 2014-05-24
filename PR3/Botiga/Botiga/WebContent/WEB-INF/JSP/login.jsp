<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${URLS.staticcontent}css/styles.css">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
	<title>Accede para poder ver tu carrito y tu cuenta</title>
</head>
<body>


<h1>Entrada amb autentificació</h1>
	<c:forEach var="message" items="${messages.getAll()}">
		<h2>${message.text}</h2>
	</c:forEach>
<form method='POST' action='<%= response.encodeURL("j_security_check") %>'>
	<input type="hidden" name="token" value="${toke} }"/>
	Usuari:         <input type='text'     name='j_username'>
	<br>
	Paraula de pas: <input type='password' name='j_password'>
	<br>
	<input type="submit" value="Enviar">
</form>	


</body>
</html>
