package me.kevincampos.popularmovies.data.api;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class HTTPResponse {

    private final int STATUS_CODE;
    private final byte[] RESPONSE_IN_BYTES;
    private final String STRING_RESPONSE;

    static HTTPResponse with(final int RESPONSE_CODE, InputStream responseInputStream) throws IOException {
        byte[] responseInBytes = responseFromInputStream(responseInputStream);
        return new HTTPResponse(RESPONSE_CODE, responseInBytes);
    }

    private HTTPResponse(int responseCode, byte[] responseInBytes) throws UnsupportedEncodingException {
        STATUS_CODE = responseCode;
        RESPONSE_IN_BYTES = responseInBytes;

        if (RESPONSE_IN_BYTES == null) {
            STRING_RESPONSE = null;
        } else {
            STRING_RESPONSE = new String(RESPONSE_IN_BYTES, "UTF-8");
        }
    }

    private static byte[] responseFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        final byte[] RESPONSE_IN_BYTES = buffer.toByteArray();

        buffer.close();

        return RESPONSE_IN_BYTES;
    }

    public String getResponse() {
        return STRING_RESPONSE;
    }

    public @Nullable JSONObject getResponseAsJSON() throws JSONException {
        if (STRING_RESPONSE == null || STRING_RESPONSE.length() == 0 || STRING_RESPONSE.equals("null")) {
            return null;
        }
        return new JSONObject(STRING_RESPONSE);
    }

    public int getStatusCode() {
        return STATUS_CODE;
    }

    public boolean isSuccessful() {
        return STATUS_CODE >= 200 && STATUS_CODE < 300;
    }

}
