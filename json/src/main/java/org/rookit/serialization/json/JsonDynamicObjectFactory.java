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

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import org.rookit.utils.object.DynamicObject;
import org.rookit.utils.object.DynamicObjectFactory;
import org.rookit.utils.object.MalformedObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

final class JsonDynamicObjectFactory implements DynamicObjectFactory {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(JsonDynamicObjectFactory.class);

    @SuppressWarnings("FieldNotUsedInToString") // no value added
    private final JsonParser parser;

    @Inject
    private JsonDynamicObjectFactory(final JsonParser parser) {
        this.parser = parser;
    }

    @Override
    public Collection<String> supportedTypes() {
        return ImmutableList.of("json");
    }

    @Override
    public DynamicObject fromRawContent(final String rawContent) throws IOException, MalformedObjectException {
        try {
            final JsonElement element = this.parser.parse(rawContent);
            final JsonObject jsonObject = element.getAsJsonObject();
            return new JsonDynamicObject(jsonObject);
        } catch (final JsonIOException e) {
            logger.warn("Json parser reported an error.", e);
            throw new IOException(e);
        } catch (final JsonSyntaxException e) {
            logger.warn("Json parser reported an error.", e);
            throw new MalformedObjectException("Json parser reported an error", e);
        }
    }

    @Override
    public String toString() {
        return "JsonDynamicObjectFactory{" +
                "}";
    }
}
