package files;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Collection;

public class Directory extends Resource {

    private Collection<File> files;

    public Directory(Collection<File> coll) {
        this.files = coll;
        this.CONTENT_TYPE = "text/html";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        JEditorPane p = new JEditorPane();
        p.setContentType("text/html");
        this.files.forEach(entry -> {
            sb.append(entry);
            sb.append("<br />");
        });
        p.setText(sb.toString());
        //return sb.toString();
        HTMLEditorKit kit = new HTMLEditorKit();
        HTMLDocument d = (HTMLDocument) p.getDocument();
        StringWriter writer = new StringWriter();
        try {
            kit.write(writer, d, 0, d.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        String s = writer.toString();
        return s;
    }

    @Override
    public byte[] readContentFromDisk() {
        return this.toString().getBytes(Charset.forName("UTF-8"));
    }
}
