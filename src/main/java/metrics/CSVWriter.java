package metrics;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CSVWriter implements Closeable {
    private final PrintWriter out;
    public CSVWriter(File f, String header) throws IOException {
        out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f, false), StandardCharsets.UTF_8));
        if (header != null) out.println(header);
    }
    public void row(Object... cells) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(cells[i]);
        }
        out.println(sb);
        out.flush();
    }
    @Override public void close() { out.close(); }
}
