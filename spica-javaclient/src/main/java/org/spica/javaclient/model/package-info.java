@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type= LocalDateTime.class, value= LocalDateTimeXmlAdapter.class)
})
package org.spica.javaclient.model;

import com.migesok.jaxb.adapter.javatime.LocalDateTimeXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.LocalDateTime;