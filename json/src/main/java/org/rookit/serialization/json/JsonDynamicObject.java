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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.rookit.utils.object.DynamicObject;
import org.rookit.utils.object.NoSuchPropertyException;

import java.util.Objects;
import java.util.function.Function;

final class JsonDynamicObject implements DynamicObject {

    private final JsonObject delegate;

    JsonDynamicObject(final JsonObject delegate) {
        this.delegate = delegate;
    }

    private JsonElement getOrFail(final String name) {
        final JsonElement element = this.delegate.get(name);
        if (Objects.isNull(element)) {
            throw NoSuchPropertyException.missingProperty(name);
        }

        return element;
    }

    private <T> T getOrFail(final String name, final Function<JsonElement, T> conversion) {
        return conversion.apply(getOrFail(name));
    }

    @Override
    public DynamicObject getDynamicObject(final String name) {
        return new JsonDynamicObject(getOrFail(name, JsonElement::getAsJsonObject));
    }

    @Override
    public double getDouble(final String name) {
        return getOrFail(name).getAsDouble();
    }

    @Override
    public boolean getBoolean(final String name) {
        return getOrFail(name).getAsBoolean();
    }

    @Override
    public int getInt(final String name) {
        return getOrFail(name).getAsInt();
    }

    @Override
    public String getString(final String name) {
        return getOrFail(name, JsonElement::getAsString);
    }

    @Override
    public String toString() {
        return "JsonDynamicObject{" +
                "delegate=" + this.delegate +
                "}";
    }
}
