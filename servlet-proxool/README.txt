���� http://proxool.sourceforge.net

����
<servlet>
  <servlet-name>ServletConfigurator</servlet-name>
  <servlet-class>org.logicalcobwebs.proxool.configuration.ServletConfigurator</servlet-class>
	  <init-param>
	    <param-name>xmlFile</param-name>
	    <param-value>WEB-INF/proxool.xml</param-value>
	  </init-param>
  <load-on-startup>1</load-on-startup>
</servlet>

����
Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
connection = DriverManager.getConnection(url);
...
connection.close();

����
prototype-count: 
If there are fewer than this number of connections available then we will build some more (assuming the maximum-connection-count is not exceeded). For example. Of we have 3 active connections and 2 available, but our prototype-count is 4 then it will attempt to build another 2. This differs from minimum-connection-count because it takes into account the number of active connections. minimum-connection-count is absolute and doesn't care how many are in use. prototype-count is the number of spare connections it strives to keep over and above the ones that are currently active. Default is 0.

others:(���ĵ���

���⣺
1.proxool-0.8 ���admin���ʱ�����쳣���޷�ʶ�����ģ�����д���ࣩ
2.���ӱ�ʹ��ʱ״̬�仯��available -> active
  sql ִ����ϣ�״̬�仯��active -> available
  sql���ִ��ʱ�䣬��ʱthread ��kill ��maximum-active-time��
  ������min С��max ʱ����ʱ��connection��kill��maximum-connection-lifetime��