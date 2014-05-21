<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${URLS.staticcontent}css/styles.css">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<title>Carrito</title>
</head>
<body>
<h1>carrito</h1>
<c:if test="${response != null}">
	${response}
</c:if>
		Hello ${user.name}! <br>
		${user.credit} euros<br>
		<a href="${URLS.micuenta}">Mi cuenta</a>
		<br>
	  	<a href="${URLS.carrito}">Carrito</a>
		<br>
		<a href="${URLS.catalogo}">Catalogo</a>
		<br>
		<a href='catalogo?logoff=true'>logout</a>.
		<br>
	<c:if test="${carrito.items.size() <= 0}">
	No tienes items
	</c:if>	
	<c:if test="${carrito.items.size() > 0}">	
	<!--  for each en jstl mode -->
	<table>
	<c:forEach var="item" items="${carrito.getItems()}">
		<tr>
			<td><img src="${URLS.staticcontent}img/thumb/${item.image}" ></td>
			<td>${item.name}</td>
			<td>${item.price}</td>
		</tr>
	</c:forEach>
	</table>
	
	<br>
	Total ${carrito.getAmount()} eurus
	
	<form action="" method="POST">
		<input type="submit" value="comprar" />
	</form>
	</c:if>
	
</body>
</html>
