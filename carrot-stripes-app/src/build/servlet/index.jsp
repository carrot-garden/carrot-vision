<html>

<head>

	<title>Stripes</title>

	<!-- external -->
	<script src="http://www.java.com/js/deployJava.js"
		type="text/javascript"></script>

	<!-- internal -->
	<script src="artifact-descriptor.js"
		type="text/javascript"></script>

</head>

<body>
<%
out.println (
	"request directed to = "
	+ request.getServerName()
	+ ":" + request.getServerPort()
	+ "<br>"
);
out.println (
	"request received by = "
	+ request.getLocalName()
	+ " [" + request.getLocalAddr() + "]:"
	+ request.getLocalPort()
	+ "<br>"
);
out.println ("" + new java.util.Date());
%>

	<p>

	<b>Launch</b>

	<table>
		<tr>
			<td>applet (plugin default)</td>
			<td><a href="index-applet.jsp?plugin=default">launch applet</a>
			</td>
		</tr>
		<tr>
			<td>applet (plugin auto)</td>
			<td><a href="index-applet.jsp?plugin=auto">launch applet</a>
			</td>
		</tr>
		<tr>
			<td>applet (plugin V1)</td>
			<td><a href="index-applet.jsp?plugin=V1">launch applet</a>
			</td>
		</tr>
		<tr>
			<td>applet (plugin V2)</td>
			<td><a href="index-applet.jsp?plugin=V2">launch applet</a>
			</td>
		</tr>
		<tr>
			<td>application</td>
			<td>
			<script type="text/javascript">

			// note: using initJavaVersion
			deployJava.createWebStartLaunchButton(
					artifactDescriptor.jnlpFileApplication,
					artifactDescriptor.initJavaVersion
					);

			</script>
			</td>
		</tr>
	</table>

</body>

</html>
