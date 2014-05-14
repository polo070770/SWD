<%
  if (request.getParameter("logoff") != null) {
    session.invalidate();
  }
%>

<html>
<head>
<title>Index</title>
</head>
<body bgcolor="white">

<h1>Index</h1>
</body>
</html>
