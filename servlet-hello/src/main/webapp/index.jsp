<html>
<head>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
<script type="text/javascript">
	function change(type){
		document.getElementById("form").method=type;
		$("form").submit();
	}	
</script>
</head>
<body>
	<form id="form" action="/servlet-hello/HelloServlet">
		<input type="button" onclick="change('get')" value="get"></input>
		<input type="button" onclick="change('post')" value="post"></input>
	</form>
</body>
</html>
