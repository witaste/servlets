package cn.zxg;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.Test;

public class TestServiceIsAvailable {

	@Test
	public void test() {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String path = "/mmsi/services/rest?_wadl";
		String[] arrHost = { "http://10.128.2.17:8001",
				"http://10.128.2.17:8002", "http://10.128.2.18:8001",
				"http://10.128.2.18:8002", "http://10.128.2.19:8001",
				"http://10.128.2.19:8002", "http://10.128.2.20:8001",
				"http://10.128.2.20:8002", "http://10.128.2.21:8001",
				"http://10.128.2.21:8002" };
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (String host : arrHost) {
			HttpGet get = new HttpGet(host + path);

			Map<String, String> map = new HashMap<String, String>();
			CloseableHttpResponse rs = null;
			try {
				rs = httpclient.execute(get);
				int code = rs.getStatusLine().getStatusCode();
				if (code == 200) {
					map.put("host", host);
					list.add(map);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (ConnectException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		System.out.println(JSONArray.fromObject(list).toString());
		// 断言
		Assert.assertTrue(list.size() == arrHost.length);

	}

}
