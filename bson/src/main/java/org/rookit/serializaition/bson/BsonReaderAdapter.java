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
package org.rookit.serializaition.bson;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.rookit.serialization.TypeReader;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
final class BsonReaderAdapter implements TypeReader {

    private final BsonReader reader;
    private boolean readName;

    BsonReaderAdapter(final BsonReader reader) {
        this.reader = reader;
        this.readName = false;
    }

    @Override
    public String name() {
        if (this.readName) {
            return this.reader.getCurrentName();
        }
        this.readName = true;
        return this.reader.readName();
    }

    @Override
    public String peekName() {
        return name();
    }

    @Override
    public void startDocument() {
        this.readName = false;
        this.reader.readStartDocument();
    }

    @Override
    public void endDocument() {
        this.readName = false;
        this.reader.readEndDocument();
    }

    @Override
    public void startArray() {
        this.readName = false;
        this.reader.readStartArray();
    }

    @Override
    public void endArray() {
        this.readName = false;
        this.reader.readEndArray();
    }

    @Override
    public boolean hasNext() {
        this.readName = false;
        return this.reader.getCurrentBsonType() != BsonType.END_OF_DOCUMENT;
    }

    @Override
    public String toString() {
        return "BsonReaderAdapter{" +
                "reader=" + this.reader +
                ", readName=" + this.readName +
                "}";
    }
}
