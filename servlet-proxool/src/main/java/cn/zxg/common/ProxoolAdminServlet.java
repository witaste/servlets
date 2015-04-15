package cn.zxg.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.logicalcobwebs.logging.Log;
import org.logicalcobwebs.logging.LogFactory;
import org.logicalcobwebs.proxool.ConnectionInfoIF;
import org.logicalcobwebs.proxool.ConnectionPoolDefinitionIF;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.Version;
import org.logicalcobwebs.proxool.admin.SnapshotIF;
import org.logicalcobwebs.proxool.admin.StatisticsIF;
import org.logicalcobwebs.proxool.admin.servlet.AdminServlet;

/**
 * 该类继承AdminServlet
 * 解决 java.io.CharConversionException: Not an ISO 8859-1 character 问题
 */
public class ProxoolAdminServlet extends AdminServlet {

	private static final long serialVersionUID = -1088591012339944571L;
	private static final Log LOG = LogFactory.getLog(ProxoolAdminServlet.class);
	protected static final String ACTION_LIST = "list";
	private static final String ACTION_STATS = "stats";
	protected static final String ACTION_CHART = "chart";
	protected static final String TYPE = "type";
	protected static final String TYPE_CONNECTIONS = "1";
	protected static final String TYPE_ACTIVITY_LEVEL = "2";
	private static final String STYLE_CAPTION = "text-align: right; color: #333333;";
	private static final String STYLE_DATA = "background: white;";
	private static final String STYLE_NO_DATA = "color: #666666;";
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat(
			"HH:mm:ss");
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(
			"0.00");
	private static final String LEVEL = "level";
	private static final String LEVEL_MORE = "more";
	private static final String LEVEL_LESS = "less";
	private static final String ACTION = "action";
	private static final String ALIAS = "alias";
	private static final String CONNECTION_ID = "id";

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Pragma", "no-cache");
		response.setCharacterEncoding("utf-8");
		String link = request.getRequestURI();

		String action = request.getParameter("action");
		if (action == null)
			action = "stats";

		String level = request.getParameter("level");
		String connectionId = request.getParameter("id");

		String alias = request.getParameter("alias");
		String[] aliases = ProxoolFacade.getAliases();
		if (alias == null) {
			if (aliases.length == 1)
				alias = aliases[0];
			else {
				action = "list";
			}

		}

