package core.dao.util;

import com.vaadin.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ByteArrayStreamResource extends StreamResource {

    private final byte[] data;
    private final String filename;

    public ByteArrayStreamResource(byte[] data, String filename) {
        super(new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                return new ByteArrayInputStream(data);
            }
        }, filename);
        this.data = data;
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return filename;
    }
}