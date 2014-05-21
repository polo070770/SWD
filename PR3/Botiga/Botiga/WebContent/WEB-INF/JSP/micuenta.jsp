<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<title>Mi cuenta</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
</head>
<body bgcolor="white">
	
<h1>Mi cuenta</h1>
<c:if test="${response != null}">
	${response}
</c:if>
		${user.name}<br>
		${user.credit} €<br>
		<a href="${URLS.micuenta}">Mi cuenta</a>
		<br>
	  	<a href="${URLS.carrito}">Carrito</a>
		<br>
		<a href="${URLS.catalogo}">Catalogo</a>
		<br>
		<table>
		<c:forEach var="item" items="${compras}">
			<tr>
				<td><img src="${URLS.staticcontent}img/thumb/${item.image}" ></td>
				<td>${item.name}</td>
				<td><a a href="${URLS.download}${item.url}">Download</a></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
