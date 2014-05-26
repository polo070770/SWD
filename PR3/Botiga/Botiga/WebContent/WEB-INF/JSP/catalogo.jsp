<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<t:wrapper>
	<div class="row">
		<h3 class="pull-right">
			<a href="${URLS.carrito}" class="btn btn-default btn-lg" role="button">
				<span class="glyphicon glyphicon-shopping-cart"></span>
				<c:choose>
					<c:when test="${carrito != null}">
						${carrito.getNumItems()} 
					</c:when>
					<c:otherwise>
					  0 
					</c:otherwise>	
				</c:choose>
				
			</a>		
		</h3>
	</div>
	<div class="row">
		<c:forEach var="item" items="${catalog}">
			<div class="col-xs-12 col-sm-6 col-lg-4 col-md-4 catalog-item">
		    	<div class="thumbnail">
		        	<a data-toggle="lightbox" href="${URLS.staticcontent}img/${item.image}" title="Ampliar imagen">
						<img src="${URLS.staticcontent}img/thumb/${item.image}" 
							alt="Ampliar imagen" data-type="image" class="img-responsive" >
					</a>
		            <div class="caption">
		            	<h4 class="pull-right">${item.getPriceRounded()} €</h4>
						<h4><a href="#">${item.name}</a></h4>
						<p title="${item.description}">${item.description}</p>
					</div>
					<a href="${URLS.addItem}${item.id}" >
		        		<span class="glyphicon glyphicon-ok"></span>
		        		Añadir al carrito
		        	</a>
		    	</div>
			</div>
		</c:forEach>
	</div>
</t:wrapper>