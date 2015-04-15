官网 http://proxool.sourceforge.net

配置
<servlet>
  <servlet-name>ServletConfigurator</servlet-name>
  <servlet-class>org.logicalcobwebs.proxool.configuration.ServletConfigurator</servlet-class>
	  <init-param>
	    <param-name>xmlFile</param-name>
	    <param-value>WEB-INF/proxool.xml</param-value>
	  </init-param>
  <load-on-startup>1</load-on-startup>
</servlet>

调用
Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
connection = DriverManager.getConnection(url);
...
connection.close();

属性
prototype-count: 
If there are fewer than this number of connections available then we will build some more (assuming the maximum-connection-count is not exceeded). For example. Of we have 3 active connections and 2 available, but our prototype-count is 4 then it will attempt to build another 2. This differs from minimum-connection-count because it takes into account the number of active connections. minimum-connection-count is absolute and doesn't care how many are in use. prototype-count is the number of spare connections it strives to keep over and above the ones that are currently active. Default is 0.

others:(见文档）

问题：
1.proxool-0.8 监控admin输出时编码异常，无法识别中文（可重写该类）
2.连接被使用时状态变化：available -> active
  sql 执行完毕，状态变化：active -> available
  sql最大执行时间，超时thread 被kill 【maximum-active-time】
  当大于min 小于max 时，超时的connection被kill【maximum-connection-lifetime】