@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type= LocalDateTime.class, value= LocalDateTimeXmlAdapter.class),
    @XmlJavaTypeAdapter(type= LocalDate.class, value= LocalDateXmlAdapter.class)
})
package org.spica.javaclient.model;

import com.migesok.jaxb.adapter.javatime.LocalDateTimeXmlAdapter;

import com.migesok.jaxb.adapter.javatime.LocalDateXmlAdapter;
import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.LocalDateTime;