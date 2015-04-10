<关键代码>
response.sendRedirect(reUrl)
<解释>
响应通知客户端重新请求reUrl，为两组request,response。
-------------------------------------------------------------------
<参数传递>
Servlet1中：
根据
url1?theParameter=theValue
生成
url2?theParameter=theValue
将参数传递至Servlet2。
或者将theParameter存放至Session中。

-------------------------------会话--------------------------------------
<session-config>
	<session-timeout>1</session-timeout>
</session-config>
whole number of minutes
session-timeout 为一次会话中两次对话的最大时间间隔，超出间隔，会话结束(map.remove(key))。
不同浏览器JSESSIONID不同
同一浏览器各窗口共用JSESSIONID
----------------------------------------------------------------------
String kehuduan = request.getRequestedSessionId();
String fuwuqi = request.getSession().getId();

kehudu:ACA3649C063D9160130217C20319351B （服务器session过期，不存在该JSESSIONID，取不到session，创建新的session，并将新sessionid返回给客户端）
fuwuqi:9BD4E2C33D3DC015E443EB2F23D0D13E

kehudu:9BD4E2C33D3DC015E443EB2F23D0D13E（服务器session未过期，获取session）
fuwuqi:9BD4E2C33D3DC015E443EB2F23D0D13E

理解为：Map<k,v>
通过 JSESSIONID 获取 session
session中存放该JSESSIONID的各种信息
取不到时，创建新的session，并将新id 赋给JSESSIONID

什么是会话？
包含多次对话。