		if (alias != null) {
			try {
				ProxoolFacade.getConnectionPoolDefinition(alias);
			} catch (ProxoolException e) {
				action = "list";
			}

		}
		PrintWriter out = response.getWriter();
		openHtml(out);
		try {
			if (action.equals("list")) {
				response.setContentType("text/html");
				doList(out, alias, link, level);
			} else if (action.equals("stats")) {
				response.setContentType("text/html");
				doStats(out, alias, link, level,
						connectionId);
			} else {
				LOG.error("Unrecognised action '" + action + "'");
			}
		} catch (ProxoolException e) {
			LOG.error("Problem", e);
		}
		out.println(
				"<div style=\"text-align: right; width: 550px; color: #333333;\">Proxool "
						+ Version.getVersion() + "</div>");
		closeHtml(out);
	}

	private void doStats(PrintWriter out, String alias, String link,
			String level, String connectionId) throws ProxoolException,
			IOException {
		doList(out, alias, link, level);
		doDefinition(out, alias, link);
		doSnapshot(out, alias, link, level, connectionId);
		doStatistics(out, alias, link);
	}

	private void doStatistics(PrintWriter out, String alias, String link)
			throws ProxoolException, IOException {
		StatisticsIF[] statisticsArray = ProxoolFacade.getStatistics(alias);
		ConnectionPoolDefinitionIF cpd = ProxoolFacade
				.getConnectionPoolDefinition(alias);

		for (int i = 0; i < statisticsArray.length; ++i) {
			StatisticsIF statistics = statisticsArray[i];
			out.print("<b>Statistics</b> from ");
			out.print(TIME_FORMAT.format(statistics.getStartDate()));
			out.print(" to ");
			out.print(TIME_FORMAT.format(statistics.getStopDate()));

			openTable(out);

			printDefinitionEntry(
					out,
					"Served",
					statistics.getServedCount()
							+ " ("
							+ DECIMAL_FORMAT.format(statistics
									.getServedPerSecond()) + "/s)");

			printDefinitionEntry(
					out,
					"Refused",
					statistics.getRefusedCount()
							+ " ("
							+ DECIMAL_FORMAT.format(statistics
									.getRefusedPerSecond()) + "/s)");

			printDefinitionEntry(
					out,
					"Average active time",
					DECIMAL_FORMAT.format(statistics.getAverageActiveTime() / 1000.0D)
							+ "s");

			StringBuffer activityLevelBuffer = new StringBuffer();
			int activityLevel = (int) (100.0D * statistics
					.getAverageActiveCount() / cpd.getMaximumConnectionCount());
			activityLevelBuffer.append(activityLevel);
			activityLevelBuffer.append("%<br/>");
			String[] colours = { "0000ff", "eeeeee" };
			int[] lengths = { activityLevel, 100 - activityLevel };
			drawBarChart(activityLevelBuffer, colours, lengths);
			printDefinitionEntry(out, "Activity level",
					activityLevelBuffer.toString());

			closeTable(out);
		}
	}

	private void drawBarChart(StringBuffer out, String[] colours, int[] lengths) {
		out.append("<table style=\"margin: 8px; font-size: 50%;\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>");

		int totalLength = 0;
		for (int i = 0; i < colours.length; ++i) {
			totalLength += lengths[i];
		}

		for (int j = 0; j < colours.length; ++j) {
			String colour = colours[j];
			int length = lengths[j];
			if (length > 0) {
				out.append("<td bgcolor=\"#");
				out.append(colour);
				out.append("\" width=\"");
				out.append(100 * length / totalLength);
				out.append("%\">&nbsp;</td>");
			}
		}
		out.append("</tr></table>");
	}

	private void doDefinition(PrintWriter out, String alias, String link)
			throws ProxoolException, IOException {
		ConnectionPoolDefinitionIF cpd = ProxoolFacade
				.getConnectionPoolDefinition(alias);

		out.print("<b>Defintition</b> for ");
		out.println(alias);
		openTable(out);

		printDefinitionEntry(out, "URL", cpd.getUrl());

		printDefinitionEntry(out, "Driver", cpd.getDriver());

		printDefinitionEntry(
				out,
				"Connections",
				cpd.getMinimumConnectionCount() + " (min), "
						+ cpd.getMaximumConnectionCount() + " (max)");

		printDefinitionEntry(
				out,
				"Prototyping",
				(cpd.getPrototypeCount() > 0) ? String.valueOf(cpd
						.getPrototypeCount()) : null);

		printDefinitionEntry(out, "Connection Lifetime",
				formatMilliseconds(cpd.getMaximumConnectionLifetime()));

		printDefinitionEntry(out, "Maximum active time",
				formatMilliseconds(cpd.getMaximumActiveTime()));
		printDefinitionEntry(out, "House keeping sleep time",
				(cpd.getHouseKeepingSleepTime() / 1000) + "s");

		printDefinitionEntry(out, "House keeping test SQL",
				cpd.getHouseKeepingTestSql());

		String fatalSqlExceptions = null;
		if ((cpd.getFatalSqlExceptions() != null)
				&& (cpd.getFatalSqlExceptions().size() > 0)) {
			StringBuffer fatalSqlExceptionsBuffer = new StringBuffer();
			Iterator i = cpd.getFatalSqlExceptions().iterator();
			while (i.hasNext()) {
				String s = (String) i.next();
				fatalSqlExceptionsBuffer.append(s);
				fatalSqlExceptionsBuffer.append((i.hasNext()) ? ", " : "");
			}
			fatalSqlExceptions = fatalSqlExceptionsBuffer.toString();
		}
		printDefinitionEntry(out, "Fatal SQL exceptions", fatalSqlExceptions);
		printDefinitionEntry(out, "Wrapper", cpd.getFatalSqlExceptionWrapper());

		printDefinitionEntry(out, "Statistics", cpd.getStatistics());

		closeTable(out);
	}

	private void doSnapshot(PrintWriter out, String alias, String link,
			String level, String connectionId) throws IOException,
			ProxoolException {
		boolean detail = (level != null) && (level.equals("more"));
		SnapshotIF snapshot = ProxoolFacade.getSnapshot(alias, detail);

		if (snapshot != null) {
			out.print("<b>Snapshot</b> at ");
			out.println(TIME_FORMAT.format(snapshot.getSnapshotDate()));
			openTable(out);

			printDefinitionEntry(out, "Start date",
					DATE_FORMAT.format(snapshot.getDateStarted()));

			StringBuffer connectionsBuffer = new StringBuffer();
			connectionsBuffer.append(snapshot.getActiveConnectionCount());
			connectionsBuffer.append(" (active), ");
			connectionsBuffer.append(snapshot.getAvailableConnectionCount());
			connectionsBuffer.append(" (available), ");
			if (snapshot.getOfflineConnectionCount() > 0) {
				connectionsBuffer.append(snapshot.getOfflineConnectionCount());
				connectionsBuffer.append(" (offline), ");
			}
			connectionsBuffer.append(snapshot.getMaximumConnectionCount());
			connectionsBuffer.append(" (max)<br/>");
			String[] colours = { "ff9999", "66cc66", "cccccc" };
			int[] lengths = {
					snapshot.getActiveConnectionCount(),
					snapshot.getAvailableConnectionCount(),
					snapshot.getMaximumConnectionCount()
							- snapshot.getActiveConnectionCount()
							- snapshot.getAvailableConnectionCount() };

			drawBarChart(connectionsBuffer, colours, lengths);
			printDefinitionEntry(out, "Connections",
					connectionsBuffer.toString());

			printDefinitionEntry(out, "Served",
					String.valueOf(snapshot.getServedCount()));

			printDefinitionEntry(out, "Refused",
					String.valueOf(snapshot.getRefusedCount()));

			if (!(detail)) {
				out.println("    <tr>");
				out.print("<td colspan=\"2\" align=\"right\"><a href=\"");
				out.print(link);
				out.print("?");
				out.print("alias");
				out.print("=");
				out.print(alias);
				out.print("&");
				out.print("level");
				out.print("=");
				out.print("more");
				out.println("\">more information</a></td>");
				out.println("    </tr>");
			} else {
				out.println("    <tr>");
				out.print("      <td width=\"200\" valign=\"top\" style=\"text-align: right; color: #333333;\">");
				out.print("Details");
				out.println("</td>");
				out.print("      <td style=\"color: #666666;\">");

				doSnapshotDetails(out, alias, snapshot, link, connectionId);

				out.println("</td>");
				out.println("    </tr>");

				long drillDownConnectionId = 0L;
				if (connectionId != null) {
					drillDownConnectionId = Long.valueOf(connectionId)
							.longValue();
					ConnectionInfoIF drillDownConnection = snapshot
							.getConnectionInfo(drillDownConnectionId);
					if (drillDownConnection != null) {
						out.println("    <tr>");
						out.print("      <td width=\"200\" valign=\"top\" style=\"text-align: right; color: #333333;\">");
						out.print("Connection #" + connectionId);
						out.println("</td>");
						out.print("      <td style=\"color: #666666;\">");

						doDrillDownConnection(out, drillDownConnection, link);

						out.println("</td>");
						out.println("    </tr>");
					}
				}

				out.println("    <tr>");
				out.print("<td colspan=\"2\" align=\"right\"><a href=\"");
				out.print(link);
				out.print("?");
				out.print("alias");
				out.print("=");
				out.print(alias);
				out.print("&");
				out.print("level");
				out.print("=");
				out.print("less");
				out.println("\">less information</a></td>");
				out.println("    </tr>");
			}

			closeTable(out);
		}
	}

	private void doSnapshotDetails(PrintWriter out, String alias,
			SnapshotIF snapshot, String link, String connectionId)
			throws IOException {
		long drillDownConnectionId = 0L;
		if (connectionId != null) {
			drillDownConnectionId = Long.valueOf(connectionId).longValue();
		}

		if ((snapshot.getConnectionInfos() != null)
				&& (snapshot.getConnectionInfos().length > 0)) {
			out.println("<table cellpadding=\"2\" border=\"0\">");
			out.println("  <tbody>");

			out.print("<tr>");
			out.print("<td style=\"font-size: 90%\">#</td>");
			out.print("<td style=\"font-size: 90%\" align=\"center\">born</td>");
			out.print("<td style=\"font-size: 90%\" align=\"center\">last<br>start</td>");
			out.print("<td style=\"font-size: 90%\" align=\"center\">lap<br>(ms)</td>");
			out.print("<td style=\"font-size: 90%\" width=\"90%\">&nbsp;thread</td>");
			out.print("</tr>");

			ConnectionInfoIF[] connectionInfos = snapshot.getConnectionInfos();
			for (int i = 0; i < connectionInfos.length; ++i) {
				ConnectionInfoIF connectionInfo = connectionInfos[i];

				if (connectionInfo.getStatus() != 0) {
					out.print("<tr>");

					out.print("<td bgcolor=\"#");
					if (connectionInfo.getStatus() == 2)
						out.print("ffcccc");
					else if (connectionInfo.getStatus() == 1)
						out.print("ccffcc");
					else if (connectionInfo.getStatus() == 3)
						out.print("ccccff");

					out.print("\" style=\"");

					if (drillDownConnectionId == connectionInfo.getId()) {
						out.print("border: 1px solid black;");
						out.print("\">");
						out.print(connectionInfo.getId());
					} else {
						out.print("border: 1px solid transparent;");
						out.print("\"><a href=\"");
						out.print(link);
						out.print("?");
						out.print("alias");
						out.print("=");
						out.print(alias);
						out.print("&");
						out.print("level");
						out.print("=");
						out.print("more");
						out.print("&");
						out.print("id");
						out.print("=");
						out.print(connectionInfo.getId());
						out.print("\">");
						out.print(connectionInfo.getId());
						out.print("</a>");
					}
					out.print("</td>");

					out.print("<td>&nbsp;");
					out.print(TIME_FORMAT.format(connectionInfo.getBirthDate()));
					out.print("</td>");

					out.print("<td>&nbsp;");
					out.print((connectionInfo.getTimeLastStartActive() > 0L) ? TIME_FORMAT
							.format(new Date(connectionInfo
									.getTimeLastStartActive())) : "-");
					out.print("</td>");

					out.print("<td align=\"right\">");
					if (connectionInfo.getTimeLastStopActive() > 0L) {
						out.print((int) (connectionInfo.getTimeLastStopActive() - connectionInfo
								.getTimeLastStartActive()));
					} else if (connectionInfo.getTimeLastStartActive() > 0L) {
						out.print("<font color=\"red\">");
						out.print((int) (snapshot.getSnapshotDate().getTime() - connectionInfo
								.getTimeLastStartActive()));
						out.print("</font>");
					} else {
						out.print("&nbsp;");
					}
					out.print("&nbsp;&nbsp;</td>");

					out.print("<td>&nbsp;");
					out.print((connectionInfo.getRequester() != null) ? connectionInfo
							.getRequester() : "-");
					out.print("</td>");

					out.println("</tr>");
				}
			}
			out.println("  </tbody>");
			out.println("</table>");
		} else {
			out.println("No connections yet");
		}
	}

	private void doDrillDownConnection(PrintWriter out,
			ConnectionInfoIF drillDownConnection, String link)
			throws IOException {
		out.print("<div style=\"font-size: 90%\">");
		out.print("proxy = ");
		out.print(drillDownConnection.getProxyHashcode());
		out.print("</div>");

		out.print("<div style=\"font-size: 90%\">");
		out.print("delegate = ");
		out.print(drillDownConnection.getDelegateHashcode());
		out.print("</div>");

		out.print("<div style=\"font-size: 90%\">");
		out.print("url = ");
		out.print(drillDownConnection.getDelegateUrl());
		out.print("</div>");
	}

	private void openHtml(PrintWriter out) throws IOException {
		out.println("<html><header><title>Proxool Admin</title></header><body BGCOLOR=\"#eeeeee\">");
	}

	private void closeHtml(PrintWriter out) throws IOException {
		out.println("</body></html>");
		out.flush();
		out.close();
	}

	private void openTable(PrintWriter out) throws IOException {
		out.println("<table width=\"550\" cellpadding=\"2\" cellspacing=\"2\" border=\"0\" bgcolor=\"#EEEEEE\" style=\"border: 1px solid black\">");
		out.println("  <tbody>");
	}

	private void closeTable(PrintWriter out) throws IOException {
		out.println("  </tbody>");
		out.println("</table>");
		out.println("<br/>");
	}

	private void printDefinitionEntry(PrintWriter out, String name,
			String value) throws IOException {
		out.println("    <tr>");
		out.print("      <td width=\"200\" valign=\"top\" style=\"text-align: right; color: #333333;\">");
		out.print(name);
		out.println("</td>");
		if (value != null) {
			out.print("      <td style=\"background: white;\">");
			out.print(value);
		} else {
			out.print("      <td style=\"color: #666666;\">off");
		}
		out.print("</td>");
		out.println("    </tr>");
	}

	private void doList(PrintWriter out, String alias, String link,
			String level) throws IOException, ProxoolException {
		out.print("<b>Pools</b>");
		openTable(out);

		String[] aliases = ProxoolFacade.getAliases();
		for (int i = 0; i < aliases.length; ++i) {
			String a = aliases[i];
			String style = "";
			if (a.equals(alias))
				style = "background: white;";

			ConnectionPoolDefinitionIF cpd = ProxoolFacade
					.getConnectionPoolDefinition(a);
			out.println("    <tr style=\"" + style + "\">");

			out.print("      <td width=\"200\" style=\"text-align: right; color: #333333;\">");
			out.print((a.equals(alias)) ? ">" : "&nbsp;");
			out.println("</td>");

			out.print("      <td><a href=\"" + link + "?" + "alias" + "=" + a
					+ "&" + "level" + "=" + level + "\">");
			out.print(a);
			out.println("</a> -> ");
			out.print(cpd.getUrl());
			out.println("</td>");
			out.println("    </tr>");
		}

		closeTable(out);
	}

	private String formatMilliseconds(int time) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.add(14, time);
		return TIME_FORMAT.format(c.getTime());
	}
}
