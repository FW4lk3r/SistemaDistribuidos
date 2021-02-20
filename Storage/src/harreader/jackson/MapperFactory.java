package harreader.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import harreader.HarReaderMode;

public interface MapperFactory {

    ObjectMapper instance(HarReaderMode mode);

}
