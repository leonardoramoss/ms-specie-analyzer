package io.species.analyzer.infrastructure.auditing.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.UUID;

public class ResponseWrapper extends HttpServletResponseWrapper {

    private PrintWriter printWriter;
    private ServletOutputStream servletOutputStream;
    private ServletOutputStreamWrapper servletOutputStreamWrapper;

    private final UUID uuid;

    /**
     * Constructor for {@link ResponseWrapper}
     *
     * @param response object with response information
     * @param uuid is unique identification from current request
     */
    public ResponseWrapper(final HttpServletResponse response, final UUID uuid) {
        super(response);
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (printWriter != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (servletOutputStream == null) {
            servletOutputStream = getResponse().getOutputStream();
            servletOutputStreamWrapper = new ServletOutputStreamWrapper(servletOutputStream);
        }

        return servletOutputStreamWrapper;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (servletOutputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (printWriter == null) {
            servletOutputStreamWrapper = new ServletOutputStreamWrapper(getResponse().getOutputStream());
            printWriter = new PrintWriter(new OutputStreamWriter(servletOutputStreamWrapper, getResponse().getCharacterEncoding()), true);
        }

        return printWriter;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (printWriter != null) {
            printWriter.flush();
        } else if (servletOutputStream != null) {
            servletOutputStreamWrapper.flush();
        }
    }

    /**
     * @return array of bytes to handle request response object
     */
    public byte[] toByteArray() {
        if (servletOutputStreamWrapper != null) {
            return servletOutputStreamWrapper.getCopy();
        } else {
            return new byte[0];
        }
    }

    private static class ServletOutputStreamWrapper extends ServletOutputStream {

        private final OutputStream outputStream;
        private final ByteArrayOutputStream byteArrayOutputStream;

        private ServletOutputStreamWrapper(final OutputStream outputStream) {
            this.outputStream = outputStream;
            this.byteArrayOutputStream = new ByteArrayOutputStream(1024);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(final WriteListener writeListener) { }

        @Override
        public void write(final int b) throws IOException {
            outputStream.write(b);
            byteArrayOutputStream.write(b);
        }

        public byte[] getCopy() {
            return byteArrayOutputStream.toByteArray();
        }
    }
}
