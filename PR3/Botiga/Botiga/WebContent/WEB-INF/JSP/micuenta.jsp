<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<t:wrapper>
	<h3>Saldo disponible ${client.getCreditRounded()} €</h3>
	<c:choose>
		<c:when test="${client.items.size() <= 0}">
			<div class="row">
				<h2>No tienes items en tu cuenta</h2>
			</div>	
		</c:when>
		<c:otherwise>
			<div class="row">
				<c:forEach var="item" items="${client.getItems()}">
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
                            <a href="${URLS.download}${item.id}" >
                            	<span class="glyphicon glyphicon-download-alt"></span>
                            	Descargar
                            </a>
                        </div>
                   	</div>
				</c:forEach>
			</div>
		</c:otherwise>
	</c:choose>
</t:wrapper>

