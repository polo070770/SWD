<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<link rel="stylesheet" type="text/css" href="${URLS.staticcontent}bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="${URLS.staticcontent}bootstrap/font-awesome/css/font-awesome.css">
	<link rel="stylesheet" type="text/css" href="${URLS.staticcontent}bootstrap/css/plugins/lightbox/ekko-lightbox.min.css">
	<link rel="stylesheet" type="text/css" href="${URLS.staticcontent}css/login.css">
	<link rel="stylesheet" type="text/css" href="${URLS.staticcontent}css/base.css">
	<title>
		<c:choose>
			<c:when test="${page_title!=null }">
			Botiga - ${page_title}
			</c:when>
			<c:otherwise>
			Botiga
			</c:otherwise>
		</c:choose>
	</title>
	<script src="${URLS.staticcontent}bootstrap/js/jquery-1.10.2.min.js"></script>	
</head>
<body>
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${URLS.base}">Botiga</a>
            </div>
			
			<!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav">
               		<li><a href="${URLS.catalogo}">Catalogo</a></li>
                    <li><a href="${URLS.carrito}">Carrito</a></li>
	                <c:if test="${client!=null}">
	                	<li><a href="${URLS.micuenta}">Mi cuenta</a></li>
	                    <li><a href="${URLS.logout}">Logout</a></li>
                    </c:if>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>
	<div class="content">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">
						<c:choose>
							<c:when test="${page_title!=null }">
							${page_title}
							</c:when>
							<c:otherwise>
							Botiga
							</c:otherwise>
						</c:choose>
					</h1>
					<c:forEach var="message" items="${messages.getAll()}">
						<div class="alert alert-${message.type}">
							<button type="button" class="close" data-dismiss="alert">×</button>
							${message.text}
						</div>
					</c:forEach>
				</div>
			</div>
			<!-- BODY -->
			<jsp:doBody/>	
			<!-- /BODY -->
		</div>
	</div>
	<script src="${URLS.staticcontent}bootstrap/js/bootstrap.min.js"></script>
	<script src="${URLS.staticcontent}bootstrap/js/plugins/lightbox/ekko-lightbox.min.clean.js"></script>
	<script>
		$(document).ready( function() {
			// Inicio lightbox
			$(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
				event.preventDefault();
				$(this).ekkoLightbox();
			});
		});
	
	</script>		
</body>
</html>