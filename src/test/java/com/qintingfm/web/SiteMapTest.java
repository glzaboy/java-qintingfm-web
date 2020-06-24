package com.qintingfm.web;

import com.qintingfm.web.pojo.sitemap.SiteMap;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.PrintWriter;

@SpringBootTest
public class SiteMapTest {
    @Test
    void test() throws JAXBException {
        SiteMap siteMap=new SiteMap();
        JAXBContext jaxbContext=JAXBContext.newInstance(SiteMap.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        PrintWriter println=new PrintWriter(System.out);
        marshaller.marshal(siteMap,println);
    }
}
