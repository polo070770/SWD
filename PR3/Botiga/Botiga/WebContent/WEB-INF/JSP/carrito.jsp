<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<t:wrapper>
	<h3>Saldo disponible ${client.getCreditRounded()} €</h3>
	<c:choose>
		<c:when test="${carrito.items.size() <= 0}">
			<h2>No tienes items en el carro</h2>
		</c:when>
		<c:otherwise>
			<div class="row">
				<h3 class="pull-right">Total carrito ${carrito.getAmountRounded()} €</h3>
			</div>
			<!--  for each en jstl mode -->
			<div class="row">
				<c:forEach var="item" items="${carrito.getItems()}">
					<div class="col-xs-12 col-sm-6 col-lg-4 col-md-4 catalog-item">
                        <div class="thumbnail">
                        <a data-toggle="lightbox" href="${URLS.staticcontent}img/${item.image}" title="Ampliar imagen">
								<img src="${URLS.staticcontent}img/thumb/${item.image}" alt="Ampliar imagen" data-type="image" class="img-responsive" >
							</a>
                            
                            <div class="caption">
                                <h4 class="pull-right">${item.getPriceRounded()} €</h4>
                                <h4><a href="#">${item.name}</a></h4>
                                <p>${item.description}</p>
                            </div>
                            <a href="${URLS.removeItem}${item.id}" class="red">
                            	<span class="glyphicon glyphicon-remove"></span>
                            	Quitar del carrito
                           	</a>
                        </div>
                   	</div>
				</c:forEach>
			</div>

				<form action="${URLS.carrito}" method="POST">
					<input type="hidden" name="token_carrito" value="${token_carrito}"/>
					<button class="btn btn-lg btn-primary btn-block" type="submit">Comprar</button>
				</form>
		</c:otherwise>
	</c:choose>
</t:wrapper>
