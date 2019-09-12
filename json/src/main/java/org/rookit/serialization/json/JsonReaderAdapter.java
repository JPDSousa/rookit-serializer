/*******************************************************************************
 * Copyright (C) 2018 Joao Sousa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.rookit.serialization.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.rookit.failsafe.Failsafe;
import org.rookit.serialization.TypeReader;

import java.io.IOException;
import java.util.Objects;

final class JsonReaderAdapter implements TypeReader {

    private final Failsafe failsafe;
    private final JsonParser reader;

    JsonReaderAdapter(final Failsafe failsafe, final JsonParser reader) {
        this.failsafe = failsafe;
        this.reader = reader;
    }

    @Override
    public String name() {
        try {
            return this.reader.currentName();
        } catch (final IOException e) {
            return this.failsafe.handleException().inputOutputException(e);
        }
    }

    @Override
    public String peekName() {
        return name();
    }

    private void moveCursor(final JsonToken expectedToken) {
        if (Objects.equals(this.reader.getCurrentToken(), expectedToken)) {
            try {
                this.reader.nextToken();
            } catch (final IOException e) {
                this.failsafe.handleException().inputOutputException(e);
            }
        }
    }

    @Override
    public void startDocument() {
        moveCursor(JsonToken.START_OBJECT);
    }

    @Override
    public void endDocument() {
        moveCursor(JsonToken.END_OBJECT);
    }

    @Override
    public void startArray() {
        moveCursor(JsonToken.START_ARRAY);
    }

    @Override
    public void endArray() {
        moveCursor(JsonToken.END_ARRAY);
    }

    @Override
    public boolean hasNext() {
        final JsonToken currentToken = this.reader.getCurrentToken();
        return (currentToken != JsonToken.END_OBJECT) && (currentToken != JsonToken.END_ARRAY);
    }

    @Override
    public String toString() {
        return "JsonReaderAdapter{" +
                "failsafe=" + this.failsafe +
                ", reader=" + this.reader +
                "}";
    }
}
