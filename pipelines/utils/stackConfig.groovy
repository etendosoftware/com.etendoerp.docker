/**
 * Extracts the major Java version number from JAVA_HOME.
 * Supports paths like:
 *   /usr/lib/jvm/java-17-openjdk-amd64  → "17"
 *   /usr/lib/jvm/jdk.17.0.13            → "17"
 *   /usr/lib/jvm/java-11-openjdk-amd64  → "11"
 */
String getJavaVersionNumber() {
  def javaHome = env.JAVA_HOME ?: ''
  def matcher = javaHome =~ /(?:java-|jdk[.-])(\d+)/
  if (matcher.find()) {
    return matcher.group(1)
  }
  return '17'
}

String getTomcatFolder() {
  def tomcatFolder = sh(script: "basename ${TOMCAT_URL} .tar.gz", returnStdout: true).trim()
  return tomcatFolder
}

String generateStackMessage() {
  def tomcatFolder = env.TOMCAT_URL?.trim() ? getTomcatFolder() : null
  def javaVersion  = env.JAVA_HOME?.trim() ? getJavaVersionNumber() : null

  def msg = "<em>💡 The stack for this execution is:</em>\n<ul>\n"
  msg += tomcatFolder ? "<li><strong>Tomcat:</strong> ${tomcatFolder}</li>\n" : ''
  msg += javaVersion  ? "<li><strong>Java:</strong> ${javaVersion}</li>\n"   : ''
  msg += "</ul>\n"
  return msg
}

/**
 * Resolves the stack configuration based on whether the branch is a backport or not.
 *
 * Reads from Jenkins-managed environment variables (configure in the Jenkins job):
 *   JAVA_HOME_DEFAULT           — e.g. /usr/lib/jvm/java-17-openjdk-amd64
 *   JAVA_HOME_BACKPORT          — e.g. /usr/lib/jvm/java-11-openjdk-amd64
 *   TOMCAT_URL_DEFAULT          — full URL to the default Tomcat tarball
 *   TOMCAT_URL_BACKPORT         — full URL to the backport Tomcat tarball
 *   NEXT_CLASSIC_VERSION_DEFAULT — upper bound of etendo-core range for main builds (e.g. 26.2.0)
 *   NEXT_CLASSIC_VERSION_BACKPORT — upper bound for backport builds (e.g. 25.3.0)
 *   BASE_CORE_VERSION           — lower bound of etendo-core range (e.g. 24.4.0)
 *
 * @param fromBackport env.FROM_BACKPORT ("true"/"false")
 * @return Map with: stackType, javaHome, tomcatUrl, nextClassicVersion, baseCoreVersion
 */
Map resolveStackConfiguration(String fromBackport) {
  def stackType          = 'DEFAULT'
  def javaHome           = env.JAVA_HOME_DEFAULT
  def tomcatUrl          = env.TOMCAT_URL_DEFAULT
  def nextClassicVersion = env.NEXT_CLASSIC_VERSION_DEFAULT
  def baseCoreVersion    = env.BASE_CORE_VERSION

  if (fromBackport == env.TRUE) {
    stackType          = 'BACKPORT'
    javaHome           = env.JAVA_HOME_BACKPORT          ?: javaHome
    tomcatUrl          = env.TOMCAT_URL_BACKPORT         ?: tomcatUrl
    nextClassicVersion = env.NEXT_CLASSIC_VERSION_BACKPORT ?: nextClassicVersion
    echo "✅ Using BACKPORT stack configuration"
  } else {
    echo "✅ Using DEFAULT stack configuration"
  }

  return [
    stackType         : stackType,
    javaHome          : javaHome,
    tomcatUrl         : tomcatUrl,
    nextClassicVersion: nextClassicVersion,
    baseCoreVersion   : baseCoreVersion
  ]
}

return this
