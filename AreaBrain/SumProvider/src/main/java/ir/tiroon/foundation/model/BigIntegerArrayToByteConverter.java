package ir.tiroon.foundation.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter
public class BigIntegerArrayToByteConverter implements AttributeConverter<List<BigInteger>,String>{


    @Override
    public String convertToDatabaseColumn(List<BigInteger> bigIntegers) {
        return bigIntegers == null ? null : StringUtils.join(bigIntegers,",");
    }

    @Override
    public List<BigInteger> convertToEntityAttribute(String dbData) {
//       String dbData = new String(bytes);
       if (StringUtils.isBlank(dbData))
            return new ArrayList<>();

        try (Stream<String> stream = Arrays.stream(dbData.split(","))) {
            return stream.map(BigInteger::new).collect(Collectors.toList());
        }
    }
}
