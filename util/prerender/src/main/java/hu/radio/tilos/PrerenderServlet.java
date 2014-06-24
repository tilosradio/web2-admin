package hu.radio.tilos;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;

public class PrerenderServlet extends HttpServlet {

    String workDir = "/tmp/prerender";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        checkWorkDir();

        String command = workDir + "/node_modules/phantomjs/bin/phantomjs " + workDir + "/prerender.js http://tilos.hu/" + req.getRequestURI();
        resp.setCharacterEncoding("UTF-8");
        resp.addHeader("Content-Type", "text/html; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        String html = execute(command).toString();
        html = html.replaceAll("href=\"/\"", "href=\"http://tilos.hu/\"");
        html = html.replaceAll("<script.*/script>", "");
        resp.getWriter().write(html);
    }

    private String execute(String command) {
        try {
            String line;
            Process p = Runtime.getRuntime().exec(command, new String[0], new File(workDir));
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder b = new StringBuilder();
            while ((line = input.readLine()) != null) {
                //System.out.println(line);
                b.append(line);
                b.append("\n");
            }
            input.close();
            return b.toString();
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    private void checkWorkDir() {
        File f = new File(workDir);
        if (!f.exists()) {
            f.mkdirs();
        }
        extract("package.json", f);
        extract("prerender.js", f);
        if (!new File(f, "node_modules").exists()) {
            execute("npm update");
        }
    }

    private void extract(String resourceName, File f) {
        try (OutputStream out = new FileOutputStream(new File(f, resourceName))) {
            try (InputStream stream = getClass().getResourceAsStream("/" + resourceName)) {
                byte[] buffer = new byte[1240];
                int s = 0;
                while ((s = stream.read(buffer)) > 0) {
                    out.write(buffer, 0, s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
