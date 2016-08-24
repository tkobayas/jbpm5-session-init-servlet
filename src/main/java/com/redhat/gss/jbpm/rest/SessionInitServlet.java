package com.redhat.gss.jbpm.rest;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class SessionInitServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(SessionInitServlet.class);

    @Override
    public void init() throws ServletException {

        ServletConfig sc = getServletConfig();
        final String targetUrl = sc.getInitParameter("targetUrl");
        final String authUrl = sc.getInitParameter("authUrl");
        final String username = sc.getInitParameter("username");
        final String password = sc.getInitParameter("password");

        // Even if you deploy this WAR in deploy/deploy.last, it will block web connector startup in deployment thread so HTTP access doesn't reach.
        // Need to split from the deployment thread
        Thread thread = new Thread(new Runnable() {

            public void run() {
                logger.info("SessionInitServlet.init()");

                HttpClient httpclient = new HttpClient();

                // 1st access
                HttpMethod theMethod = new GetMethod(targetUrl);
                try {
                    int result = httpclient.executeMethod(theMethod);
                    logger.info(targetUrl + " : result = " + result);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    theMethod.releaseConnection();
                }

                // do auth access
                PostMethod authMethod = new PostMethod(authUrl);
                NameValuePair[] data = { new NameValuePair("j_username", username),
                        new NameValuePair("j_password", password) };
                authMethod.setRequestBody(data);
                try {
                    int result = httpclient.executeMethod(authMethod);
                    logger.info(authUrl + " : result = " + result);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    authMethod.releaseConnection();
                }

                // 2nd access
                StringBuffer sb = new StringBuffer();
                try {
                    int result = httpclient.executeMethod(theMethod);
                    logger.info(targetUrl + " : result = " + result);
                    sb.append(theMethod.getResponseBodyAsString());
                    String rawResult = sb.toString();
                    logger.info(rawResult);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    theMethod.releaseConnection();
                }
            }
        });

        thread.start();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("This servelt is just used for load-on-startup");
    }
}
