package hu.tilos.radio.backend.converters;

import org.modelmapper.AbstractConverter;

/**
 * Converter to prefix string value.
 */
public class PrefixingConverter extends AbstractConverter<String, String> {

    private String prefix;

    public PrefixingConverter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected String convert(String source) {
        return prefix + source;
    }
}
