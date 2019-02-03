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
package org.rookit.serialization;

import org.rookit.utils.optional.Optional;
import org.rookit.utils.optional.OptionalFactory;

final class PropertySerializer<T> implements Serializer<T> {

    private final Serializer<T> valueSerializer;
    private final String propertyName;
    private final OptionalFactory optionalFactory;

    PropertySerializer(final Serializer<T> valueSerializer,
                       final String propertyName,
                       final OptionalFactory optionalFactory) {
        this.valueSerializer = valueSerializer;
        this.propertyName = propertyName;
        this.optionalFactory = optionalFactory;
    }

    @Override
    public void write(final TypeWriter writer, final T value) {
        writer.name(this.propertyName);
        this.valueSerializer.write(writer, value);
    }

    @Override
    public T read(final TypeReader reader) {
        // ignores name
        reader.name();
        return this.valueSerializer.read(reader);
    }

    @Override
    public Optional<T> readOptional(final TypeReader reader) {
        if(this.propertyName.equals(reader.peekName())) {
            return this.optionalFactory.of(read(reader));
        }
        return this.optionalFactory.empty();
    }

    @Override
    public Class<T> serializationClass() {
        return this.valueSerializer.serializationClass();
    }

    @Override
    public String toString() {
        return "PropertySerializer{" +
                "valueSerializer=" + this.valueSerializer +
                ", propertyName='" + this.propertyName + '\'' +
                ", optionalFactory=" + this.optionalFactory +
                "}";
    }
}
