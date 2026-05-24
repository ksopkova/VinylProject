package com.example.hellofx.vinyl.network.protocol;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JsonCodec {
    private JsonCodec() {
    }

    public static String stringify(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String string) {
            return "\"" + escape(string) + "\"";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof Map<?, ?> map) {
            StringBuilder builder = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) {
                    builder.append(',');
                }
                builder.append(stringify(String.valueOf(entry.getKey())));
                builder.append(':');
                builder.append(stringify(entry.getValue()));
                first = false;
            }
            return builder.append('}').toString();
        }
        if (value instanceof Iterable<?> iterable) {
            StringBuilder builder = new StringBuilder("[");
            boolean first = true;
            for (Object item : iterable) {
                if (!first) {
                    builder.append(',');
                }
                builder.append(stringify(item));
                first = false;
            }
            return builder.append(']').toString();
        }

        return stringify(value.toString());
    }

    public static Map<String, Object> parseObject(String json) {
        Object parsed = new Parser(json).parse();
        if (!(parsed instanceof Map<?, ?> parsedMap)) {
            throw new IllegalArgumentException("JSON root must be an object.");
        }

        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : parsedMap.entrySet()) {
            result.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return result;
    }

    private static String escape(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '"' -> builder.append("\\\"");
                case '\\' -> builder.append("\\\\");
                case '\b' -> builder.append("\\b");
                case '\f' -> builder.append("\\f");
                case '\n' -> builder.append("\\n");
                case '\r' -> builder.append("\\r");
                case '\t' -> builder.append("\\t");
                default -> {
                    if (c < 32) {
                        builder.append(String.format("\\u%04x", (int) c));
                    } else {
                        builder.append(c);
                    }
                }
            }
        }
        return builder.toString();
    }

    private static final class Parser {
        private final String input;
        private int index;

        private Parser(String input) {
            this.input = input;
        }

        private Object parse() {
            Object value = parseValue();
            skipWhitespace();
            if (index != input.length()) {
                throw error("Unexpected trailing content.");
            }
            return value;
        }

        private Object parseValue() {
            skipWhitespace();
            if (index >= input.length()) {
                throw error("Unexpected end of JSON.");
            }

            char current = input.charAt(index);
            return switch (current) {
                case '{' -> parseObjectValue();
                case '[' -> parseArrayValue();
                case '"' -> parseStringValue();
                case 't', 'f' -> parseBooleanValue();
                case 'n' -> parseNullValue();
                default -> {
                    if (current == '-' || Character.isDigit(current)) {
                        yield parseNumberValue();
                    }
                    throw error("Unexpected character '" + current + "'.");
                }
            };
        }

        private Map<String, Object> parseObjectValue() {
            expect('{');
            Map<String, Object> result = new LinkedHashMap<>();
            skipWhitespace();
            if (peek('}')) {
                expect('}');
                return result;
            }

            while (true) {
                String key = parseStringValue();
                skipWhitespace();
                expect(':');
                Object value = parseValue();
                result.put(key, value);
                skipWhitespace();
                if (peek('}')) {
                    expect('}');
                    return result;
                }
                expect(',');
            }
        }

        private List<Object> parseArrayValue() {
            expect('[');
            List<Object> result = new ArrayList<>();
            skipWhitespace();
            if (peek(']')) {
                expect(']');
                return result;
            }

            while (true) {
                result.add(parseValue());
                skipWhitespace();
                if (peek(']')) {
                    expect(']');
                    return result;
                }
                expect(',');
            }
        }

        private String parseStringValue() {
            expect('"');
            StringBuilder builder = new StringBuilder();
            while (index < input.length()) {
                char current = input.charAt(index++);
                if (current == '"') {
                    return builder.toString();
                }
                if (current == '\\') {
                    if (index >= input.length()) {
                        throw error("Unexpected end of escape sequence.");
                    }
                    char escaped = input.charAt(index++);
                    switch (escaped) {
                        case '"' -> builder.append('"');
                        case '\\' -> builder.append('\\');
                        case '/' -> builder.append('/');
                        case 'b' -> builder.append('\b');
                        case 'f' -> builder.append('\f');
                        case 'n' -> builder.append('\n');
                        case 'r' -> builder.append('\r');
                        case 't' -> builder.append('\t');
                        case 'u' -> builder.append(parseUnicodeEscape());
                        default -> throw error("Invalid escape sequence.");
                    }
                } else {
                    builder.append(current);
                }
            }
            throw error("Unterminated string.");
        }

        private char parseUnicodeEscape() {
            if (index + 4 > input.length()) {
                throw error("Invalid unicode escape.");
            }
            String hex = input.substring(index, index + 4);
            index += 4;
            try {
                return (char) Integer.parseInt(hex, 16);
            } catch (NumberFormatException e) {
                throw error("Invalid unicode escape.");
            }
        }

        private Boolean parseBooleanValue() {
            if (input.startsWith("true", index)) {
                index += 4;
                return true;
            }
            if (input.startsWith("false", index)) {
                index += 5;
                return false;
            }
            throw error("Invalid boolean value.");
        }

        private Object parseNullValue() {
            if (!input.startsWith("null", index)) {
                throw error("Invalid null value.");
            }
            index += 4;
            return null;
        }

        private Number parseNumberValue() {
            int start = index;
            if (peek('-')) {
                index++;
            }
            while (index < input.length() && Character.isDigit(input.charAt(index))) {
                index++;
            }
            boolean decimal = false;
            if (peek('.')) {
                decimal = true;
                index++;
                while (index < input.length() && Character.isDigit(input.charAt(index))) {
                    index++;
                }
            }
            if (index < input.length() && (input.charAt(index) == 'e' || input.charAt(index) == 'E')) {
                decimal = true;
                index++;
                if (index < input.length() && (input.charAt(index) == '+' || input.charAt(index) == '-')) {
                    index++;
                }
                while (index < input.length() && Character.isDigit(input.charAt(index))) {
                    index++;
                }
            }

            String number = input.substring(start, index);
            try {
                return decimal ? Double.parseDouble(number) : Long.parseLong(number);
            } catch (NumberFormatException e) {
                throw error("Invalid number value.");
            }
        }

        private void skipWhitespace() {
            while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
                index++;
            }
        }

        private boolean peek(char expected) {
            return index < input.length() && input.charAt(index) == expected;
        }

        private void expect(char expected) {
            if (!peek(expected)) {
                throw error("Expected '" + expected + "'.");
            }
            index++;
        }

        private IllegalArgumentException error(String message) {
            return new IllegalArgumentException(message + " At character " + index + ".");
        }
    }
}
