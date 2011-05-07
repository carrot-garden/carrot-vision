
<html>

<head>

	<title>Stipes Applet</title>

	<!-- external -->
	<script src="http://www.java.com/js/deployJava.js"
		type="text/javascript"></script>

	<!-- internal -->
	<script src="browser-detect.js" type="text/javascript"></script>
	<script src="artifact-descriptor.js" type="text/javascript"></script>
	<script src="applet-descriptor.js" type="text/javascript"></script>
	<script src="pivot-descriptor.js" type="text/javascript"></script>

</head>

<body>

	<script type="text/javascript">

	//////////////////////////

	// currently mac os x jnlp works by default only on chrome
	var isBrokenMac = ( (browserDetect.OS == 'Mac') && (browserDetect.browser != 'Chrome') );

	// currently windows jnlp does not work for safari
	var isBrokenWin = ( (browserDetect.OS == 'Windows') && (browserDetect.browser == 'Safari') );

	function getUrlVars() {
		var vars = {};
		var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
			vars[key] = value;
		});
		return vars;
	}

	/////////////////////////

	// read plugin detect mode
	var plugin = getUrlVars()["plugin"]

	// force OLD browser plugin mode;
	if(plugin == 'V1'){
		usePluginV1();
	}

	// force NEW browser plugin mode;
	if(plugin == 'V2'){
		usePluginV2();
	}

	// detect browser mode in script
	if(plugin == 'auto'){
		if( isBrokenMac || isBrokenWin ){
			usePluginV1();
		} else {
			usePluginV2();
		}
	}

	// detect browser mode
	if(plugin == 'default'){
		// let browser choose own compatibility mode
	}

	//////////////////////////

	// note: using initJavaVersion
	deployJava.runApplet(
			appletAttributes,
			appletParameters,
			artifactDescriptor.initJavaVersion
			);

	</script>

</body>

</html>
