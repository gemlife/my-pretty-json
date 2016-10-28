package com.abb.e7.core

import com.oracle.webservices.internal.api.message.PropertySet
import ru.qatools.properties.Property
import ru.qatools.properties.Resource

@Resource.Classpath("application.properties")
interface ApplicationProperties {

    @Property("base.url")
    String getBaseUrl()

    @Property("xml.url")
    String getXmlUrl()
}