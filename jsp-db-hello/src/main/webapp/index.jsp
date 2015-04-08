<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.sql.*"%>
<html>
<body>
	<%
		// 初始化Oracle驱动
		Class.forName("oracle.jdbc.driver.OracleDriver", true, this.getClass().getClassLoader());
		// 建立会话 
		String url = "jdbc:oracle:thin:@172.16.50.67:1521:orcl";
		String username = "e_channel";
		String password = "e_channel_test";
		Connection conn = DriverManager.getConnection(url, username,password);
		// PL/SQL Developer | SQL Window   
		Statement stmt = conn.createStatement();
		// 语句 
		String sql = "select * from zxg";
		ResultSet rs = stmt.executeQuery(sql);
	%>
	<table border=1>
		<thead>
			<tr>
				<th>ID</th>
				<th>NAME</th>
				<th>LOGON_TIME</th>
			</tr>
		</thead>
		<tbody>
	<%
		// 打印
		while (rs.next()) {
	%>
			<!-- 字段名不区分大小写 -->
			<tr>
				<td><%=rs.getString("ID")%></td>
				<td><%=rs.getString("NAME")%></td>
				<td><%=rs.getTimestamp("LOGON_TIME")%></td>
			</tr>
	<%
		}
		rs.close();
		stmt.close();
		conn.close();
	%>
		</tbody>
	</table>
</body>
</html>