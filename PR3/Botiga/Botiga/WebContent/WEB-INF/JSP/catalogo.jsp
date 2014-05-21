<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<title>Catalogo</title>
<link rel="stylesheet" type="text/css" href="${URLS.staticcontent}css/styles.css">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
</head>
<body >

<c:choose>
	<c:when test="${request.isUserInRole('Client')}">
		Hello ${user.name}! <br>
		${user.credit} €
	</c:when>
	<c:otherwise>
		<a href="${URLS.micuenta}">Mi cuenta</a>
		<br>
	  	<a href="${URLS.carrito}">Carrito</a>
	</c:otherwise>	
</c:choose>

<h1>Catalogo</h1>

<h2>
	<c:choose>
		<c:when test="${carrito != null}">
			${carrito.getNumItems()} 
		</c:when>
		<c:otherwise>
		  0 
		</c:otherwise>	
	</c:choose>
	items en el carro
</h2>
<table>
	<!--  for each en jstl mode -->
	<c:forEach var="item" items="${catalog}">
		<tr>
			<td><img src="${URLS.staticcontent}img/thumb/${item.image}" ></td>
			<td>${item.name}</td>
			<td><a href="${URLS.catalogo}?add=${item.id}">Añadir al carrito</a></td>
		</tr>
	</c:forEach>
</table>
</body>
</html>
