package com.easyms.common.ms.log;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author abessa
 */
public class ServletInputStreamWrapper extends ServletInputStream {

    private ByteArrayInputStream data;

    ServletInputStreamWrapper(ByteArrayInputStream data) {
        this.data = data;
    }

    @Override
    public int read() throws IOException {
        return data.read();
    }


    @Override
    public boolean isFinished() {
        return data.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        // nothing to do
    }
}