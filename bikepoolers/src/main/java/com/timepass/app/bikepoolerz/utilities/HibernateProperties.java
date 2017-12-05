/*package com.learn.springboot.utilities;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application_config.yml")
public class HibernateProperties {
	private String properties;
	public static class hibernateProperties {
        
        private String path;
        private String title;

        //getters and setters

        @Override
        public String toString() {
            return "Menu{" +
                    ", path='" + path + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }

		
    }

    public static class Compiler {
        private String timeout;
        private String outputFolder;

        //getters and setters

        @Override
        public String toString() {
            return "Compiler{" +
                    "timeout='" + timeout + '\'' +
                    ", outputFolder='" + outputFolder + '\'' +
                    '}';
        }

    }
    
    public String getProperties() {
		return properties;
	}

	public void setProperties( String properties) {
		this.properties = properties;
	}

}
*/