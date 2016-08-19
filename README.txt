# jBPM 5 session init servlet for BRMS 5.3.1

This is a WAR file which triggers REST access "business-central-server/rs/process/definitions" on BRMS startup in order to overcome the problem described in https://access.redhat.com/solutions/1989783

- Edit src/main/webapp/WEB-INF/web.xml <init-param> to meet your BRMS URL, username/password
- mvn clean package
- Copy target/jbpm5-session-init-servlet-1.0.0.war under $JBOSS_HOME/server/$PROFILE/deploy/deploy.last (create "deploy.last" directory if it doesn't exist)

When you start BRMS, logs would be like:

11:11:05,259 INFO  [TomcatDeployment] deploy, ctxPath=/jbpm5-session-init-servlet-1.0.0
11:11:05,317 INFO  [ProfileServiceBootstrap] Loading profile: ProfileKey@37f421b3[domain=default, server=default, name=default]
11:11:05,326 INFO  [STDOUT] SessionInitServlet.init()
11:11:05,332 INFO  [Http11Protocol] Starting Coyote HTTP/1.1 on http-127.0.0.1-8080
11:11:05,375 INFO  [AjpProtocol] Starting Coyote AJP/1.3 on ajp-127.0.0.1-8009
11:11:05,397 INFO  [ServerImpl] JBoss (Microcontainer) [5.2.0 (build: SVNTag=JBPAPP_5_2_0 date=201211232041)] Started in 47s:105ms
11:11:05,569 INFO  [STDOUT] http://localhost:8080/business-central-server/rs/process/definitions : result = 200
11:11:05,588 INFO  [STDOUT] http://localhost:8080/business-central-server/rs/identity/secure/j_security_check : result = 302
...
11:11:10,245 INFO  [STDOUT] http://localhost:8080/business-central-server/rs/process/definitions : result = 200
11:11:10,245 WARN  [HttpMethodBase] Going to buffer response body of large or unknown size. Using getResponseBodyAsStream instead is recommended.
11:11:10,246 INFO  [STDOUT] {"definitions":[{"id":"defaultPackage.helloTask","name":"helloTask","version":0,"packageName":"defaultPackage","deploymentId":"N/A","suspended":false,"diagramUrl":"http://localhost:8080/jboss-brms/org.drools.guvnor.Guvnor/package/defaultPackage/LATEST/defaultPackage.helloTask-image.png"},{"id":"defaultPackage.hello","name":"hello","version":0,"packageName":"defaultPackage","deploymentId":"N/A","suspended":false,"diagramUrl":"http://localhost:8080/jboss-brms/org.drools.guvnor.Guvnor/package/defaultPackage/LATEST/defaultPackage.hello-image.png"}]}
