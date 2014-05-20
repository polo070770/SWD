<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>Catalogo</title>
<link rel="stylesheet" type="text/css" href="${URLS.staticcontent}css/styles.css">
</head>
<body bgcolor="white">

<c:choose>
	<c:when test="${request.isUserInRole('Client')}">
		${user.name}
	</c:when>
	<c:otherwise>
	 <a href="${URLS.login}">login</a>
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
