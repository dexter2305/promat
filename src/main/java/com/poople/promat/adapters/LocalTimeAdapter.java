package com.poople.promat.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    @Override
    public LocalTime unmarshal(String valueAsString) throws Exception {
        return LocalTime.parse(valueAsString, DTFormatter.INSTANCE.getTimeFormatter());
    }

    @Override
    public String marshal(LocalTime localtime) throws Exception {
        return DTFormatter.INSTANCE.getTimeFormatter().format(localtime);
    }
}
