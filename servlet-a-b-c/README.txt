<�ؼ�����>
response.sendRedirect(reUrl)
<����>
��Ӧ֪ͨ�ͻ�����������reUrl��Ϊ����request,response��
-------------------------------------------------------------------
<��������>
Servlet1�У�
����
url1?theParameter=theValue
����
url2?theParameter=theValue
������������Servlet2��
���߽�theParameter�����Session�С�

-------------------------------�Ự--------------------------------------
<session-config>
	<session-timeout>1</session-timeout>
</session-config>
whole number of minutes
session-timeout Ϊһ�λỰ�����ζԻ������ʱ����������������Ự����(map.remove(key))��
��ͬ�����JSESSIONID��ͬ
ͬһ����������ڹ���JSESSIONID
----------------------------------------------------------------------
String kehuduan = request.getRequestedSessionId();
String fuwuqi = request.getSession().getId();

kehudu:ACA3649C063D9160130217C20319351B ��������session���ڣ������ڸ�JSESSIONID��ȡ����session�������µ�session��������sessionid���ظ��ͻ��ˣ�
fuwuqi:9BD4E2C33D3DC015E443EB2F23D0D13E

kehudu:9BD4E2C33D3DC015E443EB2F23D0D13E��������sessionδ���ڣ���ȡsession��
fuwuqi:9BD4E2C33D3DC015E443EB2F23D0D13E

���Ϊ��Map<k,v>
ͨ�� JSESSIONID ��ȡ session
session�д�Ÿ�JSESSIONID�ĸ�����Ϣ
ȡ����ʱ�������µ�session��������id ����JSESSIONID

ʲô�ǻỰ��
������ζԻ���



