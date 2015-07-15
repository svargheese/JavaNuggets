package com.svargheese.jnuggets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class WifiDataSharer extends AbstractHandler {

	private static final StringBuilder DATA = new StringBuilder("data");
	private static final String HTML_CONTENT = "<h1>Copy or Paste to share.</h1>"
			+ "\n<form method='POST'>"
			+ "\n<textarea id='data' name='data' onClick='this.setSelectionRange(0, this.value.length);' rows='4' cols='50'>%s</textarea>"
			+ "\n<br><input name='write' type='submit' value='Write'/>"
			+ "\n</form>";

	public WifiDataSharer() {

		super();
	}

	@Override
	public void handle(String arg0, Request baseRequest,
			HttpServletRequest arg2, HttpServletResponse r) throws IOException,
			ServletException {

		String data = (String) baseRequest.getParameter("data");
		boolean isWrite = "Write".equalsIgnoreCase((String) baseRequest
				.getParameter("write"));

		if (isWrite && data != null) {// Update

			System.out.println(String.format("Older data %s incoming data %s",
					WifiDataSharer.DATA == null ? "null" : WifiDataSharer.DATA,
					data));
			DATA.delete(0, WifiDataSharer.DATA.length());
			DATA.append(data);
			r.sendRedirect("/");
		} else { // Read

			r.setContentType("text/html;charset=utf-8");
			r.getWriter().println(String.format(HTML_CONTENT, DATA));
			r.setStatus(HttpServletResponse.SC_FOUND);
			baseRequest.setHandled(true);
		}
	}

	public static void main(String[] args) {

		try {

			Server server = new Server(8080);
			server.setHandler(new WifiDataSharer());
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
