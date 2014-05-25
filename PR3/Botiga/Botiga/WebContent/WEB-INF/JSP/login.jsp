<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<t:wrapper>
	<form class="form-signin" action="j_security_check"method="POST">
		<div class="logo-img"></div>
		<input type="hidden" name="token" value="${token_login}"/>
	  	<input type="text" class="form-control" name='j_username' placeholder="Usuario" autofocus="" value="">
		<input type="password" class="form-control"name='j_password' placeholder="Contraseña" value="">
	  	<button class="btn btn-lg btn-primary btn-block" type="submit">Entrar</button>
	</form>
</t:wrapper>