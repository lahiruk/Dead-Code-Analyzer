package lk.devfactory.config;

import io.swagger.inflector.SwaggerInflector;
import io.swagger.inflector.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class InflectorConfig extends SwaggerInflector {

    @Autowired
    public InflectorConfig(Configuration configuration) {
        super(configuration);
    }

}
