<html>
<head>
	<title>Accede para poder ver tu carrito y tu cuenta</title>
</head>
<body>
<font color = blue >

<h1>Entrada amb autentificació</h1>

<form method='POST' action='<%= response.encodeURL("j_security_check") %>'>
	Usuari:         <input type='text'     name='j_username'>
	<br>
	Paraula de pas: <input type='password' name='j_password'>
	<br>
	<input type="submit" value="Enviar">
</form>	


</body>
</html>
