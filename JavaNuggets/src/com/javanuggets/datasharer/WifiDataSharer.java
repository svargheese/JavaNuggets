package com.javanuggets.datasharer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class WifiDataSharer extends AbstractHandler {

	private static StringBuilder data = new StringBuilder("data");

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

		if (isWrite && data != null) {

			System.out.println(String.format("Older data %s incoming data %s",
					WifiDataSharer.data == null ? "null" : WifiDataSharer.data,
					data));
			WifiDataSharer.data.delete(0, WifiDataSharer.data.length());
			WifiDataSharer.data.append(data);
			r.sendRedirect("/");
		}

		r.setContentType("text/html;charset=utf-8");
		r.getWriter().println("<h1>Copy or Paste to share.</h1>");
		r.getWriter().println("<form method='POST'>");
		r.getWriter()
				.println(
						"<input id='data' name='data' onClick='this.setSelectionRange(0, this.value.length);' type='textarea' value='"
								+ WifiDataSharer.data + "'/>");
		r.getWriter().println(
				"<input name='write' type='submit' value='Write'/>");
		r.getWriter().println("</form>");
		r.setStatus(HttpServletResponse.SC_FOUND);
		baseRequest.setHandled(true);
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
