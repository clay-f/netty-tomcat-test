package com.f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/hello", loadOnStartup = 1)
public class HelloServlet extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(HelloServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("接收 get 请求, params: {}", req.getParameterMap());
        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.println("hello servlet");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("get 请求完毕...");
        }
    }
}
