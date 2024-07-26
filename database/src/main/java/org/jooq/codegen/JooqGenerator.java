package org.jooq.codegen;

import org.jooq.meta.jaxb.Configuration;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class JooqGenerator {
    public static void main(String[] args) throws UnsupportedEncodingException {
        URL url1 = JooqGenerator.class.getClassLoader().getResource("jooq-config.xml");
        String s = URLDecoder.decode(url1.getFile(), "utf-8");
        Configuration configuration = javax.xml.bind.JAXB.unmarshal(new File(s), Configuration.class);
        try {
            GenerationTool.generate(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
